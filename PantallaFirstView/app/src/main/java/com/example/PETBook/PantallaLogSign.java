package com.example.PETBook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pantallafirstview.R;

public class PantallaLogSign extends AppCompatActivity {


    static String username;
    static String passwordd;
    private Conexion conexion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_log_sign);
        getSupportActionBar().hide();
        conexion = new Conexion();
    }
    public void signUp(View view){
        Intent intent = new Intent(this, PantallaSignUp.class);
        startActivity(intent);
    }

    public void comprovarConta(View view) {

        EditText usuari = findViewById(R.id.user);
        EditText password = findViewById(R.id.password);
        TextView userWrong = findViewById(R.id.userWrong);
        TextView passWrong = findViewById(R.id.passWrong);


        String user = usuari.getText().toString();
        String pass = password.getText().toString();

        String conta = conexion.comprobarUsuario(user,pass);
        char success = conta.charAt(0);
        char mailconf = conta.charAt(1);
        if (success == 't') {
            Intent intent = new Intent(this, PantallaHome.class);
            startActivity(intent);
        }
        else if(conta == "malament") {
            userWrong.setVisibility(View.VISIBLE);
            passWrong.setVisibility(View.INVISIBLE);
        }
        else if(success == 'f'){

            passWrong.setVisibility(View.VISIBLE);
            userWrong.setVisibility(View.INVISIBLE);
        }
        else{
            userWrong.setVisibility(View.VISIBLE);
            passWrong.setVisibility(View.VISIBLE);
        }
    }
}
