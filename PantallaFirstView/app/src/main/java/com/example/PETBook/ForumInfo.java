package com.example.PETBook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        listCommentsForum = findViewById(R.id.list_comments_forum);

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

    private void mostrarComments(){
        Conexion con = new Conexion(this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/forum/GetAllThreadComments?creatorMail=" + creatorForum + "&title=" + tranformacionStringAURL(nameForum) , "GET", null);
        System.out.println("conexion bien hecha");
    }

    @Override
    public void OnprocessFinish(JSONObject json) {

        try {
            if(json.getInt("code") == 200){
                commentForumModel = new ArrayList<>();
                JSONArray jsonArray = json.getJSONArray("array");
                for(int i = 0; i < jsonArray.length(); ++i){
                    JSONObject forum = jsonArray.getJSONObject(i);
                    CommentForumModel cf = new CommentForumModel();
                    cf.setCreationDate(transformacionFechaHora(forum.getString("creationDate")));
                    cf.setCreatorMail(forum.getString("creatorMail"));
                    cf.setDescription(forum.getString("description"));
                    commentForumModel.add(cf);
                }
                commentForumAdapter = new CommentForumAdapter(this, commentForumModel);
                listCommentsForum.setAdapter(commentForumAdapter);
                System.out.print(json.getInt("code") + " se muestran correctamente la lista de comments\n");
            }
            else{
                System.out.print("El sistema no logra mostrar la lista de foros del creador\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
