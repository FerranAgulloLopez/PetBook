package com.example.PETBook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pantallafirstview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewEvent extends AppCompatActivity {

    private EditText inputLocalizacion;
    private EditText inputFecha;
    private EditText inputHora;

    private Button addEventButton;

    Calendar calendario = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        inputLocalizacion = (EditText) findViewById(R.id.editLocalizacion);
        inputFecha = (EditText) findViewById(R.id.editFecha);
        inputHora = (EditText) findViewById(R.id.editHora);

        addEventButton = (Button) findViewById(R.id.addEventButton);

        inputFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(NewEvent.this, date, calendario.get(Calendar.YEAR),
                        calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        inputHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(NewEvent.this, time, calendario.get(Calendar.HOUR_OF_DAY),
                        calendario.get(Calendar.MINUTE),true).show();
            }
        });

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEvent();
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
        inputFecha.setText(String.format(formatoFecha,calendario.get(Calendar.DAY_OF_MONTH),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.YEAR)));
    }

    private void actualizarHora(){
        String formatoHora = "%02d:%02d";
        inputHora.setText(String.format(formatoHora,
                calendario.get(Calendar.HOUR_OF_DAY),
                calendario.get(Calendar.MINUTE)));
    }

    private void createEvent(){
        String localizacion = inputLocalizacion.getText().toString();
        String fechaE = inputFecha.getText().toString();
        String horaE = inputHora.getText().toString();
        Integer any = calendario.get(Calendar.YEAR);
        Integer mes = calendario.get(Calendar.MONTH);
        Integer dia = calendario.get(Calendar.DAY_OF_MONTH);
        String hora = inputHora.getText().toString();


        JSONObject jsonToSend = new JSONObject();
        try {
            jsonToSend.accumulate("any", any.toString());
            jsonToSend.accumulate("coordenadas", localizacion);
            jsonToSend.accumulate("dia", dia.toString());
            jsonToSend.accumulate("hora", hora);
            jsonToSend.accumulate("mes", mes.toString());
            jsonToSend.accumulate("radio", "0");
            jsonToSend.accumulate("userEmail", SingletonUsuario.getInstance().getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* Nueva conexion llamando a la funcion del server */

        Conexion con = new Conexion("http://10.4.41.146:9999/ServerRESTAPI/CreaEvento",
                "POST", jsonToSend);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        JSONObject json = con.doInBackground();





        Toast.makeText(this, "Creación de evento correcta", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MyEvents.class);
        startActivity(intent);

    }

}
