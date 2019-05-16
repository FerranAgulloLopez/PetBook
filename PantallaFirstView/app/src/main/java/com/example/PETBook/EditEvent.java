package com.example.PETBook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.EventModel;
import com.example.pantallafirstview.R;

import org.json.JSONObject;

import java.util.Calendar;


public class EditEvent extends AppCompatActivity implements AsyncResult {

    private EventModel event;
    private EditText titulo;
    private EditText descripcion;
    private EditText fecha;
    private EditText hora;
    private EditText loc;
    private ImageButton acceptEdit;
    private ImageButton cancelEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        Bundle eventEdit = getIntent().getExtras();

        titulo = (EditText) findViewById(R.id.editNamePet);
        descripcion = (EditText) findViewById(R.id.editDescriptionPet);


        acceptEdit = (ImageButton) findViewById(R.id.imageButtonAcceptEdit);
        acceptEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder edit = new AlertDialog.Builder(EditEvent.this);
                edit.setMessage("Si confirma los datos del evento se reescribirán.")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editEvent();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog errorE = edit.create();
                errorE.setTitle("Modificar evento");
                errorE.show();
            }
        });

        cancelEdit = (ImageButton) findViewById(R.id.imageButtonCancelEdit);
        cancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder noedit = new AlertDialog.Builder(EditEvent.this);
                noedit.setMessage("Desea cancelar todos los cambios?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(EditEvent.this,EventInfo.class);
                                intent.putExtra("event",event);
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
                errorE.setTitle("Cancelar modificar evento");
                errorE.show();
            }
        });

        recibirDatos();
    }

    private void recibirDatos(){
        Bundle datosRecibidos = this.getIntent().getExtras();
        if(datosRecibidos != null) {
            event = (EventModel) datosRecibidos.getSerializable("event");
            System.out.print("La ventana recibe los datos ya que el bundle no es vacio\n");
            titulo.setText(event.getTitulo());
            descripcion.setText(event.getDescripcion());
            loc.setText(String.valueOf(event.getDireccion()));
            String[] Fecha = event.getFecha().split(" ");
            fecha.setText(Fecha[0]);
            hora.setText(Fecha[1]);
        }
    }

    private boolean validateTitulo(String titulo){
        if(titulo.isEmpty()){
            //Titulo.setError("Campo obligatorio");
            return false;
        }
        else{
            //Titulo.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateLocation(String loc){
        if(loc.isEmpty()) {
            //Localizacion.setError("Campo obligatorio");
            return false;
        }
        else {
            //Localizacion.setErrorEnabled(false);
            return true;
        }
    }

    private void editEvent(){
        SingletonUsuario su = SingletonUsuario.getInstance();
        String title = titulo.getText().toString();
        String description = descripcion.getText().toString();
        if(validateTitulo(title)){
            JSONObject jsonToSend = new JSONObject();
            try{
                jsonToSend.accumulate("description", description);
                jsonToSend.accumulate("isPublic", event.getPublic());
                jsonToSend.accumulate("public",event.getPublic());
                jsonToSend.accumulate("title", title);
                System.out.print(jsonToSend);
            } catch (Exception e){
                e.printStackTrace();
            }

        Conexion con = new Conexion(EditEvent.this);
            con.execute("http://10.4.41.146:9999/ServerRESTAPI/events/UpdateEvent?eventId="+event.getId(), "PUT", jsonToSend.toString());
        }

    }


    @Override
    public void OnprocessFinish(JSONObject json) {

        try{
            if (json.getInt("code") == 200) {
                System.out.print(json.getInt("code")+ "Correcto+++++++++++++++++++++++++++\n");
                Toast.makeText(this, "Evento modificado correctamente", Toast.LENGTH_SHORT).show();
                event.setTitulo(titulo.getText().toString());
                event.setDescripcion(descripcion.getText().toString());
                Intent intent = new Intent(EditEvent.this, EventInfo.class);
                intent.putExtra("event",event);
                startActivity(intent);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
