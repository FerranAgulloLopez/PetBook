package com.example.PETBook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Adapters.CommentForumAdapter;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.CommentForumModel;
import com.example.PETBook.Models.ForumModel;
import com.example.pantallafirstview.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ForumInfo extends AppCompatActivity implements AsyncResult {

    private ForumModel forumModel;
    private TextView forumName;
    private TextView forumCreator;
    private TextView forumDataCreation;
    private TextView forumDescription;
    private ListView listCommentsForum;
    private String nameForum;
    private String creatorForum;
    private CommentForumAdapter commentForumAdapter;
    private ArrayList<CommentForumModel> commentForumModel;

    private ImageButton editButton;
    private ImageButton deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hilo_forum);

        forumName = findViewById(R.id.nombreForoInfo);
        forumCreator = findViewById(R.id.nombreCreadorForum);
        forumDataCreation = findViewById(R.id.dataCreacioForum);
        forumDescription = findViewById(R.id.descripcionForoInfo);


/*
        editButton = (ImageButton) findViewById(R.id.EditPetButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumInfo.this,EditEvent.class);
                intent.putExtra("event",event);
                startActivity(intent);
            }
        });


        deleteButton = (ImageButton) findViewById(R.id.imageButtonDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder error = new AlertDialog.Builder(ForumInfo.this);
                error.setMessage("Esta seguro que quiere eliminar el evento?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteEvent();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog errorE = error.create();
                errorE.setTitle("Eliminar evento");
                errorE.show();
            }
        });
*/

        recibirDatos();

    }

    private void recibirDatos(){
        Bundle datosRecibidos = this.getIntent().getExtras();
        if(datosRecibidos != null) {
            forumModel = (ForumModel) datosRecibidos.getSerializable("forum");
            System.out.print("La ventana recibe los datos ya que el bundle no es vacio\n");


            forumCreator.setText(forumModel.getCreatorMail());
            forumDataCreation.setText(forumModel.getCreationDate());
            forumDescription.setText(forumModel.getDescription());
            forumName.setText(forumModel.getTitle());

            creatorForum = forumModel.getCreatorMail();
            nameForum = forumModel.getTitle();
            commentForumModel = forumModel.getComments();
            //mostrarComments();
        }
    }
    private void mostrarComments(){
        /*Conexion con = new Conexion(this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/GetAllThreadComments?creatorMail=" + creatorForum + "&title=" + nameForum, "GET", null);*/

        commentForumAdapter = new CommentForumAdapter(this, commentForumModel);
        listCommentsForum.setAdapter(commentForumAdapter);
    }

    /*private void deleteEvent(){
        SingletonUsuario su = SingletonUsuario.getInstance();
        String localizacion = txtLoc.getText().toString();
        String titulo = txtTitle.getText().toString();
        String descripcion = txtDescription.getText().toString();
        Boolean publico = true;
        String Fecha = txtFecha.getText().toString() + "T" + txtHora.getText().toString() + ":00.000Z";
        String user = su.getEmail();

        JSONObject jsonToSend = new JSONObject();
        try {
            jsonToSend.accumulate("coordenadas", Integer.parseInt(localizacion)/10); //cambiar cuando se implemente MAPS
            jsonToSend.accumulate("descripcion", descripcion);
            jsonToSend.accumulate("fecha", Fecha); //2019-05-24T19:13:00.000Z formato fecha
            jsonToSend.accumulate("publico", publico);
            jsonToSend.accumulate("radio", 0); //No se trata el valor por Google Maps
            jsonToSend.accumulate("titulo", titulo);
            jsonToSend.accumulate("userEmail", user);
            System.out.print(jsonToSend);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Conexion con = new Conexion(this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/DeleteEvent/", "DELETE", jsonToSend.toString());
    }
*/

    @Override
    public void OnprocessFinish(JSONObject json) {

        try {
            if (json.getInt("code") == 200) {
                System.out.print(json.getInt("code")+ "Correcto+++++++++++++++++++++++++++\n");
                Toast.makeText(this, "Evento correctamente eliminado", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("fragment", "forum");
                startActivity(intent);
            } else {
                System.out.print(json.getInt("code")+ "Mal+++++++++++++++++++++++++++\n");
                Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
