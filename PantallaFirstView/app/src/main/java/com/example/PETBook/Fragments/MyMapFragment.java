package com.example.PETBook.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.EventInfo;
import com.example.PETBook.MainActivity;
import com.example.PETBook.Models.EventModel;
import com.example.PETBook.Models.InterestSiteModel;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

public class MyMapFragment extends Fragment implements OnMapReadyCallback, AsyncResult {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /* ATRIBUTOS   */
    private View MyView;

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private LatLng madrid = new LatLng(40.4893538, -3.6827461);
    private MapView myOpenMapView;
    private MapController myMapController;
    private ArrayList<EventModel> AllEvents;
    private ArrayList<InterestSiteModel> AllInterestSites;
    private static SingletonUsuario su = SingletonUsuario.getInstance();
    private String typeConnection;

    public MyMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyMapFragment newInstance(String param1, String param2) {
        MyMapFragment fragment = new MyMapFragment();
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
        MyView = inflater.inflate(R.layout.activity_map2, container, false);
        // Set tittle to the fragment
        getActivity().setTitle("Mapa");
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        typeConnection = "Events";
        Conexion con = new Conexion(MyMapFragment.this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/events/GetAllEvents", "GET", null);
        mapFragment.getMapAsync(this);
        return MyView;
    }

    private String transformacionFechaHora(String fechaHora) {
        Integer fin = 0;
        String result = fechaHora.replace("T", " ");
        result = result.replace(":00.000+0000", " ");
        return result;
    }


    @Override
    public void OnprocessFinish(JSONObject output) {
        if (output != null) {
            if (typeConnection.equals("Events")){
                try {
                    int response = output.getInt("code");
                    if (response == 200) {
                        AllEvents = new ArrayList<>();
                        JSONArray jsonArray = output.getJSONArray("array");
                        for (int i = 0; i < jsonArray.length(); ++i) {
                            JSONObject evento = jsonArray.getJSONObject(i);
                            EventModel e = new EventModel();
                            e.setId(evento.getInt("id"));
                            e.setTitulo(evento.getString("title"));
                            e.setDescripcion(evento.getString("description"));
                            e.setFecha(transformacionFechaHora(evento.getString("date")));
                            JSONObject loc = evento.getJSONObject("localization");
                            e.setDireccion(loc.getString("address"));
                            e.setCoordenadas(loc.getDouble("longitude"), loc.getDouble("latitude"));
                            e.setPublico(evento.getBoolean("public"));
                            JSONArray m = evento.getJSONArray("participants");
                            ArrayList<String> miembros = new ArrayList<String>();
                            for (int j = 0; j < m.length(); ++j) {
                                miembros.add(m.getString(j));
                            }
                            e.setMiembros(miembros);
                            e.setCreador(evento.getString("creatorMail"));
                            AllEvents.add(e);
                            LatLng pos = new LatLng(e.getLatitude(), e.getLongitude());
                            Marker markEvent = mMap.addMarker(new MarkerOptions().position(pos));
                            markEvent.setTag(i + " Event");
                        }
                        typeConnection = "Interest Sites";
                        Conexion con = new Conexion(MyMapFragment.this);
                        con.execute("http://10.4.41.146:9999/ServerRESTAPI/interestSites?accepted=true", "GET", null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                try{
                    int response = output.getInt("code");
                    if (response == 200){
                        AllInterestSites = new ArrayList<>();
                        JSONArray jsonArray = output.getJSONArray("array");
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject is = jsonArray.getJSONObject(i);
                            InterestSiteModel site = new InterestSiteModel();
                            site.setId(is.getInt("id"));
                            site.setTitulo(is.getString("name"));
                            site.setDescripcion(is.getString("description"));
                            site.setTipo(is.getString("type"));
                            JSONObject loc = is.getJSONObject("localization");
                            site.setDireccion(loc.getString("address"));
                            site.setLatitude(loc.getDouble("latitude"));
                            site.setLongitude(loc.getDouble("longitude"));
                            AllInterestSites.add(site);
                            LatLng pos = new LatLng(site.getLatitude(), site.getLongitude());
                            Marker markInterest = mMap.addMarker(new MarkerOptions().position(pos));
                            markInterest.setTag(i + " Interest");
                            markInterest.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_park));
                            markInterest.setAnchor(0.0f,1.0f);
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(madrid));

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View popup;
                String[] s = marker.getTag().toString().split(" ");
                if(s[1].equals("Interest")){
                    popup = getLayoutInflater().inflate(R.layout.interestsite_window_info, null);
                }
                else{
                    popup = getLayoutInflater().inflate(R.layout.event_window_info, null);
                }

                if(s[1].equals("Interest")) {
                    InterestSiteModel is = AllInterestSites.get(Integer.parseInt(s[0]));
                    TextView name = (TextView) popup.findViewById(R.id.NameInterestInfo);
                    TextView type = (TextView) popup.findViewById(R.id.TypeInterestInfo);
                    TextView desc = (TextView) popup.findViewById(R.id.descInterestInfo);
                    TextView loc = (TextView) popup.findViewById(R.id.LocInterestInfo);
                    name.setText(is.getTitulo());
                    type.setText("Tipo: " + is.getTipo());
                    desc.setText("Descripción: \n" + is.getDescripcion());
                    loc.setText(is.getDireccion());
                }
                else {
                    EventModel e = AllEvents.get(Integer.parseInt(s[0]));
                    TextView title = (TextView) popup.findViewById(R.id.titleInfoEvent);
                    TextView miembros = (TextView) popup.findViewById(R.id.partInfoEvent);
                    TextView fecha = (TextView) popup.findViewById(R.id.fechaInfoEvent);
                    title.setText(e.getTitulo());
                    miembros.setText("Participantes: " + String.valueOf(e.getNumeroParticipantes()));
                    fecha.setText(e.getFecha());
                }
                return popup;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View popup;
                String[] s = marker.getTag().toString().split(" ");
                if(s[1].equals("Interest")){
                    popup = getLayoutInflater().inflate(R.layout.interestsite_window_info, null);
                }
                else{
                    popup = getLayoutInflater().inflate(R.layout.event_window_info, null);
                }

                if(s[1].equals("Interest")) {
                    InterestSiteModel is = AllInterestSites.get(Integer.parseInt(s[0]));
                    TextView name = (TextView) popup.findViewById(R.id.NameInterestInfo);
                    TextView type = (TextView) popup.findViewById(R.id.TypeInterestInfo);
                    TextView desc = (TextView) popup.findViewById(R.id.descInterestInfo);
                    TextView loc = (TextView) popup.findViewById(R.id.LocInterestInfo);
                    name.setText(is.getTitulo());
                    type.setText("Tipo: " + is.getTipo());
                    desc.setText("Descripción: \n" + is.getDescripcion());
                    loc.setText(is.getDireccion());
                }
                else {
                    EventModel e = AllEvents.get(Integer.parseInt(s[0]));
                    TextView title = (TextView) popup.findViewById(R.id.titleInfoEvent);
                    TextView miembros = (TextView) popup.findViewById(R.id.partInfoEvent);
                    TextView fecha = (TextView) popup.findViewById(R.id.fechaInfoEvent);
                    title.setText(e.getTitulo());
                    miembros.setText("Participantes: " + String.valueOf(e.getNumeroParticipantes()));
                    fecha.setText(e.getFecha());
                }
                return popup;
            }

        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String[] s = marker.getTag().toString().split(" ");
                if(s[1].equals("Event")){
                    EventModel event = AllEvents.get(Integer.parseInt(s[0]));
                    Intent intent = new Intent(getActivity(), EventInfo.class);
                    intent.putExtra("event", event);
                    if (event.getCreador().equals(su.getEmail())){
                        intent.putExtra("eventType", "Creator");
                    }
                    else{
                        intent.putExtra("eventType", "Participant");
                    }
                    startActivity(intent);
                }
            }
        });

    }
}
