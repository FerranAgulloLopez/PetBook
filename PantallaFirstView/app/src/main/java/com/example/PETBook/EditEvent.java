package com.example.PETBook;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
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

    Calendar calendario = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        Bundle eventEdit = getIntent().getExtras();

        titulo = (EditText) findViewById(R.id.editNamePet);
        descripcion = (EditText) findViewById(R.id.editDescriptionPet);

        fecha = (EditText) findViewById(R.id.editRacePet);
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditEvent.this, date, calendario.get(Calendar.YEAR),
                        calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        hora = (EditText) findViewById(R.id.editEspeciePet);
        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(EditEvent.this, time, calendario.get(Calendar.HOUR_OF_DAY),
                        calendario.get(Calendar.MINUTE),true).show();
            }
        });

        loc = (EditText) findViewById(R.id.editEdadPet);

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

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendario.set(Calendar.YEAR, year);
            calendario.set(Calendar.MONTH, monthOfYear);
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarFecha();
        }

    };

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendario.set(Calendar.HOUR_OF_DAY,hourOfDay);
            calendario.set(Calendar.MINUTE,minute);
            actualizarHora();
        }
    };

    private void actualizarFecha(){
        String formatoFecha = "%04d-%02d-%02d";
        fecha.setText(String.format(formatoFecha,calendario.get(Calendar.YEAR),
                1+calendario.get(Calendar.MONTH),calendario.get(Calendar.DAY_OF_MONTH)));
    }

    private void actualizarHora(){
        String formatoHora = "%02d:%02d";
        hora.setText(String.format(formatoHora,
                calendario.get(Calendar.HOUR_OF_DAY),
                calendario.get(Calendar.MINUTE)));
    }

    /*private String transformacionFechaHora(){
        String fecha = String.format("%04d-%02d-%02d",calendario.get(Calendar.YEAR),
                (calendario.get(Calendar.MONTH)+1),calendario.get(Calendar.DAY_OF_MONTH));
        String hora = this.hora.getText().toString();
        return fecha + "T" + hora + ":00.000Z";
    }*/

    private void recibirDatos(){
        Bundle datosRecibidos = this.getIntent().getExtras();
        if(datosRecibidos != null) {
            event = (EventModel) datosRecibidos.getSerializable("event");
            System.out.print("La ventana recibe los datos ya que el bundle no es vacio\n");
            titulo.setText(event.getTitulo());
            descripcion.setText(event.getDescripcion());
            loc.setText(String.valueOf(event.getLocalizacion()));
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
        String localizacion = loc.getText().toString();
        String fechaHora = fecha.getText().toString() + "T" + hora.getText().toString() + ":00.000Z";
        if(validateTitulo(title) && validateLocation(localizacion)){
            JSONObject jsonToSend = new JSONObject();
            try{
                jsonToSend.accumulate("coordenadas", Integer.parseInt(localizacion)/10);
                jsonToSend.accumulate("descripcion", descripcion.getText().toString());
                jsonToSend.accumulate("fecha", fechaHora); //2019-05-24T19:13:00.000Z formato fecha
                jsonToSend.accumulate("publico", event.getPublic());
                jsonToSend.accumulate("radio", 0); //No se trata el valor por Google Maps
                jsonToSend.accumulate("titulo", title);
                jsonToSend.accumulate("userEmail", su.getEmail());
                System.out.print(jsonToSend);
            } catch (Exception e){
                e.printStackTrace();
            }

        Conexion con = new Conexion(EditEvent.this);
            con.execute("http://10.4.41.146:9999/ServerRESTAPI/UpdateEvent/", "PUT", jsonToSend.toString());
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
                event.setFecha(fecha.getText().toString() + " " + hora.getText().toString());
                event.setLocalizacion(Integer.parseInt(loc.getText().toString()));
                Intent intent = new Intent(EditEvent.this, EventInfo.class);
                intent.putExtra("event",event);
                startActivity(intent);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
