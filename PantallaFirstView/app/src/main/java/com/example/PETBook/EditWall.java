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
import com.example.PETBook.Models.ForumModel;
import com.example.PETBook.Models.Image;
import com.example.pantallafirstview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditWall extends AppCompatActivity implements AsyncResult {




    private TextInputLayout newWall;
    private ImageView imatgeUser;
    private ImageButton cancel;
    private ImageButton confirm;
    private String tipoConexion;
    private String idComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wall);

        imatgeUser = (CircleImageView) findViewById(R.id.imatgeEditWall);
        cancel = findViewById(R.id.cancelEditWall);
        confirm = findViewById(R.id.confirmEditWall);
        newWall = findViewById(R.id.editWall);
        getPicture();
        recibirDatos();
        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateWall();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditWall.this, MainActivity.class);
                intent.putExtra("fragment","myprofile");
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
    private void recibirDatos(){
        Bundle datosRecibidos = this.getIntent().getExtras();
        if(datosRecibidos != null) {
            idComment = (String) datosRecibidos.getSerializable("idComment");
            System.out.println("idComment: " + idComment);
        }
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

    public void updateWall() {

        String fechaHora = crearFechaActual();
        String description = newWall.getEditText().getText().toString().trim();
        boolean isValidDescription = validarComment(description, newWall);
        if (isValidDescription) {
            tipoConexion = "updateWall";
            JSONObject jsonToSend = new JSONObject();
            try {
                jsonToSend.accumulate("description", description);
                jsonToSend.accumulate("updateDate", fechaHora);
                //jsonToSend.accumulate("ok", "true");
                System.out.println(jsonToSend);
            } catch (JSONException e) {
                e.printStackTrace();


            }
            Conexion con = new Conexion(EditWall.this);
            SingletonUsuario su = SingletonUsuario.getInstance();
            con.execute("http://10.4.41.146:9999/ServerRESTAPI/users/WallPosts/" + idComment, "PUT", jsonToSend.toString());

        }
    }

    @Override
    public void OnprocessFinish(JSONObject json) {
        if (tipoConexion.equals("getImatge")) {
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
        else if(tipoConexion.equals("updateWall")){
            try {
                if(json.getInt("code")==200){
                    Toast.makeText(this, "Post updated succesfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditWall.this, MainActivity.class);
                    intent.putExtra("fragment", "myprofile");
                    intent.putExtra("nameProfile", SingletonUsuario.getInstance().getEmail());
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
