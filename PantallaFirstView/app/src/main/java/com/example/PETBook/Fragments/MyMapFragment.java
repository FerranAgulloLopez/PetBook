package com.example.PETBook.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.EventModel;
import com.example.pantallafirstview.BuildConfig;
import com.example.pantallafirstview.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

public class MyMapFragment extends Fragment implements AsyncResult {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /* ATRIBUTOS   */
    private View MyView;

    private MapView myOpenMapView;
    private MapController myMapController;
    private ArrayList<EventModel> AllEvents;

    public MyMapFragment() {
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
        GeoPoint madrid = new GeoPoint(40.416775,-3.70379);

        myOpenMapView = (MapView) MyView.findViewById(R.id.openmapview);
        myOpenMapView.setBuiltInZoomControls(true);
        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setCenter(madrid);
        myMapController.setZoom(6);

        myOpenMapView.setMultiTouchControls(true);

        OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);
        return MyView;
    }

    private String transformacionFechaHora(String fechaHora){
        Integer fin = 0;
        String result = fechaHora.replace("T", " ");
        result = result.replace(":00.000+0000", " ");
        return result;
    }

    @Override
    public void OnprocessFinish(JSONObject output){
        if (output != null){
            try{
                int response = output.getInt("code");
                if (response == 200){
                    AllEvents = new ArrayList<>();
                    JSONArray jsonArray = output.getJSONArray("array");
                    for(int i = 0; i < jsonArray.length(); ++i){
                        JSONObject evento = jsonArray.getJSONObject(i);
                        EventModel e = new EventModel();
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
                        AllEvents.add(e);
                    }
                }
            } catch (Exception e){
                    e.printStackTrace();
            }
        }

    }
}
