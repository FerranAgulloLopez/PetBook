package com.example.PETBook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Fragments.MyPetsFragment;
import com.example.pantallafirstview.R;

import org.json.JSONException;
import org.json.JSONObject;

public class NewPet extends AppCompatActivity implements AsyncResult {


    private EditText nombremascota;
    private EditText edad;
    private Spinner sexo;
    private EditText color;
    private EditText race;
    private EditText especie;
    private EditText description;
    private Button buttonAddPet;
    private TextView textNM;


    String nombrePet;
    String edadPet;
    String sexoPet;
    String colorPet;
    String racePet;
    String especiePet;
    String descPet;


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
        especie = findViewById(R.id.Type);
        description = findViewById(R.id.Observations);
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


    public void addNewPet(View view) throws JSONException {

        nombrePet = nombremascota.getText().toString();
        edadPet = edad.getText().toString();
        sexoPet = sexo.getSelectedItem().toString();
        colorPet = color.getText().toString();
        racePet = race.getText().toString();
        especiePet = especie.getText().toString();
        descPet = description.getText().toString();

        if (validateName()) {
            /* Juntar los datos en un Json para ponerlo en el body */

            JSONObject jsonToSend = new JSONObject();
            try {
                jsonToSend.accumulate("email", usuario);
                jsonToSend.accumulate("nombre", nombrePet);
                jsonToSend.accumulate("color", colorPet);
                jsonToSend.accumulate("descripcion", descPet);
                jsonToSend.accumulate("edad", edadPet);
                jsonToSend.accumulate("especie", especiePet);
                jsonToSend.accumulate("raza", racePet);
                jsonToSend.accumulate("sexo", sexoPet);

                //jsonToSend.accumulate("foto", fotoPet);



            } catch (JSONException e) {
                e.printStackTrace();
            }

            /* Nueva conexion llamando a la funcion del server */

            Conexion con = new Conexion(this);
            con.execute("http://10.4.41.146:9999/ServerRESTAPI/CreatePet/", "POST", jsonToSend.toString());



        }

    }

    @Override
    public void OnprocessFinish(JSONObject json) {

        try {
            if(json.getInt("code")==200) {
                if (sexoPet.equals("Male")) {
                    Toast.makeText(this, nombrePet + " añadido correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, nombrePet + " añadida correctamente", Toast.LENGTH_SHORT).show();
                }
                System.out.println(json.getInt("code") +"\n\n\n");

                Intent intent = new Intent(this, MyPetsFragment.class);
                intent.putExtra("fragment","pets");
                startActivity(intent);
            }
            else{
                Toast.makeText(this, nombrePet + " ya existe", Toast.LENGTH_SHORT).show();
                System.out.println(json.getInt("code") +"\n\n\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            try {
                System.out.println(json.getInt("code") +"\n\n\n");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }

    }

}
