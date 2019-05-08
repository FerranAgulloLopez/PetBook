package com.example.PETBook;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Adapters.CommentForumAdapter;
import com.example.PETBook.Adapters.EventAdapter;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.CommentForumModel;
import com.example.PETBook.Models.EventModel;
import com.example.PETBook.Models.ForumModel;
import com.example.pantallafirstview.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

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
    private FloatingActionButton addButtonComment;
    private TextInputLayout descriptionComment;
    private String tipoConexion;
    private ImageButton deleteForumButton;
    private ImageButton editForumButton;
    private TextInputLayout descriptionEdit;
    private ImageButton confirmEditDescription;
    private ImageButton cancelEditDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hilo_forum);

        forumName = findViewById(R.id.nombreForoInfo);
        forumCreator = findViewById(R.id.nombreCreadorForum);
        forumDataCreation = findViewById(R.id.dataCreacioForum);
        forumDescription = findViewById(R.id.descripcionForoInfo);
        listCommentsForum = findViewById(R.id.list_comments_forum);
        descriptionComment = findViewById(R.id.Comment);
        deleteForumButton = findViewById(R.id.deleteForumButton);
        editForumButton = findViewById(R.id.editForumButton);
        descriptionEdit = findViewById(R.id.editDescription);
        confirmEditDescription = findViewById(R.id.confirmEditDescription);
        cancelEditDescription = findViewById(R.id.cancelEditDescription);

        recibirDatos();

        addButtonComment = (FloatingActionButton) findViewById(R.id.addComment);
        addButtonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();

            }
        });
        ListView lv = (ListView) findViewById(R.id.list_comments_forum);

        lv.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        deleteForumButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder edit = new AlertDialog.Builder(ForumInfo.this);
                edit.setMessage("Are you sure you want to delete the forum?\nYou can't undo the changes.")
                        .setCancelable(false)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteForum();
                            }
                        });
                AlertDialog errorE = edit.create();
                errorE.setTitle("Delete Forum");
                errorE.show();
            }
        });
        editForumButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder edit = new AlertDialog.Builder(ForumInfo.this);
                edit.setMessage("Do you want to edit the forum?")
                        .setCancelable(false)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editForum();
                            }
                        });
                AlertDialog errorE = edit.create();
                errorE.setTitle("Delete Forum");
                errorE.show();
            }
        });
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

            SingletonUsuario su = SingletonUsuario.getInstance();
            creatorForum = forumModel.getCreatorMail();
            nameForum = forumModel.getTitle();

            if(su.getEmail().equals(creatorForum)){
                deleteForumButton.setVisibility(View.VISIBLE);
                editForumButton.setVisibility(View.VISIBLE);
            }
            else{
                deleteForumButton.setVisibility(View.INVISIBLE);
                editForumButton.setVisibility(View.INVISIBLE);
            }

            mostrarComments();
        }
    }

    private String tranformacionStringAURL (String titulo){
        String result = titulo.replace(" ", "%20");
        return result;
    }

    private String transformacionFechaHora(String fechaHora) throws ParseException {
        Integer fin = 0;
        String result = fechaHora.replace("T", " ");
        result = result.replace("+0000","");

        return result;
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
        String fechaRetorno = año + "-" + mes+ "-" + dia + "T" + hora + ":" + minutos + ":" + segundos + ".000Z";
        System.out.println(fechaRetorno);
        return fechaRetorno;
    }

    private void mostrarComments(){
        tipoConexion = "mostrarComments";
        Conexion con = new Conexion(this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/forum/GetAllThreadComments?creatorMail=" + creatorForum + "&title=" + tranformacionStringAURL(nameForum) , "GET", null);
        System.out.println("conexion mostrar comments bien hecha");
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
    private void deleteForum(){
        tipoConexion = "deleteForum";
        Conexion con = new Conexion(this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/forum/DeleteForumThread?creatorMail=" + creatorForum + "&title=" + tranformacionStringAURL(nameForum) , "DELETE", null);
    }

    private void addComment(){
        tipoConexion = "addComment";
        System.out.println("Comienzo add comment llego bien");
        SingletonUsuario su = SingletonUsuario.getInstance();
        String creatorComment = su.getEmail();
        String creationDate = crearFechaActual();
        String description = descriptionComment.getEditText().getText().toString();

        boolean isValidComment = validarComment(description, descriptionComment);
        if(isValidComment) {
              JSONObject jsonToSend = new JSONObject();
            try {
                jsonToSend.accumulate("creationDate", creationDate);
                jsonToSend.accumulate("creatorMail", creatorComment);
                jsonToSend.accumulate("description", description);
                System.out.println(jsonToSend);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Conexion con = new Conexion(this);
            con.execute("http://10.4.41.146:9999/ServerRESTAPI/forum/CreateNewForumComment?creatorMail=" + creatorForum + "&title=" + tranformacionStringAURL(nameForum), "POST", jsonToSend.toString());
            System.out.println("aixo es la resposta " );
            Bundle enviar = new Bundle();
            Intent intent = new Intent(this, MainActivity.class);
            enviar.putString("fragment","forum");
            intent.putExtras(enviar);
            startActivity(intent);
            //System.out.println("conexion add comment bien hecha");
            //System.out.println("Si llego aqui no peta");
        }
    }
    private void editForum(){
        tipoConexion = "editForum";
        forumDescription.setVisibility(View.INVISIBLE);
        descriptionEdit.setVisibility(View.VISIBLE);
        confirmEditDescription.setVisibility(View.VISIBLE);
        cancelEditDescription.setVisibility(View.VISIBLE);


        confirmEditDescription.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder edit = new AlertDialog.Builder(ForumInfo.this);
                edit.setMessage("Do you want to confirm the changes?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String updateDate = crearFechaActual();
                                String description = descriptionEdit.getEditText().getText().toString();
                                confirmEditDescription = (ImageButton) findViewById(R.id.confirmEditDescription);
                                boolean isValidComment = validarComment(description, descriptionEdit);
                                if(isValidComment) {
                                    JSONObject jsonToSend = new JSONObject();
                                    try {
                                        jsonToSend.accumulate("updateDate", updateDate);
                                        jsonToSend.accumulate("description", description);
                                        System.out.println(jsonToSend);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Conexion con = new Conexion(ForumInfo.this);
                                    con.execute("http://10.4.41.146:9999/ServerRESTAPI/forum/UpdateForumThread?creatorMail=" + creatorForum + "&title=" + tranformacionStringAURL(nameForum), "PUT", jsonToSend.toString());

                                    Bundle enviar = new Bundle();
                                    Intent intent = new Intent(ForumInfo.this, MainActivity.class);
                                    enviar.putString("fragment","forum");
                                    intent.putExtras(enviar);
                                    startActivity(intent);
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog errorE = edit.create();
                errorE.setTitle("Edit Forum");
                errorE.show();
            }
        });

        cancelEditDescription = (ImageButton) findViewById(R.id.cancelEditDescription);
        cancelEditDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder noedit = new AlertDialog.Builder(ForumInfo.this);
                noedit.setMessage("Do you want to cancel the changes?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Bundle enviar = new Bundle();
                                Intent intent = new Intent(ForumInfo.this, MainActivity.class);
                                enviar.putString("fragment","forum");
                                intent.putExtras(enviar);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog errorE = noedit.create();
                errorE.setTitle("Cancel edit Forum");
                errorE.show();
            }
        });

    }

    @Override
    public void OnprocessFinish(JSONObject json) {
        if (tipoConexion.equals("mostrarComments")) {
            try {
                if (json.getInt("code") == 200) {
                    commentForumModel = new ArrayList<>();
                    JSONArray jsonArray = json.getJSONArray("array");
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject forum = jsonArray.getJSONObject(i);
                        CommentForumModel cf = new CommentForumModel();
                        cf.setCreationDate(transformacionFechaHora(forum.getString("creationDate")));
                        cf.setCreatorMail(forum.getString("creatorMail"));
                        cf.setDescription(forum.getString("description"));
                        //cf.setTamaño(jsonArray.length());
                        commentForumModel.add(cf);
                    }
                    commentForumAdapter = new CommentForumAdapter(this, commentForumModel);
                    listCommentsForum.setAdapter(commentForumAdapter);
                    System.out.print(json.getInt("code") + " se muestran correctamente la lista de comments\n");
                } else {
                    System.out.print("El sistema no logra mostrar la lista de foros del creador\n");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(tipoConexion.equals("deleteForum")){
            try {
                if(json.getInt("code") == 200){
                    Toast.makeText(this, "Forum successfully deleted", Toast.LENGTH_SHORT).show();
                    Bundle enviar = new Bundle();
                    Intent intent = new Intent(this, MainActivity.class);
                    enviar.putString("fragment","forum");
                    intent.putExtras(enviar);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, "There was a problem during the process", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (tipoConexion.equals("editForum")){
            try {
                if(json.getInt("code") == 200){
                    Toast.makeText(this, "Forum successfully edited", Toast.LENGTH_SHORT).show();
                    Bundle enviar = new Bundle();
                    Intent intent = new Intent(this, MainActivity.class);
                    enviar.putString("fragment","forum");
                    intent.putExtras(enviar);
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
