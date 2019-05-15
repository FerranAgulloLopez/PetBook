package com.example.PETBook;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.time.LocalDateTime;
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
    private ImageButton addButtonComment;
    private TextInputLayout descriptionComment;
    private String tipoConexion;
    private ImageButton deleteForumButton;
    private ImageButton editForumButton;
    private TextInputLayout descriptionEdit;
    private ImageButton confirmEditDescription;
    private ImageButton cancelEditDescription;
    private ImageButton editCommentButton;
    private boolean editableComment;
    private Integer minutosCreacion;
    private ImageButton confirmEditComment;
    private ImageButton cancelEditComment;
    private TextInputLayout editComment;
    private TextView descripcionComment;
    private String fechaCreacion;
    private TextView dataComment;
    private TextView creatorComment;

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

        addButtonComment = (ImageButton) findViewById(R.id.addComment);
        addButtonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });
        ListView lv = (ListView) findViewById(R.id.list_comments_forum);
        lv.setClickable(true);

        lv.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (comprobarEditable()) {
                    descripcionComment = arg1.findViewById(R.id.descripcionComment);
                    editComment = arg1.findViewById(R.id.CommentDesign);
                    //editCommentButton = arg1.findViewById(R.id.editCommentButton);
                    dataComment = arg1.findViewById(R.id.dataComment);
                    creatorComment = arg1.findViewById(R.id.userCreatorComment);
                    cancelEditComment = arg1.findViewById(R.id.cancelEditComment);
                    confirmEditComment = arg1.findViewById(R.id.confirmEditComment);
                    editableComment = true;
                    editCommment();
                    //arg1.findViewById(R.id.editCommentButton).setVisibility(View.VISIBLE);
                    return false;
                } else {
                    Toast.makeText(ForumInfo.this, "You can't edit this comment", Toast.LENGTH_SHORT).show();
                    return false;
                }
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
                errorE.setTitle("Edit Forum");
                errorE.show();
            }
        });
        if(editableComment) {
            editCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder edit = new AlertDialog.Builder(ForumInfo.this);
                    edit.setMessage("Do you want to edit the comment?")
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
                                    editCommment();
                                }
                            });
                    AlertDialog errorE = edit.create();
                    errorE.setTitle("Edit Comment");
                    errorE.show();
                }
            });
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    private boolean comprobarEditable(){
        LocalDateTime ahora= LocalDateTime.now();
        Integer minutos = ahora.getMinute();
        /*if( minutos - minutosCreacion <= 5){
            return true;
        }*/
        return true;
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

    private String transformacionStringAURL(String titulo){
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
        minutosCreacion = ahora.getMinute();
        return fechaRetorno;
    }

    private void mostrarComments(){
        tipoConexion = "mostrarComments";
        Conexion con = new Conexion(this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/forum/GetAllThreadComments?creatorMail=" + creatorForum + "&title=" + transformacionStringAURL(nameForum) , "GET", null);
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
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/forum/DeleteForumThread?creatorMail=" + creatorForum + "&title=" + transformacionStringAURL(nameForum) , "DELETE", null);
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
            con.execute("http://10.4.41.146:9999/ServerRESTAPI/forum/CreateNewForumComment?creatorMail=" + creatorForum + "&title=" + transformacionStringAURL(nameForum), "POST", jsonToSend.toString());
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
                                    con.execute("http://10.4.41.146:9999/ServerRESTAPI/forum/UpdateForumThread?creatorMail=" + creatorForum + "&title=" + transformacionStringAURL(nameForum), "PUT", jsonToSend.toString());

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

    private String transformacionFechaURL(String fechaHora) throws ParseException{
        Integer fin = 0;
        String result = fechaHora.replace(":", "%3A");
        result = result.replace(" ","T");

        return result;
    }

    private void editCommment(){
       /* LocalDateTime ahora= LocalDateTime.now();
        Integer minutosActual = ahora.getMinute();*/
        tipoConexion = "editarComment";
        descripcionComment.setVisibility(View.INVISIBLE);
        editComment.setVisibility(View.VISIBLE);
        confirmEditComment.setVisibility(View.VISIBLE);
        cancelEditComment.setVisibility(View.VISIBLE);


        confirmEditComment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder edit = new AlertDialog.Builder(ForumInfo.this);
                edit.setMessage("Do you want to confirm the changes?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String updateDate = crearFechaActual();
                                String description = editComment.getEditText().getText().toString();
                                //confirmEditDescription = (ImageButton) findViewById(R.id.confirmEditDescription);
                                boolean isValidComment = validarComment(description, editComment);
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
                                    try {
                                        //http://10.4.41.146:9999/ServerRESTAPI/forum/UpdateForumComment?commentCreationDate=2019-05-12T14%3A11%3A18.0000&commentCreatorMail=A&threadCreatorMail=A&threadTitle=Mi%20foro22
                                        con.execute("http://10.4.41.146:9999/ServerRESTAPI/forum/UpdateForumComment?commentCreationDate=" + transformacionFechaURL(fechaCreacion) + "&commentCreatorMail=A"
                                                +"&threadCreatorMail=A" /*+ forumCreator*/ + "&threadTitle=Mi%20Foro22"/* + transformacionStringAURL(forumName.getText().toString())*/, "PUT", jsonToSend.toString());
                                        System.out.println("http://10.4.41.146:9999/ServerRESTAPI/forum/UpdateForumComment?commentCreationDate=" + transformacionFechaURL(fechaCreacion) + "&commentCreatorMail="
                                                + creatorComment.getText().toString() +"&threadCreatorMail=" + forumCreator.getText().toString() + "&threadTitle=" + transformacionStringAURL(forumName.getText().toString()));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

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
                errorE.setTitle("Edit Comment");
                errorE.show();
            }
        });


        cancelEditComment.setOnClickListener(new View.OnClickListener() {
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
                errorE.setTitle("Cancel edit Comment");
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
                        fechaCreacion=cf.getCreationDate();
                       // System.out.println(cf.getCreationDate());
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
        else if(tipoConexion.equals("editarComment")){
            try {
                if(json.getInt("code") == 200){
                    Toast.makeText(this, "Comment successfully edited", Toast.LENGTH_SHORT).show();
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
