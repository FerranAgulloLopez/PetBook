package com.example.petbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LogInSignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_sign_up);
        getSupportActionBar().hide();
    }
    public void comprovarConta(View view) {
         EditText usuari = findViewById(R.id.user);
         EditText password = findViewById(R.id.password);
         TextView userWrong = findViewById(R.id.userWrong);
         TextView passWrong = findViewById(R.id.passWrong);

        String user = usuari.getText().toString();
        String pass = password.getText().toString();


        if (user.equals("Admin") && pass.equals("admin")) {
            passWrong.setVisibility(View.INVISIBLE);
            userWrong.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(this, PantallaHome.class);
            startActivity(intent);
        } else if(user != "Admin" && pass.equals("admin")) {
            userWrong.setVisibility(View.VISIBLE);
            passWrong.setVisibility(View.INVISIBLE);
        }
        else if(pass != "admin" && user.equals("Admin")){

            passWrong.setVisibility(View.VISIBLE);
            userWrong.setVisibility(View.INVISIBLE);
        }
        else{
            userWrong.setVisibility(View.VISIBLE);
            passWrong.setVisibility(View.VISIBLE);
        }

    }
}
