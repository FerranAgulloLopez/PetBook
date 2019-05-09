package com.example.PETBook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.FriendModel;
import com.example.PETBook.Models.FriendSuggestionModel;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendAdapter extends BaseAdapter implements AsyncResult {

    private Context context;
    private ArrayList<FriendModel> user_friends;

    FriendModel friendAccepted;

    public FriendAdapter(Context context, ArrayList<FriendModel> array){
        this.context = context;
        user_friends = array;
    }

    @Override
    public int getCount() {
        return user_friends.size();
    }

    @Override
    public Object getItem(int position) {
        return user_friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.friend_design,null);
        }

        final FriendModel friend = user_friends.get(position);

        TextView inputFullName = (TextView) convertView.findViewById(R.id.fullNameInput);

        inputFullName.setText(user_friends.get(position).getName() +" " + user_friends.get(position).getSurnames());

        Button deleteButton = (Button) convertView.findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendAccepted = friend;
                SingletonUsuario su = SingletonUsuario.getInstance();
                /* Nueva conexion llamando a la funcion del server */
                Conexion con = new Conexion(FriendAdapter.this);
                con.execute("http://10.4.41.146:9999/ServerRESTAPI/Unfriend/" + su.getEmail() + "/" + friend.getEmail(), "POST", null);

            }
        });
        return convertView;
    }

    public void OnprocessFinish(JSONObject output) {
        if (output != null) {
            try {
                int response = output.getInt("code");
                if (response == 200) {
                    user_friends.remove(friendAccepted);
                    FriendAdapter.this.notifyDataSetChanged();
                    Toast.makeText(this.context, "Friend removed successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this.context, "There was a problem during the process.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast toast = Toast.makeText(this.context, "The server does not work.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

}