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
    private static String mainURL = "http://10.4.41.146:9999/ServerRESTAPI/";

    public RespuestaServer comprobarUsuario(String imputURL, String body) {
        HttpURLConnection urlConnection = null;
        RespuestaServer rs = new RespuestaServer(null, 200);

        try {
            URL url = new URL(mainURL + imputURL);
            urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

            Integer nume = urlConnection.getResponseCode();
            rs.setCodigo(nume);
            if (nume == 200) {
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                String resp = "";
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
                    resp += current;
                }
                JSONObject success = new JSONObject(resp);
                rs.setJson(success);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return rs;
    }


    /*public static void main(String[] args) {
        URL url;
        HttpURLConnection urlConnection = null;
        boolean i = comprobarUsuario("alex1","alex");
        System.out.print(i);
    }*/

}


