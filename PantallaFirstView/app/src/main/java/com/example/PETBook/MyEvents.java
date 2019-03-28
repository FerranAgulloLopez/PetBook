package com.example.PETBook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.pantallafirstview.R;

public class MyEvents extends AppCompatActivity {

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void crearEvento(View view){
        Intent intent = new Intent(this,PantallaCrearEvento.class);
        startActivity(intent);
    }
}
