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

    public static String comprobarUsuario(String user, String pass) {
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

            String aux = "";

            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();

                if(!(current == '{') && !(current == '"') && !(current == '}') ) {
                    aux += current;
                    System.out.print(current);
                }
                }
                /*else {
                    aux += "-1";*/
                    System.out.print("\n");
                //}
           // }

            String aux3 = "" ;
            Integer au1 = urlConnection.getResponseCode();
            System.out.print(aux+"\n");
            for(int i=1;i<aux.length();i++ ){
                if(aux.charAt(i-1) == ':'){
                    aux3+= aux.charAt(i);
                }

            }
            System.out.print(au1+"\n");
            //aux3 = "success:" + aux3;
            System.out.print(aux3+"\n");
            char success = aux3.charAt(0);
            char mailconf = aux3.charAt(1);
            System.out.print(success+"\n");
            System.out.print(mailconf+"\n");
            return aux3;
            //return aux3;
            /*System.out.print("\n");
            System.out.print(aux);
            String success = "";
            String mailconfirmed = "";
            while(!(aux == "-1")){
                success += aux;
            }
            System.out.print(success);
            System.out.print("\n");*/

        } catch (Exception e) {
            //e.printStackTrace();
            //return false;
            return "malament";
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        //return true;
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

/*
    public static void main(String[] args) {
        URL url;
        HttpURLConnection urlConnection = null;
        String i = comprobarUsuario("alex","alex1");
        System.out.print(i);
    }

*/

}
