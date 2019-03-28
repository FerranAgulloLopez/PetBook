package com.example.PETBook;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
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
import android.widget.Toast;

public class PantallaSignUp extends AppCompatActivity {

    private TextInputLayout textInputName;
    private TextInputLayout textInputSurnames;
    private TextInputLayout textInputMail;
    private TextInputLayout textInputPassword1;
    private TextInputLayout textInputPassword2;
    private TextInputLayout textInputBirthday;

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

        textInputName      = (TextInputLayout) findViewById(R.id.nameTextInput);
        textInputSurnames  = (TextInputLayout) findViewById(R.id.surnamesTextInput);
        textInputMail      = (TextInputLayout) findViewById(R.id.mailTextInput);
        textInputPassword1  = (TextInputLayout) findViewById(R.id.password1TextInput);
        textInputPassword2 = (TextInputLayout) findViewById(R.id.password2TextInput);
        textInputBirthday  = (TextInputLayout) findViewById(R.id.birthdayTextInput);

        inputName      = (EditText) findViewById(R.id.nameInput);
        inputSurnames  = (EditText) findViewById(R.id.surnamesInput);
        inputMail      = (EditText) findViewById(R.id.mailInput);
        inputPassword1  = (EditText) findViewById(R.id.password1Input);
        inputPassword2 = (EditText) findViewById(R.id.password2Input);
        inputBirthday  = (EditText) findViewById(R.id.birthdayInput);

        buttonSignIn   = (Button) findViewById(R.id.signInButton);


        inputBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PantallaSignUp.this, date, calendario
                        .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
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
        String formatoDeFecha = "dd/mm/yy"; //In which you need put here
        Locale spanish = new Locale("es", "ES");
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, spanish);

        inputBirthday.setText(sdf.format(calendario.getTime()));
    }

    private boolean validateName() {
        if (inputName.getText().toString().isEmpty()) {
            textInputName.setError("Field can't be empty");
            return false;
        } else {
            textInputName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateSurnames() {
        if (inputSurnames.getText().toString().isEmpty()) {
            textInputSurnames.setError("Field can't be empty");
            return false;
        } else {
            textInputSurnames.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        if (inputMail.getText().toString().isEmpty()) {
            textInputMail.setError("Field can't be empty");
            return false;
        } else {
            textInputMail.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePasswords() {
        if (inputPassword1.getText().toString().isEmpty()) {
            textInputPassword1.setError("Field can't be empty");
            return false;
        } else if (inputPassword1.getText().toString().trim().length() > 8 ) {
            textInputPassword1.setError("Password too long");
            return false;
        } else {
            textInputPassword1.setErrorEnabled(false);
            return true;
        }

        if (inputPassword2.getText().toString().isEmpty()) {
            textInputPassword2.setError("Field can't be empty");
            return false;
        } else if (inputPassword2.getText().toString().trim().length() > 8 ) {
            textInputPassword2.setError("Password too long");
            return false;
        } else {
            textInputPassword2.setErrorEnabled(false);
            return true;
        }
    }


    private void signUp() {

        if (validateName() && validateSurnames() && validateEmail() && validatePasswords()) {
            /* recoger datos para backend

            String name = inputName.getText().toString();
            String surnames = inputSurnames.getText().toString();
            String email = inputMail.getText().toString();
            String password1 = inputPassword1.getText().toString();
            String password2 = inputPassword2.getText().toString();
            String birthday = inputPassword2.getText().toString();
            */
            Toast.makeText(this, "Registrado correctamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, PantallaLogSign.class);
            startActivity(intent);
        }
    }
}
