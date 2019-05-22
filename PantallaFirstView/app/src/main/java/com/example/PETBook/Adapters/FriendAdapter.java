package com.example.PETBook.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Chat;
import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Fragments.MyFriendsFragment;
import com.example.PETBook.Models.FriendModel;
import com.example.PETBook.Models.FriendSuggestionModel;
import com.example.PETBook.Models.Image;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapter extends BaseAdapter implements AsyncResult {

    private Context context;
    private ArrayList<FriendModel> user_friends;
    private String tipoConexion;
    private ImageView imageProfile;
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
        imageProfile = (CircleImageView) convertView.findViewById(R.id.imageView);
        Button deleteButton = (Button) convertView.findViewById(R.id.deleteButton);
        tipoConexion = "imageFriend";
        Conexion con = new Conexion(this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/getPicture/" + friend.getEmail(), "GET", null);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder error = new AlertDialog.Builder(FriendAdapter.this.context);
                error.setMessage("Are you sure you want to remove the friend?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                friendAccepted = friend;
                                tipoConexion = "unfriend";
                                SingletonUsuario su = SingletonUsuario.getInstance();
                                /* Nueva conexion llamando a la funcion del server */
                                Conexion con = new Conexion(FriendAdapter.this);
                                con.execute("http://10.4.41.146:9999/ServerRESTAPI/Unfriend/" + friend.getEmail(), "POST", null);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog errorE = error.create();
                errorE.setTitle("Remove Friend");
                errorE.show();
            }
        });

        Button chatButton = (Button) convertView.findViewById(R.id.button2);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("emailReceptor", friend.getEmail());
                intent.putExtra("nameReceptor", friend.getName());
                context.startActivity(intent);
            }
        });




        return convertView;
    }

    public void OnprocessFinish(JSONObject output) {
        if (output != null) {
            if(tipoConexion.equals("unfriend")) {
                try {
                    int response = output.getInt("code");
                    if (response == 200) {
                        user_friends.remove(friendAccepted);
                        FriendAdapter.this.notifyDataSetChanged();
                        AppCompatActivity activity = (AppCompatActivity) this.context;
                        Fragment fragment = new MyFriendsFragment();
                        Bundle args = new Bundle();
                        args.putInt("index_tl",1);
                        fragment.setArguments(args);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
                        Toast.makeText(this.context, "Friend removed successfully.", Toast.LENGTH_SHORT).show();
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
                        user.setProfilePicture(profileImage);
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