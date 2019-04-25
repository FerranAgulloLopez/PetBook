package com.example.PETBook.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


//-----------

import android.widget.AdapterView;
import android.widget.ListView;


//-------------


import com.example.PETBook.Adapters.PetAdapters;
import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.PetModel;
import com.example.PETBook.NewPet;
import com.example.PETBook.PetInfo;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyPetsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyPetsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPetsFragment extends Fragment implements AsyncResult {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private PetAdapters petsUser;
    private ArrayList<PetModel> pets;
    private View MyView;
    private OnFragmentInteractionListener mListener;
    private ListView lista;

    public MyPetsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPetsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPetsFragment newInstance(String param1, String param2) {
        MyPetsFragment fragment = new MyPetsFragment();
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


        View MyView =  inflater.inflate(R.layout.fragment_pets, container, false);

        // Set tittle to the fragment
        getActivity().setTitle("Mis mascotas");




        Toolbar toolbar = (Toolbar) MyView.findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        pets = new ArrayList<>();
        // pets.add(new PetModel("idPet", "nom"));
        //pets.add(new PetModel("idPet2", "nom2"));
        //TODO: pets comes from DB

        SingletonUsuario su = SingletonUsuario.getInstance();
        String us = su.getEmail();



        Conexion con = new Conexion(this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/getALLPetsByUser/" + us,"GET", null);
            lista = MyView.findViewById(R.id.list_pets);
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PetModel petmodel = pets.get(position);
                    Intent intent = new Intent(getActivity(), PetInfo.class);
                    intent.putExtra("pet", petmodel);
                    startActivity(intent);
                }
            });
        FloatingActionButton fab = MyView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewPet.class);
                startActivity(intent);
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

    @Override
    public void OnprocessFinish(JSONObject json) {
        try {
            if (json.getInt("code") == 200) {
                pets = new ArrayList<PetModel>();
                JSONArray jsonArray = json.getJSONArray("array");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectHijo = jsonArray.getJSONObject(i);
                    PetModel petModel = new PetModel();

                    petModel.setId(jsonObjectHijo.getString("id"));
                    petModel.setNombre(jsonObjectHijo.getString("nombre"));
                    petModel.setEspecie(jsonObjectHijo.getString("especie"));
                    petModel.setRaza(jsonObjectHijo.getString("raza"));
                    petModel.setSexo(jsonObjectHijo.getString("sexo"));
                    petModel.setDescripcion(jsonObjectHijo.getString("descripcion"));
                    petModel.setEdad(jsonObjectHijo.getString("edad"));
                    petModel.setColor(jsonObjectHijo.getString("color"));

                    pets.add(petModel);

                    petsUser = new PetAdapters(getActivity(), pets);
                    lista = (ListView) MyView.findViewById(R.id.list_pets);
                    lista.setAdapter(petsUser);
                    System.out.print(json.getInt("code") + " se muestran correctamente la lista de pets\n");


                }
            }
            else{
                System.out.print("El sistema no logra mostrar la lista de pets del creador\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


