package com.example.PETBook;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pantallafirstview.R;

import org.json.JSONObject;

public class PantallaLogSign extends AppCompatActivity {


    static String username;
    static String passwordd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_log_sign);
        getSupportActionBar().hide();
    }

    public void signUp(View view) {
        Intent intent = new Intent(this, PantallaSignUp.class);
        startActivity(intent);
    }

    public void comprovarConta(View view) {
        try {

            EditText usuari = findViewById(R.id.user);
            EditText password = findViewById(R.id.password);
            TextView userWrong = findViewById(R.id.userWrong);
            TextView passWrong = findViewById(R.id.passWrong);


            String user = usuari.getText().toString();
            String pass = password.getText().toString();

            Conexion con = new Conexion("http://10.4.41.146:9999/ServerRESTAPI/ConfirmLogin?email=" + user + "&password=" + pass,
                    "POST", null);


            // ESTO HACE QUE LO PERMITA TODO, basicamente todo lo ejecuta el mismo thread( el principal)
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            // Lo suyo seria que hicieras que lo del Background funcionara, o otro metodo

            JSONObject json = con.doInBackground();


            if (json.getInt("code") == 200) {
                String success = json.getString("success");
                String mail = json.getString("mailconfirmed");
                if(success.equals("true")) {
                    SingletonUsuario.getInstance();
                    SingletonUsuario.setEmail(user);
                    // System.out.println("Ha ido bien, codigo 200");
                    Intent intent = new Intent(this, PantallaHome.class);
                    startActivity(intent);
                    finish();
                }
                else if(success.equals("false")){
                    userWrong.setVisibility(View.INVISIBLE);
                    passWrong.setVisibility(View.VISIBLE);
                }
            }
            else{
                userWrong.setVisibility(View.VISIBLE);
                passWrong.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}


