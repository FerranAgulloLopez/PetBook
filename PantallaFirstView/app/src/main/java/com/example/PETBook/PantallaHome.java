package com.example.PETBook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.pantallafirstview.R;

public class PantallaHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_home);
        getSupportActionBar().hide();
    }
        public void myPets(View view){
        Intent intent = new Intent(this, MyPets.class);
        startActivity(intent);

    }
    public void myPosts(View view){
        Intent intent = new Intent(this, MyPosts.class);
        startActivity(intent);

    }
    public void myCalendar(View view){
        Intent intent = new Intent(this, MyCalendar.class);
        startActivity(intent);

    }
    public void myEvents(View view){
        Intent intent = new Intent(this, MyEvents.class);
        startActivity(intent);

    }
}
