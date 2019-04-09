package com.example.PETBook;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pantallafirstview.R;

import org.json.JSONException;
import org.json.JSONObject;

public class NewPet extends AppCompatActivity {


    private EditText nombremascota;
    private EditText edad ;
    private Spinner sexo ;
    private EditText color ;
    private EditText race;
    private EditText type;
    private EditText observations ;
    private Button buttonAddPet ;
    private TextView textNM;


    private String usuario;

    private Button buttonAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pet);
        SingletonUsuario su = SingletonUsuario.getInstance();
        usuario = su.getEmail();
        buttonAdd = findViewById(R.id.buttonAddPet);

        nombremascota = findViewById(R.id.Name);
        edad = findViewById(R.id.Age);
        sexo = findViewById(R.id.Sex);
        color = findViewById(R.id.Color);
        race = findViewById(R.id.Race);
        type = findViewById(R.id.Type);
        observations = findViewById(R.id.Observations);
        buttonAddPet = findViewById(R.id.buttonAddPet);
        textNM = findViewById(R.id.textNM);

    }


    private boolean validateName() {
        if (nombremascota.getText().toString().isEmpty()) {
            textNM.setVisibility(View.VISIBLE);
            return false;
        } else {
            textNM.setVisibility(View.INVISIBLE);
            return true;
        }
    }


    public void addNewPet(View view) {

        String nombrePet = nombremascota.getText().toString();
        String edadPet = edad.getText().toString();
        String sexoPet = sexo.getSelectedItem().toString();
        String colorPet = color.getText().toString();
        String racePet = race.getText().toString();
        String typePet = type.getText().toString();
        String obsPet = observations.getText().toString();

        if (validateName()) {
            /* Juntar los datos en un Json para ponerlo en el body */

            JSONObject jsonToSend = new JSONObject();
            try {
                jsonToSend.accumulate("email", usuario);
                jsonToSend.accumulate("nombre", nombrePet);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /* Nueva conexion llamando a la funcion del server */

            Conexion con = new Conexion("http://10.4.41.146:9999/ServerRESTAPI/CreaMascota/",
                    "POST", jsonToSend);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            JSONObject json = con.doInBackground();

            try {
                if(json.getInt("code")==200) {
                    if (sexoPet.equals("Male")) {
                        Toast.makeText(this, nombrePet + " añadido correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, nombrePet + " añadida correctamente", Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(this, PetsContainer.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, nombrePet + " ya existe", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
