package com.example.PETBook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.pantallafirstview.R;

public class PantallaLogSign extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_log_sign);
    }
    public void logIn(View view){
        Intent intent = new Intent(this, PantallaHome.class);
        startActivity(intent);

    }
    public void signUp(View view){
        Intent intent = new Intent(this, PantallaSignUp.class);
        startActivity(intent);

    }
}
