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

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewWallCommunity extends AppCompatActivity implements AsyncResult {




    private TextInputLayout newWall;
    private ImageView imatgeUser;
    private ImageButton cancel;
    private ImageButton confirm;
    private String tipoConexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_wall);

        imatgeUser = (CircleImageView) findViewById(R.id.imatgeNewWall);
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
                Intent intent = new Intent(NewWallCommunity.this, MainActivity.class);
                intent.putExtra("fragment", "home");
                intent.putExtra("nameProfile", SingletonUsuario.getInstance().getEmail());

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
        Date date = new Date();
        return Long.toString(date.getTime());
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
            Conexion con = new Conexion(NewWallCommunity.this);
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
                    //Toast.makeText(WallFragment.this, "There was a problem during the process.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(tipoConexion.equals("afegirWall")){
            try {
                if(json.getInt("code")==200){
                    Toast.makeText(this, "Post added succesfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NewWallCommunity.this, MainActivity.class);
                    intent.putExtra("fragment", "home");
                    intent.putExtra("nameProfile", SingletonUsuario.getInstance().getEmail());

                    startActivity(intent);
                    finish();
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
