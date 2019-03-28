package com.example.conexion;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Conexion {

    private URL url;
    HttpURLConnection urlConnection = null;

    public void comprobarUsuario(String user, String pass) {
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("http://10.4.41.146:9999/ServerRESTAPI/ConfirmLogin?email=" + user + "&password=" + pass);
            //URL url1 = new URL(url + user + "&password=" + pass);
            //System.out.print(url1);

            urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

            Integer nume = urlConnection.getResponseCode();
            System.out.print(nume);

            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                //System.out.print(current);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

}
