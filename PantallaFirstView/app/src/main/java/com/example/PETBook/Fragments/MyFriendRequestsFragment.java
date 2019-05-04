package com.example.PETBook.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.PETBook.Adapters.FriendRequestAdapter;
import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.FriendRequestModel;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyFriendRequestsFragment extends Fragment implements AsyncResult {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /* ATRIBUTOS   */
    private View MyView;
    private TextView textViewMyfriends;
    private TextView textViewNotifications;
    private ListView lista;
    private FriendRequestAdapter friendsRequestsUser;
    private ArrayList<FriendRequestModel> model;

    public MyFriendRequestsFragment() {
        // Required empty public constructor
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
        MyView =  inflater.inflate(R.layout.fragment_my_friend_requests, container, false);
        // Set tittle to the fragment
        getActivity().setTitle("REQUESTS");
        textViewMyfriends      = (TextView) MyView.findViewById(R.id.myfriendsTextView);
        textViewNotifications  = (TextView) MyView.findViewById(R.id.notificationsTextView);

        Conexion con = new Conexion(MyFriendRequestsFragment.this);
        SingletonUsuario su = SingletonUsuario.getInstance();

        con.execute("http://10.4.41.146:9999/ServerRESTAPI/GetUserFriendsRequests/" + su.getEmail(),"GET", null);


        return MyView;
    }

    @Override
    public void OnprocessFinish(JSONObject json) {
        try{
            if(json.getInt("code") == 200){
                model = new ArrayList<>();
                JSONArray jsonArray = json.getJSONArray("array");
                for(int i = 0; i < jsonArray.length(); ++i){
                    JSONObject evento = jsonArray.getJSONObject(i);
                    FriendRequestModel e = new FriendRequestModel();
                    e.setName(evento.getString("firstName"));
                    e.setSurnames(evento.getString("secondName"));
                    model.add(e);
                }
                friendsRequestsUser = new FriendRequestAdapter(getActivity(), model);
                lista = (ListView) MyView.findViewById(R.id.list_friends_requests);
                lista.setAdapter(friendsRequestsUser);
                System.out.print(json.getInt("code") + " se muestran correctamente la lista de solicitudes de amigos\n");
            }
            else{
                System.out.print("El sistema no logra mostrar la lista de solicitudes de amigos del usuario\n");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
