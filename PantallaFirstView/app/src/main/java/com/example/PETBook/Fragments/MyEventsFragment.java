package com.example.PETBook.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.PETBook.Adapters.EventAdapter;
import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.EventInfo;
import com.example.PETBook.MainActivity;
import com.example.PETBook.Models.EventModel;
import com.example.PETBook.NewEvent;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyEventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyEventsFragment extends Fragment implements AsyncResult {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private View MyView;
    private ListView lista;
    private EventAdapter eventosUser;
    private ArrayList<EventModel> participante;
    private ArrayList<EventModel> creador;
    private Button creator;
    private Button participant;
    private Conexion con;
    private OnFragmentInteractionListener mListener;
    private String TypeList;

    public MyEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyEventsFragment newInstance(String param1, String param2) {
        MyEventsFragment fragment = new MyEventsFragment();
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

        MyView =  inflater.inflate(R.layout.activity_my_events, container, false);

        // Set tittle to the fragment
        getActivity().setTitle("My events");

        SingletonUsuario usuario = SingletonUsuario.getInstance();

        if (!usuario.isMailConfirmed()) {

            /*
            Show dialog to inform user that does not have email confirmed
             */
            AlertDialog.Builder emailConfirmedDialog = new AlertDialog.Builder(getActivity());
            emailConfirmedDialog.setMessage("Confirm your email to access all the functions of PetBook")
                    .setCancelable(true)
                    .setPositiveButton("Resend email confirmation", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            /*
                            sends confirmation mail to the user's email
                             */
                            SingletonUsuario user = SingletonUsuario.getInstance();
                            Conexion conexion = new Conexion(MyEventsFragment.this);
                            conexion.execute("http://10.4.41.146:9999/ServerRESTAPI/SendConfirmationEmail", "POST", null);
                            dialog.cancel();
                            Intent i = new Intent(getActivity(), MainActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                            Intent i = new Intent(getActivity(), MainActivity.class);
                            startActivity(i);
                            getActivity().finish();

                        }
                    });
            AlertDialog dialog = emailConfirmedDialog.create();
            dialog.setTitle("Email confirmation");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }





        con = new Conexion(MyEventsFragment.this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/events/GetEventsByParticipant?mail=" + SingletonUsuario.getInstance().getEmail(),"GET", null);

        lista = MyView.findViewById(R.id.list_eventos);

        creator = (Button) MyView.findViewById(R.id.CreatorEvents);
        creator.setBackgroundColor(Color.RED);
        creator.setTextColor(Color.WHITE);
        participant = (Button) MyView.findViewById(R.id.ParticipantEvents);

        creator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                creator.setBackgroundColor(Color.RED);
                creator.setTextColor(Color.WHITE);
                participant.setBackgroundColor(Color.GRAY);
                participant.setTextColor(Color.BLACK);
                eventosUser = new EventAdapter(getActivity(), creador);
                lista.setAdapter(eventosUser);
                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        EventModel eventoSeleccionado = creador.get(position);
                        Intent intent = new Intent(getActivity(), EventInfo.class);
                        intent.putExtra("event", eventoSeleccionado);
                        startActivity(intent);
                    }
                });
            }
        });
        participant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                creator.setBackgroundColor(Color.GRAY);
                creator.setTextColor(Color.BLACK);
                participant.setBackgroundColor(Color.RED);
                participant.setTextColor(Color.WHITE);
                eventosUser = new EventAdapter(getActivity(), participante);
                lista.setAdapter(eventosUser);
                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        EventModel eventoSeleccionado = participante.get(position);
                        Intent intent = new Intent(getActivity(), EventInfo.class);
                        intent.putExtra("event", eventoSeleccionado);
                        startActivity(intent);
                    }
                });
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventModel eventoSeleccionado = participante.get(position);
                Intent intent = new Intent(getActivity(), EventInfo.class);
                intent.putExtra("event", eventoSeleccionado);
                startActivity(intent);
            }
        });


        FloatingActionButton fab = MyView.findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewEvent.class);
                startActivity(intent);
            }
        });

        return MyView;
    }


    private String transformacionFechaHora(String fechaHora){
        Integer fin = 0;
        String result = fechaHora.replace("T", " ");
        result = result.replace(":00.000+0000", " ");
        return result;
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
        try{
            if(json.getInt("code") == 200){
                creador = new ArrayList<>();
                participante = new ArrayList<>();
                JSONArray jsonArray = json.getJSONArray("array");
                for(int i = 0; i < jsonArray.length(); ++i){
                    JSONObject evento = jsonArray.getJSONObject(i);
                    EventModel e = new EventModel();
                    e.setId(evento.getInt("id"));
                    e.setTitulo(evento.getString("title"));
                    e.setDescripcion(evento.getString("description"));
                    e.setFecha(transformacionFechaHora(evento.getString("date")));
                    JSONObject loc = evento.getJSONObject("localization");
                    e.setDireccion(loc.getString("address"));
                    e.setCoordenadas(loc.getDouble("longitude"),loc.getDouble("latitude"));
                    e.setPublico(evento.getBoolean("public"));
                    JSONArray m = evento.getJSONArray("participants");
                    ArrayList<String> miembros = new ArrayList<String>();
                    for(int j = 0; j < m.length(); ++j){
                        miembros.add(m.getString(j));
                    }
                    e.setMiembros(miembros);
                    e.setCreador(evento.getString("creatorMail"));
                    if(SingletonUsuario.getInstance().getEmail().equals(e.getCreador())){
                        creador.add(e);
                    }
                    else {
                        participante.add(e);
                    }
                }
                eventosUser = new EventAdapter(getActivity(), creador);
                lista = (ListView) MyView.findViewById(R.id.list_eventos);
                lista.setAdapter(eventosUser);
                System.out.print(json.getInt("code") + " se muestran correctamente la lista de eventos\n");
            }
            else{
                System.out.print("El sistema no logra mostrar la lista de eventos del creador\n");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }




}
