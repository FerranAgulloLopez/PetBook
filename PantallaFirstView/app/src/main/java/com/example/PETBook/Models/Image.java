package com.example.PETBook.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;


import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class Image implements Serializable {


    private static Image instance = null;

    private Image() {
    }

    public static Image getInstance() {
        if (instance == null) instance = new Image();
        return instance;
    }


    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public String BitmapToString(Bitmap picture) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // In case you want to compress your image, here it's at 40%
        picture.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }



/*

    // FUNCIONES AUXILIARES

    private void partirImagen(String encodedString) {
        double size = encodedString.length();
        double parts = Math.ceil(size/maxSizeAttributeJSON);

        for(Integer i =0; i< parts; i++) {
            double begin = maxSizeAttributeJSON * i;
            double end = begin + maxSizeAttributeJSON;
            if (end >= encodedString.length()) end = encodedString.length();
            this.add(encodedString.substring((int) begin, (int) end));
        }
    }

    private String unirImagen(List<String> imagen) {
        String result = "";
        for(String s : imagen) {
            result = result + s;
        }
        return result;
    }


    public void add(String substring) {
        imagen.add(substring);
    }
*/

}