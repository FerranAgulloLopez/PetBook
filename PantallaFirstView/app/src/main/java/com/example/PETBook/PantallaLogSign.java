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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_log_sign);
        getSupportActionBar().hide();


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

        boolean conta = Conexion.comprobarUsuario(user,pass);

        if (conta) {
            Intent intent = new Intent(this, PantallaHome.class);
            startActivity(intent);
        }
        /*else if(user != "admin" && pass.equals("admin")) {
            userWrong.setVisibility(View.VISIBLE);
            passWrong.setVisibility(View.INVISIBLE);
        }
        else if(pass != "admin" && user.equals("admin")){

            passWrong.setVisibility(View.VISIBLE);
            userWrong.setVisibility(View.INVISIBLE);
        }*/
        else{
            userWrong.setVisibility(View.VISIBLE);
            passWrong.setVisibility(View.VISIBLE);
        }
    }
}
