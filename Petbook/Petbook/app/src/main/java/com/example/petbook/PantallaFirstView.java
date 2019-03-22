package com.example.petbook;

import android.content.Intent;
import android.net.sip.SipSession;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PantallaFirstView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_view);
        getSupportActionBar().hide();

    }
    public void goNext(View view){
        Intent intent = new Intent(this, LogInSignUp.class);
        startActivity(intent);
    }
}
