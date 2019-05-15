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
import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.FriendModel;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyFriendsAcceptedFragment extends Fragment implements AsyncResult {

    /* ATRIBUTOS   */
    private View convertView;
    private ArrayList<FriendModel> model;
    private ListView lista;
    private FriendAdapter friendsUser;


    public MyFriendsAcceptedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_my_friends_accepted, container, false);
        }

        getActivity().setTitle("Friends");

        Conexion con = new Conexion(MyFriendsAcceptedFragment.this);
        SingletonUsuario su = SingletonUsuario.getInstance();

        con.execute("http://10.4.41.146:9999/ServerRESTAPI/GetUserFriends/" + su.getEmail(),"GET", null);

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
                lista = (ListView) convertView.findViewById(R.id.list_friends_accepted);
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
