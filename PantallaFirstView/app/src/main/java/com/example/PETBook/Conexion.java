package com.example.PETBook;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Conexion {

    private URL url;
    private HttpURLConnection urlConnection = null;
    private static JSONObject json;

    public static boolean comprobarUsuario(String user, String pass) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL("http://10.4.41.146:9999/ServerRESTAPI/ConfirmLogin?email=" + user + "&password=" + pass);
            //URL url1 = new URL(url + user + "&password=" + pass);
            //System.out.print(url1);

            urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

            Integer nume = urlConnection.getResponseCode();

            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return true;
    }

    /*public static void crearEvento(){
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL("http://10.4.41.146:9999/ServerRESTAPI/CreaEvento");
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }*/


    public static void main(String[] args) {
        URL url;
        HttpURLConnection urlConnection = null;
        boolean i = comprobarUsuario("alex","alex");
        System.out.print(i);
    }



}
