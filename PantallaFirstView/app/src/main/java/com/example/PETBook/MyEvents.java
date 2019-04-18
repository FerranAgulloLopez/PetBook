package com.example.PETBook;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.PETBook.Adapters.EventAdapter;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.EventModel;
import com.example.pantallafirstview.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyEvents extends AppCompatActivity implements AsyncResult {

    private ListView lista;
    private EventAdapter eventosUser;
    private ArrayList<EventModel> model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        Conexion con = new Conexion(MyEvents.this);
        SingletonUsuario su = SingletonUsuario.getInstance();

        con.execute("http://10.4.41.146:9999/ServerRESTAPI/getEventsByCreator?email=" + su.getEmail(),"GET", null);


        FloatingActionButton fab = findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyEvents.this, NewEvent.class);
                startActivity(intent);
            }
        });


    }

    private String transformacionFechaHora(String fechaHora){
        Integer fin = 0;
        String result = fechaHora.replace("T", " ");
        result = result.replace(":00.000+0000", "");
        return result;
    }

    @Override
    public void OnprocessFinish(JSONObject json) {
        try{
            if(json.getInt("code") == 200){
                model = new ArrayList<>();
                JSONArray jsonArray = json.getJSONArray("array");
                for(int i = 0; i < jsonArray.length(); ++i){
                    JSONObject evento = jsonArray.getJSONObject(i);
                    EventModel e = new EventModel();
                    e.setTitulo(evento.getString("titulo"));
                    e.setDescripcion(evento.getString("descripcion"));
                    e.setFecha(transformacionFechaHora(evento.getString("fecha")));
                    e.setLocalizacion(evento.getInt("localizacion"));
                    e.setPublico(evento.getBoolean("publico"));
                    model.add(e);
                }
                eventosUser = new EventAdapter(MyEvents.this, model);
                lista = (ListView) findViewById(R.id.list_eventos);
                lista.setAdapter(eventosUser);
                System.out.print(json.getInt("code") + " se muestran correctamente la lista de eventos\n");
            }
            else{
                System.out.print("El sistema no logra mostrar la lista de eventos del creador\n");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
