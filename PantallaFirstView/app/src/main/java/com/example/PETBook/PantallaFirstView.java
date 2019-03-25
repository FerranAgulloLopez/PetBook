package com.example.PETBook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.pantallafirstview.R;

public class PantallaFirstView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_view);
        getSupportActionBar().hide();
    }
    public void nextScreen(View view){
        Intent intent = new Intent(this, PantallaLogSign.class);
        startActivity(intent);

    }
}
