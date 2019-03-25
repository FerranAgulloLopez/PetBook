package com.example.PETBook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.pantallafirstview.R;

public class PantallaSignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_sign_up);
    }
    public void signUp(View view){
        Intent intent = new Intent(this, PantallaHome.class);
        startActivity(intent);

    }
}
