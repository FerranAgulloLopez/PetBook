package com.example.PETBook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.PETBook.Models.EventModel;
import com.example.pantallafirstview.R;

import java.util.Calendar;


public class EditEvent extends AppCompatActivity {

    private EventModel event;
    private EditText titulo;
    private EditText descripcion;
    private EditText fecha;
    private EditText hora;
    private EditText loc;

    Calendar calendario = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        Bundle eventEdit = getIntent().getExtras();

        titulo = (EditText) findViewById(R.id.editTextTitle);
        descripcion = (EditText) findViewById(R.id.editTextInfo);

        fecha = (EditText) findViewById(R.id.editTextFecha);
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditEvent.this, date, calendario.get(Calendar.YEAR),
                        calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        hora = (EditText) findViewById(R.id.editTextHora);
        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(EditEvent.this, time, calendario.get(Calendar.HOUR_OF_DAY),
                        calendario.get(Calendar.MINUTE),true).show();
            }
        });

        loc = (EditText) findViewById(R.id.editTextLoc);

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
        String formatoFecha = "%02d/%02d/%02d";
        fecha.setText(String.format(formatoFecha,calendario.get(Calendar.DAY_OF_MONTH),
                1+calendario.get(Calendar.MONTH),
                calendario.get(Calendar.YEAR)));
    }

    private void actualizarHora(){
        String formatoHora = "%02d:%02d";
        hora.setText(String.format(formatoHora,
                calendario.get(Calendar.HOUR_OF_DAY),
                calendario.get(Calendar.MINUTE)));
    }

    private String transformacionFechaHora(){
        String fecha = String.format("%04d-%02d-%02d",calendario.get(Calendar.YEAR),
                (calendario.get(Calendar.MONTH)+1),calendario.get(Calendar.DAY_OF_MONTH));
        String hora = this.hora.getText().toString();
        return fecha + "T" + hora + ":00.000Z";
    }

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


}
