package com.example.PETBook;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Fragments.MyEventsFragment;
import com.example.pantallafirstview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class NewEvent extends AppCompatActivity implements AsyncResult {

    private TextInputLayout Localizacion;
    private TextInputLayout Fecha;
    private TextInputLayout Hora;
    private TextInputLayout Titulo;
    private EditText inputFecha;
    private EditText inputHora;
    private EditText inputDescripcion;

    private Button addEventButton;
    private RadioButton publicButton;

    Calendar calendario = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        Localizacion = (TextInputLayout) findViewById(R.id.Localizacion);
        Fecha = (TextInputLayout) findViewById(R.id.Fecha);
        Hora = (TextInputLayout) findViewById(R.id.Hora);
        Titulo = (TextInputLayout) findViewById(R.id.Titulo);


        addEventButton = (Button) findViewById(R.id.addEventButton);
        publicButton = (RadioButton) findViewById(R.id.PublicRadioButton);

        inputDescripcion = (EditText) findViewById(R.id.editDescripcion);

        inputFecha = (EditText) findViewById(R.id.editFecha);
        inputFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(NewEvent.this, date, calendario.get(Calendar.YEAR),
                        calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        inputHora = (EditText) findViewById(R.id.editHora);
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
                1+calendario.get(Calendar.MONTH),
                calendario.get(Calendar.YEAR)));
    }

    private void actualizarHora(){
        String formatoHora = "%02d:%02d";
        inputHora.setText(String.format(formatoHora,
                calendario.get(Calendar.HOUR_OF_DAY),
                calendario.get(Calendar.MINUTE)));
    }

    private boolean validateLocation(String loc){
        if(loc.isEmpty()) {
            Localizacion.setError("Campo obligatorio");
            return false;
        }
        else {
            Localizacion.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateFecha(String date){
        if(date.isEmpty()) {
            Fecha.setError("Campo obligatorio");
            return false;
        }
        else {
            Fecha.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateHora(String hora){
        if(hora.isEmpty()) {
            Hora.setError("Campo obligatorio");
            return false;
        }
        else {
            Hora.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateTitulo(String titulo){
        if(titulo.isEmpty()){
            Titulo.setError("Campo obligatorio");
            return false;
        }
        else{
            Titulo.setErrorEnabled(false);
            return true;
        }
    }

    private String transformacionFechaHora(){
        String fecha = String.format("%04d-%02d-%02d",calendario.get(Calendar.YEAR),
                (calendario.get(Calendar.MONTH)+1),calendario.get(Calendar.DAY_OF_MONTH));
        String hora = inputHora.getText().toString();
        return fecha + "T" + hora + ":00.000Z";
    }

    private void createEvent(){
        SingletonUsuario su = SingletonUsuario.getInstance();

        String localizacion = Localizacion.getEditText().getText().toString();
        String titulo = Titulo.getEditText().getText().toString();
        String descripcion = inputDescripcion.getText().toString();
        Integer radio = 0;
        String user = su.getEmail();
        boolean pubOpriv = publicButton.isChecked();


        boolean isValidTitulo = validateTitulo(titulo);
        boolean isValidLoc = validateLocation(localizacion);
        boolean isValidFecha = validateFecha(inputFecha.getText().toString());
        boolean isValidHora = validateHora(inputHora.getText().toString());

        if(isValidTitulo && isValidLoc && isValidFecha && isValidHora) {
            String fechaHora = transformacionFechaHora();
            JSONObject jsonToSend = new JSONObject();
            try {
                jsonToSend.accumulate("coordenadas", Integer.parseInt(localizacion));
                jsonToSend.accumulate("descripcion", descripcion);
                jsonToSend.accumulate("fecha", fechaHora); //2019-05-24T19:13:00.000Z formato fecha
                jsonToSend.accumulate("publico", pubOpriv);
                jsonToSend.accumulate("radio", 0); //No se trata el valor por Google Maps
                jsonToSend.accumulate("titulo", titulo);
                jsonToSend.accumulate("userEmail", user);
                System.out.print(jsonToSend);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /* Nueva conexion llamando a la funcion del server */

            Conexion con = new Conexion(this);
            con.execute("http://10.4.41.146:9999/ServerRESTAPI/CreateEvent/", "POST", jsonToSend.toString());


        }



    }

    @Override
    public void OnprocessFinish(JSONObject json) {

        try {
            if (json.getInt("code") == 200) {
                System.out.print(json.getInt("code")+ "Correcto+++++++++++++++++++++++++++\n");
                Toast.makeText(this, "Creación de evento correcta", Toast.LENGTH_SHORT).show();
                Bundle enviar = new Bundle();
                Intent intent = new Intent(this, MainActivity.class);
                enviar.putString("fragment","events");
                intent.putExtras(enviar);
                startActivity(intent);
            } else {
                System.out.print(json.getInt("code")+ "Mal+++++++++++++++++++++++++++\n");
                AlertDialog.Builder error = new AlertDialog.Builder(NewEvent.this);
                error.setMessage("Evento existente con los mismos datos")
                        .setCancelable(false)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog errorE = error.create();
                errorE.setTitle("Evento existente");
                errorE.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
