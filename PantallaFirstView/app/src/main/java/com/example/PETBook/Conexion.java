package com.example.PETBook;

import android.os.AsyncTask;

import com.example.PETBook.Controllers.AsyncResult;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Conexion extends AsyncTask<String, Void, JSONObject> {


    private URL url;
    private String Metodo;
    private String JsonString;
    private AsyncResult asyncResult;

    private JSONObject body;
    HttpURLConnection urlConnection;

    public Conexion (AsyncResult asyncResult){
        this.asyncResult = asyncResult;
    }

    @Override
    protected JSONObject doInBackground(String... params) {

        try {
            url = new URL(params[0]);
            Metodo = params[1];
            if (params[2] != null) JsonString = params[2];
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        JSONObject result = new JSONObject();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(Metodo);
            urlConnection.setRequestProperty("Content-Type", "application/json");

            URL u = urlConnection.getURL();
            System.out.println(u.toString());
            urlConnection.connect();    //


            if (this.JsonString != null) {
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(JsonString);
                wr.flush();
            }


            Integer nume = urlConnection.getResponseCode();




            InputStream in = null;
            StringBuffer bf = new StringBuffer();
            in = new BufferedInputStream(urlConnection.getInputStream());

            String response = parseResponse(in);
            if(response.length() != 0){
                Object json = new JSONTokener(response).nextValue();
                if (json instanceof JSONArray) {
                    JSONArray array = (JSONArray) json;
                    result.accumulate("array", array);
                } else if (json instanceof JSONObject) {
                    result = (JSONObject) json;
                }
            }
            result.put("code",nume);



        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONObject();
            try {
                result.put("code", "404");
            } catch (Exception e1){
                e1.printStackTrace();
            }
            return result;

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
    }

    private String parseResponse(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String output;
        try {
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public void onPostExecute(JSONObject Result) {
        this.asyncResult.OnprocessFinish(Result);
    }

}
