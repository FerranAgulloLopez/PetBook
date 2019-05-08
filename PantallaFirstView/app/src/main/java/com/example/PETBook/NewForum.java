package com.example.PETBook;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Fragments.MyEventsFragment;
import com.example.pantallafirstview.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class NewForum extends AppCompatActivity implements AsyncResult {


    private TextInputLayout inputForumTitle;
    private TextInputLayout inputForumTopic;
    private TextInputLayout inputForumDescription;

    private Button addForumButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_forum);

        inputForumTitle = findViewById(R.id.ForumTitle);
        inputForumTopic = findViewById(R.id.ForumTopic);
        inputForumDescription = findViewById(R.id.Description);
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


    private boolean validateTitle(String titulo) {
        if(titulo.isEmpty()){
            inputForumTitle.setError("Required field");
            return false;
        }
        else{
            inputForumTitle.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateTopic(String topic) {
        if(topic.isEmpty()){
            inputForumTopic.setError("Required field");
            return false;
        }
        else{
            inputForumTopic.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateDescription(String description) {
        if(description.isEmpty()){
            inputForumDescription.setError("Required field");
            return false;
        }
        else{
            inputForumDescription.setErrorEnabled(false);
            return true;
        }
    }
    public void addNewForum(View view) throws JSONException{
        SingletonUsuario su = SingletonUsuario.getInstance();

        String titulo = inputForumTitle.getEditText().getText().toString();
        String topic = inputForumTopic.getEditText().getText().toString();
        String descripcion = inputForumDescription.getEditText().getText().toString();

        String user = su.getEmail();
        //boolean pubOpriv = publicButton.isChecked();

        boolean validTitle = validateTitle(titulo);
        boolean validTopic = validateTopic(topic);
        boolean validDescription = validateDescription(descripcion);
        String fechaHora = crearFechaActual();
        if (validTitle && validTopic && validDescription) {
            JSONObject jsonToSend = new JSONObject();
            try {
                jsonToSend.accumulate("creationDate", fechaHora);
                jsonToSend.accumulate("creatorMail", user);
                jsonToSend.accumulate("description", descripcion);
                jsonToSend.accumulate("title", titulo);
                jsonToSend.accumulate("topic", topic);
                System.out.println(jsonToSend);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Nueva conexion llamando a la funcion del server

            Conexion con = new Conexion(this);
            con.execute("http://10.4.41.146:9999/ServerRESTAPI/forum/CreateNewForumThread", "POST", jsonToSend.toString());

        }
    }




    @Override
    public void OnprocessFinish(JSONObject json) {

        try {
            if (json.getInt("code") == 200) {
                System.out.println(json.getInt("code")+ "Correcto+++++++++++++++++++++++++++\n");
                Toast.makeText(this, "Forum successfully created", Toast.LENGTH_SHORT).show();
                Bundle enviar = new Bundle();
                Intent intent = new Intent(this, MainActivity.class);
                enviar.putString("fragment","forum");
                intent.putExtras(enviar);
                startActivity(intent);
            }
            else {
                System.out.println(json.getInt("code")+ "Mal+++++++++++++++++++++++++++\n");
                AlertDialog.Builder error = new AlertDialog.Builder(NewForum.this);
                error.setMessage("There is already a forum with the same Title")
                        .setCancelable(false)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog errorE = error.create();
                errorE.setTitle("Existent forum");
                errorE.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

