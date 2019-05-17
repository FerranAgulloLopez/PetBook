package com.example.PETBook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.EventModel;
import com.example.pantallafirstview.R;

import org.json.JSONException;
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


        editButton = (ImageButton) findViewById(R.id.EditPetButton);
        if(this.getIntent().getStringExtra("eventType").equals("Creator")) {
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EventInfo.this, EditEvent.class);
                    intent.putExtra("event", event);
                    startActivity(intent);
                }
            });
        }
        else if (this.getIntent().getStringExtra("eventType").equals("Participant")){
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EventInfo.this, EditEvent.class);
                    intent.putExtra("event", event);
                    startActivity(intent);
                }
            });
        }
        else{
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Conexion con = new Conexion(EventInfo.this);
                }
            });
        }


        deleteButton = (ImageButton) findViewById(R.id.imageButtonDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder error = new AlertDialog.Builder(EventInfo.this);
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


        recibirDatos();
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
            txtMiembros.setText(String.format("%d usuario/s participarán en el evento",event.getMiembros().size()));
            txtCreador.setText("Creado por: " + event.getCreador());
        }
    }

    private void deleteEvent(){
        Conexion con = new Conexion(this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/events/DeleteEvent/eventId="+event.getId(), "DELETE", null);
    }

    @Override
    public void OnprocessFinish(JSONObject json) {

        try {
            if (json.getInt("code") == 200) {
                System.out.print(json.getInt("code")+ "Correcto+++++++++++++++++++++++++++\n");
                Toast.makeText(this, "Evento correctamente eliminado", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("fragment", "events");
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
