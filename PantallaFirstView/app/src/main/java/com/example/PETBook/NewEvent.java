package com.example.PETBook;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Fragments.MyEventsFragment;
import com.example.PETBook.Utilidades.Alarm;
import com.example.pantallafirstview.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class NewEvent extends AppCompatActivity implements AsyncResult {

    private AutoCompleteTextView editLocation;
    private TextInputLayout Fecha;
    private TextInputLayout Hora;
    private TextInputLayout Titulo;
    private EditText inputFecha;
    private EditText inputHora;
    private EditText inputDescripcion;
    private String[] addressNames = new String[0];
    private Pair<String,String>[] positionsAddress = new Pair[0];
    int select_location;
    private Boolean isAddress = false;

    private Button addEventButton;
    private RadioButton publicButton;

    Calendar calendario = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        Fecha = (TextInputLayout) findViewById(R.id.Fecha);
        Hora = (TextInputLayout) findViewById(R.id.Hora);
        Titulo = (TextInputLayout) findViewById(R.id.Titulo);

        editLocation = (AutoCompleteTextView) findViewById(R.id.edLoc);
        editLocation.setThreshold(1);
        editLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String a = s.toString().replace(" ", "+");
                isAddress = true;
                llamadaServidor(a);
                setAdapter();
            }
        });
        editLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select_location = position;
            }
        });

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

    public void llamadaServidor(String a){
        ConexionNominatim conNom = new ConexionNominatim(this);
        conNom.execute("https://nominatim.openstreetmap.org/?addressdetails=1&q=" + a + "&format=json&limit=5");
    }

    public void setAdapter(){
        editLocation.setAdapter(new ArrayAdapter<>(this, android.R.layout.select_dialog_item,addressNames));
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
        inputFecha.setText(String.format(formatoFecha,calendario.get(Calendar.YEAR),
                1+calendario.get(Calendar.MONTH),calendario.get(Calendar.DAY_OF_MONTH)));
    }

    private void actualizarHora(){
        String formatoHora = "%02d:%02d";
        inputHora.setText(String.format(formatoHora,
                calendario.get(Calendar.HOUR_OF_DAY),
                calendario.get(Calendar.MINUTE)));
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
        String address = addressNames[select_location];
        Double lat = Double.parseDouble(positionsAddress[select_location].first);
        Double lng = Double.parseDouble(positionsAddress[select_location].second);
        String titulo = Titulo.getEditText().getText().toString();
        String descripcion = inputDescripcion.getText().toString();
        String user = su.getEmail();
        boolean pubOpriv = publicButton.isChecked();


        boolean isValidTitulo = validateTitulo(titulo);
        boolean isValidFecha = validateFecha(inputFecha.getText().toString());
        boolean isValidHora = validateHora(inputHora.getText().toString());

        if(isValidTitulo && isValidFecha && isValidHora) {
            String fechaHora = transformacionFechaHora();
            JSONObject jsonToSend = new JSONObject();
            JSONObject loc = new JSONObject();
            try {
                jsonToSend.accumulate("creatorMail", user);
                jsonToSend.accumulate("date", fechaHora);//2019-05-24T19:13:00.000Z formato fecha
                jsonToSend.accumulate("description", descripcion);
                jsonToSend.accumulate("isPublic", pubOpriv);
                loc.accumulate("address",address);
                loc.accumulate("latitude",lat);
                loc.accumulate("longitude",lng);
                jsonToSend.accumulate("localization", loc);
                jsonToSend.accumulate("public", pubOpriv);
                jsonToSend.accumulate("title", titulo);
                System.out.print(jsonToSend);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /* Nueva conexion llamando a la funcion del server */

            Conexion con = new Conexion(this);
            con.execute("http://10.4.41.146:9999/ServerRESTAPI/events/CreateEvent/", "POST", jsonToSend.toString());
        }
    }

    @Override
    public void OnprocessFinish(JSONObject json) {
        if (isAddress) {
            try {
                JSONArray jsonArray = json.getJSONArray("array");
                addressNames = new String[jsonArray.length()];
                positionsAddress = new Pair[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject address = jsonArray.getJSONObject(i);
                    String lat = address.getString("lat");
                    String lon = address.getString("lon");
                    JSONObject direccion = address.getJSONObject("address");
                    String completeAddress = "";
                    if (direccion.has("road"))
                        completeAddress += direccion.getString("road") + ", ";
                    if (direccion.has("town"))
                        completeAddress += direccion.getString("town") + ", ";
                    if (direccion.has("village"))
                        completeAddress += direccion.getString("village") + ", ";
                    if (direccion.has("city"))
                        completeAddress += direccion.getString("city") + ", ";
                    if (direccion.has("state"))
                        completeAddress += direccion.getString("state") + ", ";
                    if (direccion.has("country")) completeAddress += direccion.getString("country");
                    addressNames[i] = completeAddress;
                    positionsAddress[i] = new Pair<>(lat, lon);
                }
                isAddress = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                if (json.getInt("code") == 200) {
                    System.out.print(json.getInt("code") + "Correcto+++++++++++++++++++++++++++\n");
                    Toast.makeText(this, "CreaciÃ³n de evento correcta", Toast.LENGTH_SHORT).show();
                    //scheduleNotification();
                    Bundle enviar = new Bundle();
                    Intent intent = new Intent(this, MainActivity.class);
                    enviar.putString("fragment", "events");
                    intent.putExtras(enviar);
                    startActivity(intent);
                } else {
                    System.out.print(json.getInt("code") + "Mal+++++++++++++++++++++++++++\n");
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
}
