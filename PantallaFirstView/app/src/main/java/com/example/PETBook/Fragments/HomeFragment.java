package com.example.PETBook.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.Image;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import static android.app.Activity.RESULT_OK;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements AsyncResult {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /* ATRIBUTOS   */
    private View MyView;

    private TextView inputFullName;
    private TextView inputEmail;
    private TextView inputBirthday;
    private TextView inputPostalCode;
    private ImageView imatgePerfil;
    private Button buttonEditProfile;
    private String tipoConexion;
    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        MyView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        // Set tittle to the fragment
        getActivity().setTitle("Profile");
        imatgePerfil = MyView.findViewById(R.id.imatgePerfilHome);
        inputFullName      = (TextView) MyView.findViewById(R.id.fullNameInput);
        inputEmail      = (TextView) MyView.findViewById(R.id.emailInput);
        inputBirthday      = (TextView) MyView.findViewById(R.id.birthdayInput);
        inputPostalCode  = (TextView) MyView.findViewById(R.id.postalCodeInput);


        getUser();
        getPicture();
        buttonEditProfile = (Button) MyView.findViewById(R.id.editProfileButton);

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment=null;
                myFragment = new EditProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main,myFragment).commit();

//                editProfile();
            }
        });
        MyView.findViewById(R.id.imatgePerfilHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });

        return MyView;
    }

    private void getUser(){
        tipoConexion = "getUser";
        Conexion con = new Conexion(HomeFragment.this);
        SingletonUsuario su = SingletonUsuario.getInstance();

        con.execute("http://10.4.41.146:9999/ServerRESTAPI/GetUser/" + su.getEmail(),"GET", null);
    }

    private void getPicture(){
        tipoConexion = "getPicture";
        SingletonUsuario user = SingletonUsuario.getInstance();
        if (user.getProfilePicture() == null) {
            Conexion con = new Conexion(this);

            con.execute("http://10.4.41.146:9999/ServerRESTAPI/getPicture/" + user.getEmail(), "GET", null);
        }
        else {
            imatgePerfil.setImageBitmap(user.getProfilePicture());
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                Conexion con = new Conexion(this);
                con.execute("http://10.4.41.146:9999/ServerRESTAPI/setPicture/" + user.getEmail(), "POST", jsonToSend.toString());
            }
        }
        //Uri returnUri;
        //returnUri = data.getData();

    }
    @Override
    public void OnprocessFinish(JSONObject output) {
        if (output != null) {
            if (tipoConexion == "getUser") {
                try {
                    int response = output.getInt("code");
                    if (response == 200) {
                        inputFullName.setText(output.getString("firstName") + " " + output.getString("secondName"));
                        inputEmail.setText(output.getString("email"));
                        inputBirthday.setText(output.getString("dateOfBirth"));
                        inputPostalCode.setText(output.getString("postalCode"));
                    } else {
                        Toast.makeText(getActivity(), "There was a problem during the process.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (tipoConexion == "getPicture") {
                try {
                    if(output.getInt("code")==200) {
                            // convert string to bitmap
                            SingletonUsuario user = SingletonUsuario.getInstance();
                            Image imagenConversor = Image.getInstance();
                            String image = output.getString("image");
                            Bitmap profileImage = imagenConversor.StringToBitMap(image);
                            imatgePerfil.setImageBitmap(profileImage);
                            user.setProfilePicture(profileImage);
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
        }
    }
}




