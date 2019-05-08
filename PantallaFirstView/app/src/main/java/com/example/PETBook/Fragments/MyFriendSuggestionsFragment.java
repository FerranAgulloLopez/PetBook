package com.example.PETBook.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.PETBook.Adapters.FriendAdapter;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.FriendModel;
import com.example.PETBook.Conexion;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;


public class MyFriendSuggestionsFragment extends Fragment implements AsyncResult {

    private View convertView;
    private ArrayList<FriendModel> model;
    private ListView lista;
    private FriendAdapter friendsUser;

    public MyFriendSuggestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_my_friend_suggestions, container, false);
        }

        getActivity().setTitle("SUGGESTIONS");
        Conexion con = new Conexion(MyFriendSuggestionsFragment.this);
        SingletonUsuario su = SingletonUsuario.getInstance();

        con.execute("http://10.4.41.146:9999/ServerRESTAPI/GetUsersFriendSuggestion/" + su.getEmail(),"GET", null);
        return convertView;
    }

    @Override
    public void OnprocessFinish(JSONObject json) {

        try{
            if(json.getInt("code") == 200){
                model = new ArrayList<>();
                JSONArray jsonArray = json.getJSONArray("array");
                for(int i = 0; i < jsonArray.length(); ++i){
                    JSONObject friend = jsonArray.getJSONObject(i);
                    FriendModel e = new FriendModel();
                    e.setName(friend.getString("firstName"));
                    e.setSurnames(friend.getString("secondName"));
                    e.setEmail (friend.getString("email"));
                    model.add(e);
                }
                friendsUser = new FriendAdapter(getActivity(), model);
                lista = (ListView) convertView.findViewById(R.id.list_friends1);
                lista.setAdapter(friendsUser);
                System.out.print(json.getInt("code") + " se muestran correctamente la lista de amigos\n");
            }
            else{
                System.out.print("El sistema no logra mostrar la lista de amigos del usuario\n");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
