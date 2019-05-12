package com.example.PETBook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.pantallafirstview.R;
import com.google.firebase.iid.FirebaseInstanceId;

public class PantallaFirstView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_view);
        getSupportActionBar().hide();

        SharedPreferences sharedPreferences = getSharedPreferences("credenciales", MODE_PRIVATE);

        String login = sharedPreferences.getString("login",null);
        String token = sharedPreferences.getString("jwtToken", null);



        System.out.println("!!!!!!!!!!!! TOKEN FCM !!!!!!!!!!!!!!!");
        Log.d("Firebase", "token " + FirebaseInstanceId.getInstance().getToken());



        /*
        if user was logged move directly to MainActivity
         */
        if (login != null) {
            SingletonUsuario user = SingletonUsuario.getInstance();
            SingletonUsuario.setEmail(login);
            if (token != null) user.setJwtToken(token);
            Intent intent = new Intent(this, Chat.class);
            startActivity(intent);
            finish();
        }

    }
    public void nextScreen(View view){
        Intent intent = new Intent(this, PantallaLogSign.class);
        startActivity(intent);
        finish();
    }
}
