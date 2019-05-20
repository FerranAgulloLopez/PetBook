package com.example.PETBook;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.PETBook.Controllers.AsyncResult;
import com.example.pantallafirstview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

public class EditProfile extends AppCompatActivity implements AsyncResult {

    /* ATRIBUTOS   */
    private View MyView;

    private TextInputLayout textInputName;
    private TextInputLayout textInputSurnames;
    private TextInputLayout textInputMail;
    private TextInputLayout textInputPassword1;
    private TextInputLayout textInputPassword2;
    private TextInputLayout textInputBirthday;
    private TextInputLayout textInputPostalCode;
    private Button buttonEditProfile;
    private String tipoConexion;
    Calendar calendario = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_profile);
        Bundle profileEdit = getIntent().getExtras();
        // Set tittle to the fragment
        this.setTitle("Edit Profile");

        textInputName      = (TextInputLayout) MyView.findViewById(R.id.nameTextInput);
        textInputSurnames      = (TextInputLayout) MyView.findViewById(R.id.surnamesTextInput);
        textInputMail      = (TextInputLayout) MyView.findViewById(R.id.mailTextInput);
        textInputPassword1  = (TextInputLayout) MyView.findViewById(R.id.password1TextInput);
        textInputPassword2 = (TextInputLayout) MyView.findViewById(R.id.password2TextInput);
        textInputBirthday  = (TextInputLayout) MyView.findViewById(R.id.birthdayTextInput);
        textInputPostalCode  = (TextInputLayout) MyView.findViewById(R.id.postalCodeTextInput);

        tipoConexion = "getUser";
        Conexion con = new Conexion(EditProfile.this);
        SingletonUsuario su = SingletonUsuario.getInstance();

        con.execute("http://10.4.41.146:9999/ServerRESTAPI/GetUser/" + su.getEmail(),"GET", null);

        textInputBirthday.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditProfile.this, date, calendario
                        .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        buttonEditProfile = (Button) MyView.findViewById(R.id.editProfileButton);

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
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

    private void editProfile() {
        tipoConexion = "updateUser";
        SingletonUsuario su = SingletonUsuario.getInstance();
        String name = textInputName.getEditText().getText().toString().trim();
        String surnames = textInputSurnames.getEditText().getText().toString().trim();
        String password1 = textInputPassword1.getEditText().getText().toString().trim();
        String password2 = textInputPassword2.getEditText().getText().toString().trim();
        String birthday = textInputBirthday.getEditText().getText().toString().trim();
        String postalCode = textInputPostalCode.getEditText().getText().toString().trim();

        boolean isValidName = validateName(name);
        boolean isValidSurname = validateSurnames(surnames);
        boolean isValidPassword = validatePasswords(password1,password2);
        boolean isValidBirthday = validateBirthday(birthday);
        boolean isValidPostalCode = validatePostalCode(postalCode);

        if (isValidName && isValidSurname && isValidPassword && isValidBirthday && isValidPostalCode) {

            JSONObject jsonToSend = new JSONObject();
            try {
                jsonToSend.accumulate("firstName", name);
                jsonToSend.accumulate("secondName", surnames);
                jsonToSend.accumulate("dateOfBirth", birthday);
                jsonToSend.accumulate("postalCode", postalCode);
                //jsonToSend.accumulate("password", password1);

            } catch (Exception e) {
                e.printStackTrace();
            }

            Conexion con = new Conexion(EditProfile.this);
            con.execute("http://10.4.41.146:9999/ServerRESTAPI/update/" + su.getEmail(), "PUT", jsonToSend.toString());
        }
    }
    @Override
    public void OnprocessFinish(JSONObject output) {
        if (output != null) {
            if(tipoConexion.equals("getUser")) {
                try {
                    int response = output.getInt("code");
                    if (response == 200) {
                        textInputName.getEditText().setText(output.getString("firstName"));
                        textInputSurnames.getEditText().setText(output.getString("secondName"));
                        textInputMail.getEditText().setText(output.getString("email"));
                        textInputBirthday.getEditText().setText(output.getString("dateOfBirth"));
                        textInputPostalCode.getEditText().setText(output.getString("postalCode"));
                    } else {
                        Toast.makeText(this, "There was a problem during the process.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if(tipoConexion.equals("updateUser")) {
                try {
                    int response = output.getInt("code");
                    if (response == 200 || response == 201) {
                        Toast.makeText(this, "User updated succesfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "There was a problem during the process.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(this, "The server does not work.", Toast.LENGTH_SHORT).show();
        }
    }

}
