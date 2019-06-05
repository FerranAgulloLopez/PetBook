package com.example.PETBook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Firebase.FirebaseService;
import com.example.pantallafirstview.R;
import com.google.firebase.iid.FirebaseInstanceId;

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

            SingletonUsuario.getInstance().setMailConfirmed(false);

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
                Boolean mailconfirmed = json.getBoolean("mailconfirmed");
                Boolean admin = json.getBoolean("admin");
                Boolean banned = json.getBoolean("banned");
                if(success.equals("true")) {
                    userWrong.setVisibility(View.INVISIBLE);
                    passWrong.setVisibility(View.INVISIBLE);
                    SingletonUsuario user = SingletonUsuario.getInstance();
                    SingletonUsuario.setEmail(usuari.getText().toString());
                    user.setMailConfirmed(mailconfirmed);
                    user.setAdmin(admin);
                    user.setBanned(banned);

                    String token = json.getString("token");
                    user.setJwtToken(token);
                    user.setProfilePicture(null);
                    System.out.println("Ha ido bien, codigo 200");




                    /*
                    Register that the user has logged in
                     */
                    SharedPreferences sharedPreferences = getSharedPreferences("credenciales", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("login", usuari.getText().toString());
                    editor.putString("jwtToken", token);
                    editor.putBoolean("mailConfirmed", mailconfirmed);
                    editor.putBoolean("admin", admin);
                    editor.putBoolean("banned", banned);
                    editor.commit();


                    /*
                    Send firebase token to server
                     */

                    String tokenFirebase = FirebaseInstanceId.getInstance().getToken();
                    System.out.println("!!!!!!!!!!!! TOKEN FCM !!!!!!!!!!!!!!!");
                    Log.d("Firebase", "token " + tokenFirebase);
                    FirebaseService firebaseService = new FirebaseService();
                    firebaseService.sendRegistrationToServer(tokenFirebase);


                    // enviar correo de confirmacion al registrarse
                    Conexion conexion = new Conexion(PantallaLogSign.this);
                    conexion.execute("http://10.4.41.146:9999/ServerRESTAPI/SendConfirmationEmail", "POST", null);


                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("fragment", "home");
                    startActivity(intent);
                    finish();
                }
                else if(success.equals("false")){
                    userWrong.setVisibility(View.INVISIBLE);
                    passWrong.setVisibility(View.VISIBLE);
                }
            }
            else{
               /* userWrong.setVisibility(View.VISIBLE);
                passWrong.setVisibility(View.INVISIBLE);*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


