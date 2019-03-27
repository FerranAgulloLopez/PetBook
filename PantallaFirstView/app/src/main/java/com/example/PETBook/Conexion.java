package com.example.PETBook;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Conexion {
    public static boolean comprobarUsuario(String user, String pass) {
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("http://10.4.41.146:9999/ServerRESTAPI/ConfirmLogin?email=");
            URL url1 = new URL(url + user + "&password=" + pass);


            urlConnection = (HttpURLConnection) url
                    .openConnection();

            Integer nume = urlConnection.getResponseCode();

            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                //System.out.print(current);
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}

