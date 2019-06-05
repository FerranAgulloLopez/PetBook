package com.example.PETBook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.EventModel;
import com.example.pantallafirstview.R;

import org.json.JSONObject;

public class EventInfo extends AppCompatActivity implements AsyncResult {

    private EventModel event;
    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtLoc;
    private TextView txtFecha;
    private TextView txtHora;
    private TextView txtMiembros;
    private TextView txtCreador;
    private ImageButton editButton;
    private ImageButton deleteButton;
    private ImageButton closeEvent;
    private Boolean creator;
    private Boolean participa;
    private Boolean participaba;
    private String preWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        txtTitle = (TextView) findViewById(R.id.textTitulo);
        txtDescription = (TextView) findViewById(R.id.textDesc);
        txtLoc = (TextView) findViewById(R.id.textLoc);
        txtFecha = (TextView) findViewById(R.id.textFecha);
        txtHora = (TextView) findViewById(R.id.textHora);
        txtMiembros = (TextView) findViewById(R.id.textNumPart);
        txtCreador = (TextView) findViewById(R.id.textCreador);

        recibirDatos();
        participaba = event.getMiembros().contains(SingletonUsuario.getInstance().getEmail());
        editButton = (ImageButton) findViewById(R.id.EditPetButton);
        deleteButton = (ImageButton) findViewById(R.id.imageButtonDelete);

        if(event.getCreador().equals(SingletonUsuario.getInstance().getEmail())) {
            creator = true;
            participa = true;
            editButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#840705")));
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EventInfo.this, EditEvent.class);
                    intent.putExtra("event", event);
                    intent.putExtra("preWindow", getIntent().getExtras().getString("preWindow"));
                    startActivity(intent);
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder error = new AlertDialog.Builder(EventInfo.this);
                    error.setMessage("Are you sure you want to delete the event?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
                    errorE.setTitle("Delete event");
                    errorE.show();
                }
            });
        }
        else {
            creator = false;
            editButton.setImageDrawable(getResources().getDrawable(R.drawable.participo_evento));
            if (event.getMiembros().contains(SingletonUsuario.getInstance().getEmail())) {
                participa = true;
                botonNoParticipar();
            }
            else {
                participa = false;
                botonParticipar();
            }
            deleteButton.setEnabled(false);
            deleteButton.setVisibility(View.INVISIBLE);
        }

        closeEvent = (ImageButton) findViewById(R.id.leaveEventInfo);
        closeEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preWindow.equals("maps")){
                    Intent intent = new Intent(EventInfo.this, MainActivity.class);
                    intent.putExtra("fragment", "map");
                    startActivity(intent);
                    finish();
                }
                else if(preWindow.equals("calendar")){
                    finish();
                }
                else {
                    Intent intent = new Intent(EventInfo.this,MainActivity.class);
                    intent.putExtra("fragment", "events");
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    public void botonParticipar(){
        editButton.setImageTintList(ColorStateList.valueOf(Color.RED));
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Conexion con = new Conexion(EventInfo.this);
                    con.execute("http://10.4.41.146:9999/ServerRESTAPI/events/AddEventParticipant?eventId=" + event.getId() + "&participantMail=" + SingletonUsuario.getInstance().getEmail(), "POST", null);
                    botonNoParticipar();
                    if(participaba){
                        txtMiembros.setText(String.format("%d users joined the event",event.getMiembros().size()));
                    }
                    else txtMiembros.setText(String.format("%d users joined the event",event.getMiembros().size() + 1));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void botonNoParticipar(){
        editButton.setImageTintList(ColorStateList.valueOf(Color.GREEN));
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Conexion con = new Conexion(EventInfo.this);
                    con.execute("http://10.4.41.146:9999/ServerRESTAPI/events/DeleteEventParticipant?eventId=" + event.getId() + "&participantMail=" + SingletonUsuario.getInstance().getEmail(), "DELETE", null);
                    botonParticipar();
                    if(participaba){
                        txtMiembros.setText(String.format("%d users joined the event",event.getMiembros().size() - 1));
                    }
                    else txtMiembros.setText(String.format("%d users joined the event",event.getMiembros().size()));
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void recibirDatos(){
        Bundle datosRecibidos = this.getIntent().getExtras();
        if(datosRecibidos != null) {
            event = (EventModel) datosRecibidos.getSerializable("event");
            System.out.print("La ventana recibe los datos ya que el bundle no es vacio\n");
            txtTitle.setText(event.getTitulo());
            txtDescription.setText(event.getDescripcion());
            txtLoc.setText(event.getDireccion());
            String[] Fecha = event.getFecha().split(" ");
            txtFecha.setText(Fecha[0]);
            txtHora.setText(Fecha[1]);
            txtMiembros.setText(String.format("%d users joined the event",event.getMiembros().size()));
            txtCreador.setText("Created by: " + event.getCreador());
            preWindow = datosRecibidos.getString("preWindow");
        }
    }

    private void deleteEvent(){
        Conexion con = new Conexion(this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/events/DeleteEvent?eventId="+event.getId(), "DELETE", null);
    }

    @Override
    public void OnprocessFinish(JSONObject json) {

        try {
            if (creator) {
                if (json.getInt("code") == 200) {
                    System.out.print(json.getInt("code") + "Correcto+++++++++++++++++++++++++++\n");
                    Toast.makeText(this, "Event deleted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("fragment", "events");
                    startActivity(intent);
                } else {
                    System.out.print(json.getInt("code") + "Mal+++++++++++++++++++++++++++\n");
                    Toast.makeText(this, "Error during delete event", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                if(json.getInt("code") == 200) {
                    participa = !participa;
                    System.out.print(json.getInt("code") + "Correcto+++++++++++++++++++++++++++\n");
                    if (participa) {
                        Toast.makeText(this, "You joined the event", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, "You have left the event", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
