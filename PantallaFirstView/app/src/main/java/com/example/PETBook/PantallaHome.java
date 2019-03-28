package com.example.PETBook;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pantallafirstview.R;

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


    }
        public void myPets(View view){
        Intent intent = new Intent(this, MyPets.class);
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
