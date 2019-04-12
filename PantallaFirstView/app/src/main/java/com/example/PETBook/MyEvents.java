package com.example.PETBook;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.PETBook.Adapters.EventAdapter;
import com.example.PETBook.Models.EventModel;
import com.example.pantallafirstview.R;

import java.util.ArrayList;

public class MyEvents extends AppCompatActivity {

    private ListView lista;
    private EventAdapter eventosUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        ArrayList<EventModel> model = new ArrayList<>();

        EventModel e = new EventModel();
        e.setLocalizacion("12334");
        e.setFecha("17/4/2019 20:00");

        model.add(e);

        e = new EventModel();
        e.setLocalizacion("12320");
        e.setFecha("18/4/2019 20:00");
        model.add(e);

        e = new EventModel();
        e.setLocalizacion("14020");
        e.setFecha("24/4/2019 10:00");
        model.add(e);

        eventosUser = new EventAdapter(this, model);

        lista = (ListView) findViewById(R.id.list_eventos);
        lista.setAdapter(eventosUser);

        FloatingActionButton fab = findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyEvents.this, NewEvent.class);
                startActivity(intent);
            }
        });
    }
}
