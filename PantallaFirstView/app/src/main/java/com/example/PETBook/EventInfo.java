package com.example.PETBook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.PETBook.Models.EventModel;
import com.example.pantallafirstview.R;

public class EventInfo extends AppCompatActivity {

    private EventModel event;
    private EditText editTitle;
    private EditText editDescription;
    private EditText editLoc;
    private EditText editFecha;
    private EditText editHora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        event = (EventModel) getIntent().getSerializableExtra("event");

        editTitle = findViewById(R.id.editTitulo);
        editTitle.setText(event.getTitulo());

        editDescription = findViewById(R.id.editDescripcion);
        editDescription.setText(event.getDescripcion());

        editLoc = findViewById(R.id.editLocalizacion);
        editLoc.setText(event.getLocalizacion());

        editFecha = findViewById(R.id.editFecha);
        editFecha.setText(event.getFecha());
        editHora = findViewById(R.id.editHora);
    }
}
