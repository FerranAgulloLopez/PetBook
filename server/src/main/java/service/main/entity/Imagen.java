package service.main.entity;

import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Imagen implements Serializable {

    private String imagen; // Imagen

    //private double maxSizeAttributeJSON = 1024*16 - 1024*1; // Maximo de bytes por fila del JSON

    File file;


    public Imagen() {
        setImagen(new String());
    }

    public Imagen(String path) throws IOException {         // USAR ESTA FUNCION PARA LEER LAS FOTOS(PASANDO EL PATH DE LA FOTO)
        setImagen(new String() );

        File file = new File(path);
        this.file = file;   // Guardar File
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        String encodedString = Base64.encodeBase64URLSafeString(fileContent);

        imagen = encodedString;
    }



    public String getEncodedString() {
        return getImagen();
    }

    public String getImagen() { return imagen;}

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public byte[] getBytes() {                  // Conseguir la imagen en byte[]
        return Base64.decodeBase64(imagen);
    }


    public File getFile() {
        return file;
    }

    public void setFile() {

    }





    @Override
    public String toString() {
        return getEncodedString();
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
