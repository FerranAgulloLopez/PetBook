package com.example.PETBook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Fragments.MyPetsFragment;
import com.example.PETBook.Models.Image;
import com.example.pantallafirstview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class NewPet extends AppCompatActivity implements AsyncResult {


    private EditText nombremascota;
    private EditText edad;
    private Spinner sexo;
    private EditText color;
    private EditText race;
    private Spinner especie;
    private EditText description;
    private TextView textNM;
    private ImageView imageProfile;
    private ImageView addPet;
    private ImageView cancelPet;


    String nombrePet;
    String edadPet;
    String sexoPet;
    String colorPet;
    String racePet;
    String especiePet;
    String descPet;
    String fotoPet;


    private String usuario;

    private Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pet);
        SingletonUsuario su = SingletonUsuario.getInstance();
        usuario = su.getEmail();

        nombremascota = findViewById(R.id.Name);
        edad = findViewById(R.id.Age);
        sexo = findViewById(R.id.Sex);
        color = findViewById(R.id.Color);
        race = findViewById(R.id.Race);
        especie = findViewById(R.id.Type);
        description = findViewById(R.id.Observations);
        textNM = findViewById(R.id.textNM);
        imageProfile = findViewById(R.id.Imatge);



        findViewById(R.id.Imatge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });

        addPet = findViewById(R.id.buttonAddPet);
        cancelPet = findViewById(R.id.cancelAddPet);

        addPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addNewPet();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        cancelPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Bundle enviar = new Bundle();
                    Intent intent = new Intent(NewPet.this, MainActivity.class);
                    intent.putExtra("fragment","pets");
                    intent.putExtra("petsUser", SingletonUsuario.getInstance().getEmail());
                    startActivity(intent);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

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


    public void addNewPet()  {

        nombrePet = nombremascota.getText().toString().trim();
        edadPet = edad.getText().toString();
        sexoPet = sexo.getSelectedItem().toString();
        colorPet = color.getText().toString();
        racePet = race.getText().toString();
        especiePet = especie.getSelectedItem().toString();
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
                jsonToSend.accumulate("foto", fotoPet);

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

                Intent intent = new Intent(NewPet.this, MainActivity.class);
                intent.putExtra("fragment","pets");
                intent.putExtra("petsUser", SingletonUsuario.getInstance().getEmail());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri returnUri = data.getData();
                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), returnUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageProfile.setImageBitmap(bitmapImage);

                Image imageConversor = Image.getInstance();
                fotoPet = imageConversor.BitmapToString(bitmapImage);

            }
        }

    }

}
