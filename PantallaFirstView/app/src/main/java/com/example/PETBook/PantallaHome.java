package com.example.PETBook;

import android.content.Intent;
import android.media.Image;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pantallafirstview.R;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class PantallaHome extends AppCompatActivity {

    private String username;
    private String name;
    private ImageView imatgePerfil;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_home);
        getSupportActionBar().hide();
        /*ImageView imageProfile = findViewById(R.id.imageView4);
        imatgePerfil.setImageResource(R.drawable.imatge_defecte);
        imageProfile = imatgePerfil;

        TextView usern = findViewById(R.id.textView5);
        username = PantallaLogSign.username;
        usern.setText(username);
        /*TextView nameus = findViewById(R.id.textView5);
        nameus.setText(name);*/


        TextView usuari = findViewById(R.id.username);
        SingletonUsuario su = SingletonUsuario.getInstance();
        usuari.setText(su.getEmail());
        //Conexion con = new Conexion(user, pass);


        // ESTO HACE QUE LO PERMITA TODO, basicamente todo lo ejecuta el mismo thread( el principal)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Lo suyo seria que hicieras que lo del Background funcionara, o otro metodo

        //JSONObject json = con.doInBackground();


    }
        public void myPets(View view){
        Intent intent = new Intent(this, Pets.class);
        startActivity(intent);

    }
    public void myPosts(View view){
        Intent intent = new Intent(this, MyPosts.class);
        startActivity(intent);

    }
    public void myCalendar(View view) {
        Intent intent = new Intent(this, MyCalendar.class);
        startActivity(intent);

    }
    public void myEvents(View view){
        Intent intent = new Intent(this, MyEvents.class);
        startActivity(intent);

    }
}
