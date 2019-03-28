package com.example.PETBook;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.pantallafirstview.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PantallaCrearEvento extends AppCompatActivity {

    private EditText Descripcion;
    private EditText Localizacion;
    private EditText FechaEvento;
    private EditText HoraEvento;
    private EditText NumeroAsistentes;

    private Button buttonCrearEvento;

    Calendar calendario = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_crear_evento);

        Descripcion = (EditText) findViewById(R.id.DescriptionText);
        Localizacion = (EditText) findViewById(R.id.LocationText);
        FechaEvento = (EditText) findViewById(R.id.EventDateText);
        HoraEvento = (EditText) findViewById(R.id.EventHourText);
        NumeroAsistentes = (EditText) findViewById(R.id.AssistentText);
        buttonCrearEvento = (Button) findViewById(R.id.botonCrear);

        buttonCrearEvento.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Añadir implementación de comunicación con la base de datos
                finish();
            }
        });

        FechaEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PantallaCrearEvento.this, date, calendario
                        .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendario.set(Calendar.YEAR, year);
            calendario.set(Calendar.MONTH, monthOfYear);
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInput();
        }



    };

    private void actualizarInput() {
        String formatoDeFecha = "dd/mm/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.UK);

        FechaEvento.setText(sdf.format(calendario.getTime()));
    }

    public void creacionEvento(View view){
    }
}
