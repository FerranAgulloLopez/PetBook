package com.example.PETBook;

import android.content.Intent;
import android.os.StrictMode;
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
import com.example.PETBook.Controllers.AsyncResult;
import java.util.Locale;
import android.util.Patterns;
import java.util.regex.Pattern;
import android.widget.Toast;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

public class PantallaSignUp extends AppCompatActivity implements AsyncResult {

    private TextInputLayout textInputName;
    private TextInputLayout textInputSurnames;
    private TextInputLayout textInputMail;
    private TextInputLayout textInputPassword1;
    private TextInputLayout textInputPassword2;
    private TextInputLayout textInputBirthday;
    private TextInputLayout textInputPostalCode;

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
        textInputPostalCode  = (TextInputLayout) findViewById(R.id.postalCodeTextInput);

        buttonSignIn   = (Button) findViewById(R.id.signInButton);

        textInputBirthday.getEditText().setOnClickListener(new View.OnClickListener() {
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
/*
     String formatoDeFecha = "dd/MM/yy"; //In which you need put here
     Locale spanish = new Locale("es", "ES");
     SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, spanish);

     inputBirthday.setText(sdf.format(calendario.getTime()));
*/
        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        textInputBirthday.getEditText().setText(sdf.format(calendario.getTime()));
    }

    private boolean validateName(String name) {
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        textInputName.setErrorTextAppearance(R.style.text_error);
        textInputName.getEditText().setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check_wrong24dp,0);
        if (name.isEmpty()) {
            textInputName.setError("Please enter your first name");
            return false;
        } else if (name.length() > 20) {
            textInputName.setError("First Name is too long");
            return false;
        } else if (!patron.matcher(name).matches()) {
            textInputName.setError("Please enter a valid name");
            return false;
        } else {
            textInputName.setErrorTextAppearance(R.style.text_success);
            textInputName.setError(" ");
            textInputName.getEditText().setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check_success24dp,0);
            return true;
        }
    }

    private boolean validateSurnames(String surnames) {
        Pattern patron = Pattern.compile("^[a-zA-Z ]+$");
        textInputSurnames.setErrorTextAppearance(R.style.text_error);
        textInputSurnames.getEditText().setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check_wrong24dp,0);
        if (surnames.isEmpty()) {
            textInputSurnames.setError("Please enter your last name");
            return false;
        } else if (surnames.length() > 30) {
            textInputSurnames.setError("Last Name too long");
            return false;
        } else if (!patron.matcher(surnames).matches()) {
            textInputSurnames.setError("Please enter a valid last name");
            return false;
        } else {
            textInputSurnames.setErrorTextAppearance(R.style.text_success);
            textInputSurnames.setError(" ");
            textInputSurnames.getEditText().setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check_success24dp,0);
            return true;
        }
    }

    private boolean validateEmail(String email) {
        textInputMail.setErrorTextAppearance(R.style.text_error);
        textInputMail.getEditText().setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check_wrong24dp,0);
        if (email.isEmpty()) {
            textInputMail.setError("Please enter your email address");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textInputMail.setError("Please enter a valid email address");
            return false;
        } else {
            textInputMail.setErrorTextAppearance(R.style.text_success);
            textInputMail.setError(" ");
            textInputMail.getEditText().setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check_success24dp,0);
            return true;
        }
    }

    private boolean validatePasswords(String password1, String password2) {
        boolean passwords_ok = false;
        textInputPassword1.setErrorTextAppearance(R.style.text_error);
        if (password1.isEmpty()) {
            textInputPassword1.setError("Please enter your password");
            passwords_ok = false;
        } else if (password1.length() < 8) {
            textInputPassword1.setError("Password too short");
            passwords_ok = false;
        } else {
            textInputPassword1.setErrorTextAppearance(R.style.text_success);
            textInputPassword1.setError(" ");
        }

        textInputPassword2.setErrorTextAppearance(R.style.text_error);
        if (password2.isEmpty()) {
            textInputPassword2.setError("Please retype your password");
            passwords_ok = false;
        } else if (password2.length() < 8) {
            textInputPassword2.setError("Confirm password too short");
            passwords_ok = false;
        } else {
            textInputPassword2.setErrorTextAppearance(R.style.text_success);
            textInputPassword2.setError(" ");
            if (password1.equals(password2)) {
                //textInputPassword1.getEditText().setPadding(0,0,0,0);
                //textInputPassword2.getEditText().setPadding(0,0,0,0);
                textInputPassword1.setErrorTextAppearance(R.style.text_success);
                textInputPassword1.setError(" ");
                textInputPassword2.setErrorTextAppearance(R.style.text_success);
                textInputPassword2.setError(" ");
                passwords_ok = true;
            } else {
                textInputPassword1.setErrorTextAppearance(R.style.text_error);
                textInputPassword2.setErrorTextAppearance(R.style.text_error);
                textInputPassword1.setError("The password and its confirm are not the same");
                textInputPassword2.setError("The password and its confirm are not the same");
                passwords_ok = false;
            }
        }

        return passwords_ok;
    }

    private boolean validatePostalCode(String postalCode) {
        Pattern patron = Pattern.compile("^[0-5][1-9]{3}[0-9]$");
        textInputPostalCode.setErrorTextAppearance(R.style.text_error);
        textInputPostalCode.getEditText().setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check_wrong24dp,0);
        if (postalCode.isEmpty()) {
            textInputPostalCode.setError("Please enter your postal code");
            return false;
        } else if (!patron.matcher(postalCode).matches()) {
            textInputPostalCode.setError("Postal Code is not valid");
            return false;
        } else {
            textInputPostalCode.setErrorTextAppearance(R.style.text_success);
            textInputPostalCode.setError(" ");
            textInputPostalCode.getEditText().setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check_success24dp,0);
            return true;
        }
    }

    private boolean validateBirthday(String birthday) {
        textInputBirthday.setErrorTextAppearance(R.style.text_error);
        if (birthday.isEmpty()) {
            textInputBirthday.setError("Please enter your birthday");
            textInputBirthday.getEditText().setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check_wrong24dp,0);
            textInputBirthday.getEditText().setCompoundDrawablePadding(10);
            return false;
        } else {
            textInputBirthday.setErrorTextAppearance(R.style.text_success);
            textInputBirthday.setError(" ");
            textInputBirthday.getEditText().setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.check_success24dp,0);
            return true;
        }
    }

    private void signUp() {

        /* recoger datos para backend */
        String name = textInputName.getEditText().getText().toString().trim();
        String surnames = textInputSurnames.getEditText().getText().toString().trim();
        String email = textInputMail.getEditText().getText().toString().trim();
        String password1 = textInputPassword1.getEditText().getText().toString().trim();
        String password2 = textInputPassword2.getEditText().getText().toString().trim();
        String birthday = textInputBirthday.getEditText().getText().toString().trim();
        String postalCode = textInputPostalCode.getEditText().getText().toString().trim();
        boolean isValidName = validateName(name);
        boolean isValidSurname = validateSurnames(surnames);
        boolean isValidEmail = validateEmail(email);
        boolean isValidPassword = validatePasswords(password1,password2);
        boolean isValidBirthday = validateBirthday(birthday);
        boolean isValidPostalCode = validatePostalCode(postalCode);
        if (isValidName && isValidSurname && isValidEmail && isValidPassword && isValidBirthday && isValidPostalCode) {
            /* Juntar los datos en un Json para ponerlo en el body */

            JSONObject jsonToSend = new JSONObject();
            try {
                jsonToSend.accumulate("dateOfBirth", birthday);
                jsonToSend.accumulate("email", email);
                jsonToSend.accumulate("firstName", name);
                jsonToSend.accumulate("mailconfirmed", true);
                jsonToSend.accumulate("password", password1);
                jsonToSend.accumulate("postalCode", postalCode);
                jsonToSend.accumulate("secondName", surnames);
                System.out.println(jsonToSend);
            } catch (JSONException e) {
                e.printStackTrace();
            }



            /* Nueva conexion llamando a la funcion del server */

            Conexion con = new Conexion(this);
            con.execute("http://10.4.41.146:9999/ServerRESTAPI/RegisterUser/", "POST", jsonToSend.toString());
        }
    }

    @Override
    public void OnprocessFinish(JSONObject output) {
        if (output != null) {
            try {
                int response = output.getInt("code");
                if (response == 200) {
                    Toast.makeText(this, "Registrado correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, PantallaLogSign.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "ERRORRRRRR", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, PantallaLogSign.class);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast toast = Toast.makeText(this, "El server no funciona", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}

