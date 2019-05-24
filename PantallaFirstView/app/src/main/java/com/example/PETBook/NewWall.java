package com.example.PETBook;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.Image;
import com.example.pantallafirstview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class NewWall extends AppCompatActivity implements AsyncResult {




    private TextInputLayout newWall;
    private ImageView imatgeUser;
    private ImageButton cancel;
    private ImageButton confirm;
    private String tipoConexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_wall);

        imatgeUser = findViewById(R.id.imatgeNewWall);
        cancel = findViewById(R.id.cancelWall);
        confirm = findViewById(R.id.confirmWall);
        newWall = findViewById(R.id.crearWall);
        getPicture();

        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
               addNewWall();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewWall.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getPicture(){
        System.out.println("entro a mostrar imatge");
        tipoConexion = "getImatge";
        Conexion con = new Conexion(this);
        SingletonUsuario su = SingletonUsuario.getInstance();
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/getPicture/" + su.getEmail(), "GET", null);
        System.out.println("conexio walls ben feta");

    }

    @TargetApi(Build.VERSION_CODES.O)
    private String crearFechaActual() {
        LocalDateTime ahora= LocalDateTime.now();
        String año = String.valueOf(ahora.getYear());
        String mes = String.valueOf(ahora.getMonthValue());
        String dia = String.valueOf(ahora.getDayOfMonth());
        String hora = String.valueOf(ahora.getHour());
        String minutos = String.valueOf(ahora.getMinute());
        String segundos = String.valueOf(ahora.getSecond());
        if(ahora.getMonthValue() < 10) mes = "0" + mes;
        if(ahora.getDayOfMonth() < 10) dia = "0" + dia;
        if(ahora.getHour() < 10) hora = "0" + hora;
        if(ahora.getMinute() < 10) minutos = "0" + minutos;
        if(ahora.getSecond() < 10) segundos = "0" + segundos;
        String fechaRetorno = año + "-" + mes+ "-" + dia + "T" + hora + ":" + minutos + ":" + segundos + ".000Z";
        System.out.println(fechaRetorno);
        return fechaRetorno;
    }

    private boolean validarComment(String comment, TextInputLayout textInputLayout){
        if(comment.isEmpty()){
            textInputLayout.setError("Required field");
            return false;
        }
        else{
            textInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    public void addNewWall(){

        String fechaHora = crearFechaActual();
        String description = newWall.getEditText().getText().toString().trim();
        boolean isValidDescription = validarComment(description, newWall);
        if(isValidDescription) {
            tipoConexion = "afegirWall";
            JSONObject jsonToSend = new JSONObject();
            try {
                jsonToSend.accumulate("description", description);
                jsonToSend.accumulate("creationDate", fechaHora);
                //jsonToSend.accumulate("ok", "true");
                System.out.println(jsonToSend);
            } catch (JSONException e) {
                e.printStackTrace();


            }
            Conexion con = new Conexion(NewWall.this);
            SingletonUsuario su = SingletonUsuario.getInstance();
            con.execute("http://10.4.41.146:9999/ServerRESTAPI/users/WallPosts", "POST", jsonToSend.toString());
        }
    }

    @Override
    public void OnprocessFinish(JSONObject json) {
        if(tipoConexion.equals("getImatge")){
            try {
                System.out.println("entro a mostrar la imagen");
                if (json.getInt("code")==200) {
                    // convert string to bitmap
                    SingletonUsuario user = SingletonUsuario.getInstance();
                    Image imagenConversor = Image.getInstance();
                    String image = json.getString("image");
                    Bitmap profileImage = imagenConversor.StringToBitMap(image);
                    imatgeUser.setImageBitmap(profileImage);
                    //user.setProfilePicture(profileImage);
                } else {
                    //Toast.makeText(HomeWallFragment.this, "There was a problem during the process.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(tipoConexion.equals("afegirWall")){
            try {
                if(json.getInt("code")==200){
                    Toast.makeText(this, "Post added succesfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NewWall.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, "There was a problem during the process", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}