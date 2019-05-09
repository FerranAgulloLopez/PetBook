package com.example.PETBook.Fragments;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;
import android.widget.Toast;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment implements AsyncResult {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /* ATRIBUTOS   */
    private View MyView;

    private TextInputLayout textInputName;
    private TextInputLayout textInputSurnames;
    private TextInputLayout textInputMail;
    private TextInputLayout textInputBirthday;
    private TextInputLayout textInputPostalCode;

    private Button buttonEditProfile;
    private String tipoConexion;

    private OnFragmentInteractionListener mListener;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
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
        getActivity().setTitle("Mi perfil");

        textInputName      = (TextInputLayout) MyView.findViewById(R.id.nameTextInput);
        textInputSurnames      = (TextInputLayout) MyView.findViewById(R.id.surnamesTextInput);
        textInputMail      = (TextInputLayout) MyView.findViewById(R.id.mailTextInput);
        textInputBirthday  = (TextInputLayout) MyView.findViewById(R.id.birthdayTextInput);
        textInputPostalCode  = (TextInputLayout) MyView.findViewById(R.id.postalCodeTextInput);



        tipoConexion = "getUser";
        Conexion con = new Conexion(MyProfileFragment.this);
        SingletonUsuario su = SingletonUsuario.getInstance();

        con.execute("http://10.4.41.146:9999/ServerRESTAPI/GetUser/" + su.getEmail(),"GET", null);


        buttonEditProfile = (Button) MyView.findViewById(R.id.editProfileButton);

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

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

    private void editProfile() {
        tipoConexion = "updateUser";
        SingletonUsuario su = SingletonUsuario.getInstance();
        String name = textInputName.getEditText().getText().toString().trim();
        String surnames = textInputSurnames.getEditText().getText().toString().trim();
        String birthday = textInputBirthday.getEditText().getText().toString().trim();
        String postalCode = textInputPostalCode.getEditText().getText().toString().trim();

        JSONObject jsonToSend = new JSONObject();
        try{
            jsonToSend.accumulate("firstName", name);
            jsonToSend.accumulate("secondName", surnames);
            jsonToSend.accumulate("dateOfBirth", birthday);
            jsonToSend.accumulate("postalCode", postalCode);

        } catch (Exception e){
            e.printStackTrace();
        }

        Conexion con = new Conexion(MyProfileFragment.this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/update/" + su.getEmail(), "PUT", jsonToSend.toString());
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
                        Toast.makeText(getActivity(), "There was a problem during the process.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if(tipoConexion.equals("updateUser")) {
                try {
                    int response = output.getInt("code");
                    if (response == 200 && response == 201) {
                        Toast.makeText(getActivity(), "User updated succesfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "There was a problem during the process.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(getActivity(), "The server does not work.", Toast.LENGTH_SHORT).show();
        }
    }
}