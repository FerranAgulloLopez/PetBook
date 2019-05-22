package com.example.PETBook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Fragments.HomeWallFragment;
import com.example.PETBook.Models.Image;
import com.example.PETBook.Models.WallModel;
import com.example.pantallafirstview.R;

import org.json.JSONException;
import org.json.JSONObject;

public class WallInfo extends AppCompatActivity implements AsyncResult {


    private TextView inputFullName;
    private TextView inputEmail;
    private TextView inputBirthday;
    private TextView inputPostalCode;
    private ImageView imatgePerfil;
    private Button buttonEditProfile;
    private String tipoConexion;
    private WallModel wallModel;
    private ListView listWalls;
    private TextView descriptionWall;
    private TextView dataCreacioWall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        imatgePerfil = findViewById(R.id.imatgePerfilHome);
        inputFullName = findViewById(R.id.fullNameInput);
        inputEmail = findViewById(R.id.emailInput);
        inputBirthday = findViewById(R.id.birthdayInput);
        inputPostalCode = findViewById(R.id.postalCodeInput);
        listWalls = findViewById(R.id.list_walls);
        inputFullName.setText("Hola");
        /*mostrarWalls();*/
        Conexion con = new Conexion(WallInfo.this);
        SingletonUsuario su = SingletonUsuario.getInstance();
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/GetUser/" + su.getEmail() , "GET", null);
       /* mostrarPerfil();*/
        buttonEditProfile = (Button) findViewById(R.id.editProfileButton);

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WallInfo.this,EditProfile.class);
                //intent.putExtra("pet", petModel);
                startActivity(intent);
//                editProfile();
            }
        });
      /* findViewById(R.id.imatgePerfilHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });*/
    }

    private void mostrarWalls(){
        System.out.println("entro a mostrar walls");
        Bundle datosRecibidos = this.getIntent().getExtras();
        if(datosRecibidos != null) {
            wallModel = (WallModel) datosRecibidos.getSerializable("wall");
            System.out.print("La ventana recibe los datos ya que el bundle no es vacio\n");

            descriptionWall.setText(wallModel.getDescription());
            dataCreacioWall.setText(wallModel.getCreationDate());
        }

    }
    private void mostrarPerfil(){
        System.out.println("entro a mostrar perfil");
        tipoConexion = "getUser";
        System.out.println("conexion mostrar user bien hecha");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /*
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (resultCode == RESULT_OK) {
                if (requestCode == 1) {
                    Uri returnUri = data.getData();
                    Bitmap bitmapImage = null;
                    try {
                        bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imatgePerfil.setImageBitmap(bitmapImage);

                    SingletonUsuario user = SingletonUsuario.getInstance();
                    user.setProfilePicture(bitmapImage);

                    Image imageConversor = Image.getInstance();
                    String imageEncoded = imageConversor.BitmapToString(bitmapImage);

                    JSONObject jsonToSend = new JSONObject();
                    try {
                        jsonToSend.accumulate("image", imageEncoded);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //functionType = 2; // post
                   /* Conexion con = new Conexion(this);
                    con.execute("http://10.4.41.146:9999/ServerRESTAPI/setPicture/" + user.getEmail(), "POST", jsonToSend.toString());
                }
            }
            //Uri returnUri;
            //returnUri = data.getData();

        }*/
    @Override
    public void OnprocessFinish(JSONObject output) {
        if (output != null) {
            //if (tipoConexion.equals("getUser")) {
                try {
                    System.out.println("entro a mostrar el user");
                    if (output.getInt("code")==200) {
                        inputFullName.setText(output.getString("firstName") + " " + output.getString("secondName"));
                        inputEmail.setText("@" + output.getString("email"));
                        inputBirthday.setText(output.getString("dateOfBirth"));
                        inputPostalCode.setText(output.getString("postalCode"));
                    } else {
                        Toast.makeText(this, "There was a problem during the process.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } /*else if (tipoConexion == "getPicture") {
                try {
                    if(output.getInt("code")==200) {
                        // convert string to bitmap
                        SingletonUsuario user = SingletonUsuario.getInstance();
                        Image imagenConversor = Image.getInstance();
                        String image = output.getString("image");
                        Bitmap profileImage = imagenConversor.StringToBitMap(image);
                        imatgePerfil.setImageBitmap(profileImage);
                        user.setProfilePicture(profileImage);
                            /*Intent intent = new Intent(HomeWallFragment.this, MainActivity.class);
                            startActivity(intent);
                    }
                    else if (output.getInt("code")==404) { // user does not have profile picture
                        imatgePerfil.setImageResource(R.drawable.troymcclure);
                    }
                    else{
                        System.out.println("ERRRRRROOOOOOOR, code ----------> " + output.getInt("code"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        System.out.println(output.getInt("code") +"\n\n\n");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        }*/
    }
}
