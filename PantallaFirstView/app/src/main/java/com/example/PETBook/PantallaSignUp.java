package com.example.PETBook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import java.util.Calendar;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import java.text.SimpleDateFormat;
import com.example.pantallafirstview.R;
import java.util.Locale;

public class PantallaSignUp extends AppCompatActivity {

    TextView labelName;
    TextView labelSurnames;
    TextView labelMail;
    TextView labelPassword1;
    TextView labelPassword2;
    TextView labelBirthday;

    private EditText inputName;
    private EditText inputSurnames;
    private EditText inputMail;
    private EditText inputPassword1;
    private EditText inputPassword2;
    private EditText inputBirthday;

    private Button   buttonSignIn;

    Calendar calendario = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_sign_up);

        labelName = (TextView) findViewById(R.id.nameLabel);
        labelSurnames = (TextView) findViewById(R.id.surnamesLabel);
        labelMail = (TextView) findViewById(R.id.mailLabel);
        labelPassword1 = (TextView) findViewById(R.id.password1Label);
        labelPassword2 = (TextView) findViewById(R.id.password2Label);
        labelBirthday = (TextView) findViewById(R.id.birthdayLabel);



        inputName      = (EditText) findViewById(R.id.nameInput);
        inputSurnames  = (EditText) findViewById(R.id.surnamesInput);
        inputMail      = (EditText) findViewById(R.id.mailInput);
        inputPassword1  = (EditText) findViewById(R.id.password1Input);
        inputPassword2 = (EditText) findViewById(R.id.password2Input);
        inputBirthday  = (EditText) findViewById(R.id.birthdayInput);

        buttonSignIn   = (Button) findViewById(R.id.signInButton);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PantallaSignUp.this, date, calendario
                        .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendario.set(Calendar.YEAR, year);
            calendario.set(Calendar.MONTH, monthOfYear);
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInput();
        }

    };

    private void actualizarInput() {
        String formatoDeFecha = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        buttonSignIn.setText(sdf.format(calendario.getTime()));
    }

    public void signUp(View view){
        Intent intent = new Intent(this, PantallaHome.class);
        startActivity(intent);

    }
}
