package com.example.PETBook.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.FriendRequestModel;
import com.example.PETBook.Models.FriendSuggestionModel;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendSuggestionAdapter extends BaseAdapter implements AsyncResult {

    private Context context;
    private ArrayList<FriendSuggestionModel> user_friends_suggestions;
    private String tipoConexion;

    FriendSuggestionModel friendSuggestion;

    public FriendSuggestionAdapter(Context context, ArrayList<FriendSuggestionModel> array){
        this.context = context;
        user_friends_suggestions = array;
    }

    @Override
    public int getCount() {
        return user_friends_suggestions.size();
    }

    @Override
    public Object getItem(int position) {
        return user_friends_suggestions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.friend_suggestion_design,null);
        }

        final FriendSuggestionModel friend = user_friends_suggestions.get(position);

        TextView inputFullName = (TextView) convertView.findViewById(R.id.fullNameInput);

        inputFullName.setText(user_friends_suggestions.get(position).getName() +" " + user_friends_suggestions.get(position).getSurnames());

        Button addButton = (Button) convertView.findViewById(R.id.addButton);
        Button removeButton = (Button) convertView.findViewById(R.id.removeButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendSuggestion = friend;
                tipoConexion = "sendRequest";
                SingletonUsuario su = SingletonUsuario.getInstance();
                /* Nueva conexion llamando a la funcion del server */
                Conexion con = new Conexion(FriendSuggestionAdapter.this);
                con.execute("http://10.4.41.146:9999/ServerRESTAPI/sendFriendRequest/" + su.getEmail() + "/" + friend.getEmail(), "POST", null);
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder error = new AlertDialog.Builder(FriendSuggestionAdapter.this.context);
                error.setMessage("Are you sure you want to delete the friend Suggestion?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                friendSuggestion = friend;
                                tipoConexion = "deleteSuggestion";
                                SingletonUsuario su = SingletonUsuario.getInstance();
                                /* Nueva conexion llamando a la funcion del server */
                                Conexion con = new Conexion(FriendSuggestionAdapter.this);
                                con.execute("http://10.4.41.146:9999/ServerRESTAPI/deleteFriendSuggestion/" + su.getEmail() + "/" + friend.getEmail(), "POST", null);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog errorE = error.create();
                errorE.setTitle("Delete Friend Suggestion");
                errorE.show();





            }
        });

        return convertView;
    }

    public void OnprocessFinish(JSONObject output) {
        if (output != null) {
            if(tipoConexion.equals("sendRequest")) {
                try {
                    int response = output.getInt("code");
                    if (response == 200) {
                        user_friends_suggestions.remove(friendSuggestion);
                        FriendSuggestionAdapter.this.notifyDataSetChanged();
                        Toast.makeText(this.context, "Friend request sent successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this.context, "There was a problem during the process.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if(tipoConexion.equals("deleteSuggestion")) {
                try {
                    int response = output.getInt("code");
                    if (response == 200) {
                        user_friends_suggestions.remove(friendSuggestion);
                        FriendSuggestionAdapter.this.notifyDataSetChanged();

                        Toast.makeText(this.context, "Suggested user deleted successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this.context, "There was a problem during the process.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else {

            Toast toast = Toast.makeText(this.context, "The server does not work.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

}