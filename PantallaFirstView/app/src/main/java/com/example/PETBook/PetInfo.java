package com.example.PETBook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.EventModel;
import com.example.PETBook.Models.PetModel;
import com.example.pantallafirstview.R;

import org.json.JSONException;
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

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets_info);

        textNombre = (TextView) findViewById(R.id.NombrePet);
        textAge = (TextView) findViewById(R.id.edad);
        textSex = (TextView) findViewById(R.id.sexo);
        textType = (TextView) findViewById(R.id.type);
        textColor = (TextView) findViewById(R.id.color);
        textRace = (TextView) findViewById(R.id.raza);
        txtDescription = (TextView) findViewById(R.id.descripcion);



        /*editButton = (Button) findViewById(R.id.buttonEditEvent);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEvent();
            }
        });*/


        deleteButton = (ImageButton) findViewById(R.id.imageButtonDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder error = new AlertDialog.Builder(PetInfo.this);
                error.setMessage("Esta seguro que quiere eliminar el evento?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //deletePet();
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
                errorE.setTitle("Eliminar evento");
                errorE.show();
            }
        });


        recibirDatos();
    }

    private void recibirDatos(){
        Bundle datosRecibidos = this.getIntent().getExtras();
        if(datosRecibidos != null) {
            PetModel petModel = (PetModel) datosRecibidos.getSerializable("pet");
            id = petModel.getId();
            name = petModel.getNombre();
            age = petModel.getEdad();
            sex = petModel.getSexo();
            type = petModel.getEspecie();
            color = petModel.getColor();
            race = petModel.getRaza();
            description = petModel.getDescripcion();
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
            textAge.setText(age);
            textSex.setText(sex);
            textType.setText(type);
            textColor.setText(color);
            textRace.setText(race);
            txtDescription.setText(description);;
        }
    }/*
    private void deletePet(){
        SingletonUsuario su = SingletonUsuario.getInstance();
        String localizacion = txtLoc.getText().toString();
        String titulo = txtTitle.getText().toString();
        String descripcion = txtDescription.getText().toString();
        Boolean publico = true;
        String Fecha = txtFecha.getText().toString() + "T" + txtHora.getText().toString() + ":00.000Z";
        String user = su.getEmail();

        JSONObject jsonToSend = new JSONObject();
        try {
            jsonToSend.accumulate("coordenadas", Integer.parseInt(localizacion)/10); //cambiar cuando se implemente MAPS
            jsonToSend.accumulate("descripcion", descripcion);
            jsonToSend.accumulate("fecha", Fecha); //2019-05-24T19:13:00.000Z formato fecha
            jsonToSend.accumulate("publico", publico);
            jsonToSend.accumulate("radio", 0); //No se trata el valor por Google Maps
            jsonToSend.accumulate("titulo", titulo);
            jsonToSend.accumulate("userEmail", user);
            System.out.print(jsonToSend);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Conexion con = new Conexion(this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/DeleteEvento/", "DELETE", jsonToSend.toString());
    }*/
    @Override
    public void OnprocessFinish(JSONObject output) {

    }
}
