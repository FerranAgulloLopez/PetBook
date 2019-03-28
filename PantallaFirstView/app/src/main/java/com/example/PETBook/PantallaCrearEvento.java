package com.example.PETBook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.pantallafirstview.R;

public class PantallaCrearEvento extends AppCompatActivity {

    private EditText Descripcion;
    private EditText Localizacion;
    private EditText FechaEvento;
    private EditText HoraEvento;
    private EditText NumeroAsistentes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_crear_evento);

        Descripcion = (EditText) findViewById(R.id.DescriptionText);
        Localizacion = (EditText) findViewById(R.id.LocationText);
        FechaEvento = (EditText) findViewById(R.id.EventDateText);
        HoraEvento = (EditText) findViewById(R.id.EventHourText);
        NumeroAsistentes = (EditText) findViewById(R.id.AssistentText);
    }

    public void creacionEvento(View view){
    }
}
