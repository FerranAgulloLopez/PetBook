package com.example.PETBook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.pantallafirstview.R;

public class MyPets extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pets);
    }
    public void pet1(View view){
        Intent intent = new Intent(this, Pet1.class);
        startActivity(intent);

    }
}
