package com.example.PETBook.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.MainActivity;
import com.example.PETBook.Models.Image;
import com.example.PETBook.NewPet;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
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


    private String username;
    private String name;
    private ImageView imageProfile;
    private Integer functionType = 0; // 1 -> get ; 2 -> post


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

        // Set tittle to the fragment
        getActivity().setTitle("Mi perfil");

        // De momento layout activity_pantalla_home
        View MyView = inflater.inflate(R.layout.activity_pantalla_home, container, false);

        imageProfile = MyView.findViewById(R.id.fotoPerfil);

        SingletonUsuario user = SingletonUsuario.getInstance();

        if (user.getProfilePicture() == null) {

            functionType = 1; // get
            Conexion con = new Conexion(this);
            con.execute("http://10.4.41.146:9999/ServerRESTAPI/getPicture/" + user.getEmail(), "GET", null);
        }
        else {
            imageProfile.setImageBitmap(user.getProfilePicture());
        }


        MyView.findViewById(R.id.fotoPerfil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 1);
            }
        });


        TextView usuari = MyView.findViewById(R.id.username);
        SingletonUsuario su = SingletonUsuario.getInstance();
        usuari.setText(su.getEmail());

        return MyView;
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
                imageProfile.setImageBitmap(bitmapImage);

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

                functionType = 2; // post
                Conexion con = new Conexion(this);
                con.execute("http://10.4.41.146:9999/ServerRESTAPI/setPicture/" + user.getEmail(), "POST", jsonToSend.toString());
            }
        }
        //Uri returnUri;
        //returnUri = data.getData();

    }




    @Override
    public void OnprocessFinish(JSONObject output) {

        try {
            if(output.getInt("code")==200) {

                if (functionType == 1) {
                    // convert string to bitmap
                    SingletonUsuario user = SingletonUsuario.getInstance();
                    Image imagenConversor = Image.getInstance();
                    String image = output.getString("image");
                    Bitmap profileImage = imagenConversor.StringToBitMap(image);
                    imageProfile.setImageBitmap(profileImage);
                    user.setProfilePicture(profileImage);
                }

            }
            else if (output.getInt("code")==404) { // user does not have profile picture
                imageProfile.setImageResource(R.drawable.troymcclure);
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



    /*
    Necesario porque de momento está puesto como layout el de activity_pantalla_home
     */
    public void myPets(View view){
        System.out.println("HomeFragment");
        // Intent intent = new Intent(this, PetsContainer.class);
        //startActivity(intent);
    }
    public void myPosts(View view){
        // Intent intent = new Intent(this, MyPosts.class);
        //  startActivity(intent);
    }
    public void myCalendar(View view) {
        // Intent intent = new Intent(this, MyCalendar.class);
        // startActivity(intent);
    }
    public void myEvents(View view){
        //  Intent intent = new Intent(this, MyEvents.class);
        //  startActivity(intent);
    }
}
