package com.example.PETBook;

import android.os.AsyncTask;

import com.example.PETBook.Controllers.AsyncResult;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
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
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
            String input = "";
            while ((input = read.readLine()) != null){
                bf.append(input);
            }

            if(bf.length() != 0) {
                JSONObject inter = new JSONObject(bf.toString());
                result = inter;
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


    @Override
    public void onPostExecute(JSONObject Result) {
        this.asyncResult.OnprocessFinish(Result);
    }

}
