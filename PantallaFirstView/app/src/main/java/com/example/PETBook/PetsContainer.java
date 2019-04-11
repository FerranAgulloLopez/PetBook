package com.example.PETBook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.PETBook.Adapters.PetAdapters;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.PetModel;
import com.example.pantallafirstview.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PetsContainer extends AppCompatActivity implements AsyncResult {

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private PetAdapters mSectionsPagerAdapter;
    private List<PetModel> pets;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    //@SuppressLint("WrongThread")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        pets = new ArrayList<>();
       // pets.add(new PetModel("idPet", "nom"));
        //pets.add(new PetModel("idPet2", "nom2"));
        //TODO: pets comes from DB

        SingletonUsuario su = SingletonUsuario.getInstance();
        String us = su.getEmail();


        System.out.println("petscContainer1111111111111");


        Conexion con = new Conexion(this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/getALLMascotasByUser/" + us,"GET", null);




        System.out.println("petscContainer222222222222222");



    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pets, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Intent intent = new Intent(this, DeletePet.class);
            //startActivity(intent);
            /*AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            // Add the buttons
            builder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });

            // Create the AlertDialog
            AlertDialog dialog = builder.create();*/
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnprocessFinish(JSONObject json) {
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) json.get("array");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //System.out.println("Aixo es el json: " + jsonArray);
        //System.out.println("Aqui llego 1");

        for (int i = 0; i < jsonArray.length(); i++)
        {
            try {
                JSONObject jsonObjectHijo = jsonArray.getJSONObject(i);
                String id = jsonObjectHijo.getString("id");
                String nombre = jsonObjectHijo.getString("nombre");
                String especie = jsonObjectHijo.getString("especie");
                String raza = jsonObjectHijo.getString("raza");
                String sexo = jsonObjectHijo.getString("sexo");
                String descripcion = jsonObjectHijo.getString("descripcion");
                pets.add(new PetModel(id, nombre, especie, raza, sexo, descripcion));
            } catch (JSONException e) {
                Log.e("Parser JSON", e.toString());
            }
        }


        mSectionsPagerAdapter = new PetAdapters(getSupportFragmentManager(), pets);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Intent intent = new Intent(PetsContainer.this, NewPet.class);
                startActivity(intent);

            }
        });
    }
}
