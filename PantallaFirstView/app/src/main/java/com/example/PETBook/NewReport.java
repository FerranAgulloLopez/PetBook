package com.example.PETBook;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Controllers.AsyncResult;
import com.example.pantallafirstview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class NewReport extends AppCompatActivity implements AsyncResult {


    private TextView first;
    private TextView second;
    private TextView third;
    private TextView fourth;

    private TextView textReport;

    private String emailReportado;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);


        // TODO conseguir emailReportado
        Bundle datosRecibidos = this.getIntent().getExtras();
        emailReportado = datosRecibidos.getString("emailReportado");

        first = findViewById(R.id.post);
        second = findViewById(R.id.spam);
        third = findViewById(R.id.profile);
        fourth = findViewById(R.id.feka);

        textReport = findViewById(R.id.textReport);
        textReport.setText("Help us understand the problem. What issue with " + emailReportado + " are you reporting?");


        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReport(first.getText().toString());
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReport(second.getText().toString());
            }
        });

        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReport(third.getText().toString());
            }
        });

        fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReport(fourth.getText().toString());
            }
        });

    }


    void sendReport(String text) {

        Date date = new Date();
        String creationDate = Long.toString(date.getTime());



        JSONObject jsonToSend = new JSONObject();
        try {
            jsonToSend.accumulate("creationDate", creationDate);
            jsonToSend.accumulate("description", text);
            jsonToSend.accumulate("suspectMail", emailReportado);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Conexion conexion = new Conexion(this);
        conexion.execute("http://10.4.41.146:9999/ServerRESTAPI/reports", "POST", jsonToSend.toString());




    }




    @Override
    public void OnprocessFinish(JSONObject output) {

        try {
            if(output.getInt("code")==200) {

                Toast.makeText(this, "Report donde successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NewReport.this, MainActivity.class);
                intent.putExtra("fragment","myprofile");
                startActivity(intent);
            }
            else{
                // TODO tratar errores
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }


            AlertDialog.Builder reportMade = new AlertDialog.Builder(this);
            reportMade.setMessage("Thanks for your report")
                    .setCancelable(true)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            /*
                            Intent i = new Intent(NewReport.this, MainActivity.class);
                            i.putExtra("fragment", "home");
                            startActivity(i);
                            */
                            finish();
                        }
                    });
            dialog = reportMade.create();
            dialog.setTitle("Petbook development team");
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);





        } catch (JSONException e) {
                e.printStackTrace();

        }

    }
}
