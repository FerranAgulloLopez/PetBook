package com.example.PETBook;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.PETBook.Controllers.AsyncResult;
import com.example.pantallafirstview.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PantallaLogSign extends AppCompatActivity implements AsyncResult {


    static String username;
    static String passwordd;

    EditText usuari;
    EditText password;
    TextView userWrong;
    TextView passWrong;

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


            usuari = findViewById(R.id.user);
            password = findViewById(R.id.password);
            userWrong = findViewById(R.id.userWrong);
            passWrong = findViewById(R.id.passWrong);


            String user = usuari.getText().toString();
            String pass = password.getText().toString();

            Conexion con = new Conexion(this);
            con.execute("http://10.4.41.146:9999/ServerRESTAPI/ConfirmLogin?email=" + user + "&password=" + pass, "POST", null);


    }


    @Override
    public void OnprocessFinish(JSONObject json) {
        try {
            if (json.getInt("code") == 200) {
                String success = json.getString("success");
                String mail = json.getString("mailconfirmed");
                if(success.equals("true")) {
                    SingletonUsuario.getInstance();
                    SingletonUsuario.setEmail(usuari.getText().toString());
                     System.out.println("Ha ido bien, codigo 200");
                    Intent intent = new Intent(this, MainActivity.class);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


