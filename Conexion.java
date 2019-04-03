package com.example.conexion;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Conexion extends AsyncTask<JSONObject,Void,JSONObject> {


    private URL url;
    HttpURLConnection urlConnection;

    public Conexion (String user, String pass){
        try {
            url = new URL("http://10.4.41.146:9999/ServerRESTAPI/ConfirmLogin?email=" + user + "&password=" + pass);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected JSONObject doInBackground(JSONObject... jsonObjects) {
        HttpURLConnection urlConnection = null;
        JSONObject result = new JSONObject();
        try {
            //URL url1 = new URL(url + user + "&password=" + pass);
            //System.out.print(url1);


            urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

            Integer nume = urlConnection.getResponseCode();

            InputStream in = null;
            StringBuffer bf = new StringBuffer();
            in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
            String input = "";
            while ((input = read.readLine()) != null){
                bf.append(input);
            }
            JSONObject inter = new JSONObject(bf.toString());
            String success = inter.getString("success");
            String mail = inter.getString("mailconfirmed");
            result.put("code",nume);
            result.put("success",success);
            result.put("mailconfirmed",mail);


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


    public static void main(String[] args) {
        URL url;
        HttpURLConnection urlConnection = null;
        JSONObject login = new JSONObject();
        try {
            String usuario = "alex";
            String password = "alex";
            login.put("user", usuario);
            login.put("password", password);
            Conexion i = new Conexion(usuario, password);
            System.out.print(i.execute(login));
        } catch (Exception e){
            e.printStackTrace();
        }
    }



}
