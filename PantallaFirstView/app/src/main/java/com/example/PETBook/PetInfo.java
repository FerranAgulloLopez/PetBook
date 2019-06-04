package com.example.PETBook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.Image;
import com.example.PETBook.Models.PetModel;
import com.example.pantallafirstview.R;

import org.json.JSONObject;

public class PetInfo extends AppCompatActivity implements AsyncResult {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private String id;
    private String name;
    private String age;
    private String sex;
    private String type;
    private String color;
    private String race;
    private String description;
    private TextView textNombre;
    private TextView textAge;
    private TextView textSex;
    private TextView textType;
    private TextView textColor;
    private TextView textRace;
    private TextView txtDescription;
    private ImageButton editButton;
    private ImageButton deleteButton;
    private PetModel petModel;
    private ImageView fotoPet;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */ protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets_info);

        textNombre = (TextView) findViewById(R.id.nombrePetInfo);
        textAge = (TextView) findViewById(R.id.edadInfo);
        textSex = (TextView) findViewById(R.id.sexoInfo);
        textType = (TextView) findViewById(R.id.typeInfo);
        textColor = (TextView) findViewById(R.id.colorInfo);
        textRace = (TextView) findViewById(R.id.razaInfo);
        txtDescription = (TextView) findViewById(R.id.descripcionInfo);
        fotoPet = findViewById(R.id.fotoPet);



        editButton = (ImageButton) findViewById(R.id.EditPetButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PetInfo.this,EditPet.class);
                intent.putExtra("pet", petModel);
                startActivity(intent);
            }
        });


        deleteButton = (ImageButton) findViewById(R.id.DeletePetButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder error = new AlertDialog.Builder(PetInfo.this);
                error.setMessage("Are you sure you want to delete this pet?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletePet();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog errorE = error.create();
                // Titulo para el dialog
                errorE.setTitle("Delete pet");
                errorE.show();
            }
        });


        recibirDatos();

    }
    private void recibirDatos(){
        Bundle datosRecibidos = this.getIntent().getExtras();
        if(datosRecibidos != null) {
            petModel = (PetModel) datosRecibidos.getSerializable("pet");
            id = petModel.getId();
            name = petModel.getNombre();
            age = petModel.getEdad().toString();
            sex = petModel.getSexo();
            type = petModel.getEspecie();
            color = petModel.getColor();
            race = petModel.getRaza();
            description = petModel.getDescripcion();

            // Convert string to Bitmap
            Image imagenConversor = Image.getInstance();
            Bitmap profileImage = imagenConversor.StringToBitMap(petModel.getFoto());

/*
            int nh = (int) ( profileImage.getHeight() * (256.0 / profileImage.getWidth()) );
            Bitmap scaledProfileImage = Bitmap.createScaledBitmap(profileImage, 512, nh, true);
            */

            fotoPet.setImageBitmap(profileImage);



            System.out.print("La ventana recibe los datos ya que el bundle no es vacio\n");
            System.out.print(id + "\n");
            System.out.print(name + "\n");
            System.out.print(age + "\n");
            System.out.print(sex + "\n");
            System.out.print(type + "\n");
            System.out.print(color + "\n");
            System.out.print(race + "\n");
            System.out.print(description + "\n");
            textNombre.setText(name);
            System.out.print("textNombre bien");

            textAge.setText("Age: " + age);
            System.out.print("textAge bien");

            textSex.setText(sex);
            System.out.print("textSex bien");

            textType.setText(type);
            System.out.print("textType bien");

            textColor.setText(color);
            System.out.print("textColor bien");

            textRace.setText(race);
            System.out.print("textRace bien");

            txtDescription.setText(description);
            System.out.print("textDescription bien");

        }
    }
    private void deletePet(){
        SingletonUsuario su = SingletonUsuario.getInstance();
        String user = su.getEmail();
        String nombrePet = textNombre.getText().toString();

        Conexion con = new Conexion(this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/DeletePet/" + user + "?nombreMascota=" + nombrePet.trim(), "DELETE", null);
    }
    @Override
    public void OnprocessFinish(JSONObject json) {
        try {
            if (json.getInt("code") == 200) {
                System.out.print(json.getInt("code")+ "Correcto+++++++++++++++++++++++++++\n");
                Toast.makeText(this, "Pet successfully deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("fragment", "pets");
                intent.putExtra("nameProfile", SingletonUsuario.getInstance().getEmail());
                startActivity(intent);
            } else {
                System.out.print(json.getInt("code")+ "Mal+++++++++++++++++++++++++++\n");
                Toast.makeText(this, "User doesn't exist", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
