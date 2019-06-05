package com.example.PETBook.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.MainActivity;
import com.example.PETBook.Models.FriendRequestModel;
import com.example.PETBook.Models.FriendSuggestionModel;
import com.example.PETBook.Models.Image;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendSuggestionAdapter extends BaseAdapter implements AsyncResult {

    private Context context;
    private ArrayList<FriendSuggestionModel> user_friends_suggestions;
    private String tipoConexion;
    private ImageView imageProfile;
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
        inputFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(FriendRequestAdapter.this.context, "ver perfil amigoo", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("fragment", "myprofile");
                intent.putExtra("nameProfile", friend.getEmail());
                context.startActivity(intent);
            }
        });

        imageProfile = (CircleImageView) convertView.findViewById(R.id.imageView);
        Button addButton = (Button) convertView.findViewById(R.id.addButton);
        Button removeButton = (Button) convertView.findViewById(R.id.removeButton);
        tipoConexion = "imageFriend";
        Conexion con = new Conexion(this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/getPicture/" + friend.getEmail(), "GET", null);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendSuggestion = friend;
                tipoConexion = "sendRequest";
                SingletonUsuario su = SingletonUsuario.getInstance();
                /* Nueva conexion llamando a la funcion del server */
                Conexion con = new Conexion(FriendSuggestionAdapter.this);
                con.execute("http://10.4.41.146:9999/ServerRESTAPI/sendFriendRequest/" + friend.getEmail(), "POST", null);
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
                    System.out.println("codigo1: " + response);
                    if (response == 200) {
                        user_friends_suggestions.remove(friendSuggestion);
                        FriendSuggestionAdapter.this.notifyDataSetChanged();
                        Toast.makeText(this.context, "Friend request sent successfully.", Toast.LENGTH_SHORT).show();
                        System.out.println("codigo2: " + response);
                    }
                    else {
                        Toast.makeText(this.context, "There was a problem during the process", Toast.LENGTH_SHORT).show();
                        System.out.println("codigo5: " + response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        System.out.println("codigo: " + output.getInt("code"));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
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

            } else if(tipoConexion.equals("imageFriend")) {
                try {
                    int response = output.getInt("code");
                    if (response == 200) {
                        // convert string to bitmap
                        SingletonUsuario user = SingletonUsuario.getInstance();
                        Image imagenConversor = Image.getInstance();
                        String image = output.getString("image");
                        Bitmap profileImage = imagenConversor.StringToBitMap(image);
                        imageProfile.setImageBitmap(profileImage);
                        //user.setProfilePicture(profileImage);
                    }   else if (output.getInt("code")==404) { // user does not have profile picture
                        imageProfile.setImageResource(R.drawable.troymcclure);
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