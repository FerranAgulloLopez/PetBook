package com.example.PETBook.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Fragments.MyFriendsFragment;
import com.example.PETBook.Models.Image;
import com.example.PETBook.SingletonUsuario;
import com.example.PETBook.Models.FriendRequestModel;
import com.example.pantallafirstview.R;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestAdapter extends BaseAdapter implements AsyncResult {

    private Context context;
    private ArrayList<FriendRequestModel> user_friends_requests;
    private String tipoConexion;
    private ImageView imageProfile;
    FriendRequestModel friendRequest;

    public FriendRequestAdapter(Context context, ArrayList<FriendRequestModel> array){
        this.context = context;
        user_friends_requests = array;
    }

    @Override
    public int getCount() {
        return user_friends_requests.size();
    }

    @Override
    public Object getItem(int position) {
        return user_friends_requests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.friend_request_design,null);
        }

        final FriendRequestModel friend = user_friends_requests.get(position);


        TextView inputFullName = (TextView) convertView.findViewById(R.id.fullNameInput);
        inputFullName.setText(friend.getName() + " " + friend.getSurnames());
        imageProfile = (CircleImageView) convertView.findViewById(R.id.imageView);
        tipoConexion = "imageFriend";
        Conexion con = new Conexion(this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/getPicture/" + friend.getEmail(), "GET", null);
        inputFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FriendRequestAdapter.this.context, "ver perfil amigoo", Toast.LENGTH_SHORT).show();

                /*Intent intent = new Intent(v.getContext(), WallInfo.class);
                intent.putExtra("friendRequestSelected", friend);
                v.getContext().startActivity(intent);*/
            }
        });

        //ImageView imageProfile = (ImageView) convertView.findViewById(R.id.imageView);
/*
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FriendRequestAdapter.this.context, "ver perfil amigoo", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(v.getContext(), WallInfo.class);
                intent.putExtra("friendRequestSelected", friend);
                v.getContext().startActivity(intent);
            }
        });*/



        Button confirmButton = (Button) convertView.findViewById(R.id.confirmButton);
        Button deleteButton = (Button) convertView.findViewById(R.id.deleteButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendRequest = friend;
                tipoConexion = "acceptRequest";
                SingletonUsuario su = SingletonUsuario.getInstance();
                /* Nueva conexion llamando a la funcion del server */
                Conexion con = new Conexion(FriendRequestAdapter.this);
                con.execute("http://10.4.41.146:9999/ServerRESTAPI/acceptFriendRequest/" + friend.getEmail(), "POST", null);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder error = new AlertDialog.Builder(FriendRequestAdapter.this.context);
                error.setMessage("Are you sure you want to delete the friend request?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                friendRequest = friend;
                                tipoConexion = "denyRequest";
                                SingletonUsuario su = SingletonUsuario.getInstance();
                                /* Nueva conexion llamando a la funcion del server */
                                Conexion con = new Conexion(FriendRequestAdapter.this);
                                con.execute("http://10.4.41.146:9999/ServerRESTAPI/denyFriendRequest/" + friend.getEmail(), "POST", null);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog errorE = error.create();
                errorE.setTitle("Deny Friend Request");
                errorE.show();
            }
        });

        return convertView;
    }

    @Override
    public void OnprocessFinish(JSONObject output) {
        if (output != null) {
            if(tipoConexion.equals("acceptRequest")) {
                try {
                    int response = output.getInt("code");
                    if (response == 200) {
                        user_friends_requests.remove(friendRequest);
                        FriendRequestAdapter.this.notifyDataSetChanged();
                        AppCompatActivity activity = (AppCompatActivity) this.context;
                        Fragment fragment = new MyFriendsFragment();
                        Bundle args = new Bundle();
                        args.putInt("index_tl",2);
                        fragment.setArguments(args);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
                        Toast.makeText(this.context, "Friend request accepted successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this.context, "There was a problem during the process.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if(tipoConexion.equals("denyRequest")) {
                try {
                    int response = output.getInt("code");
                    if (response == 200) {
                        user_friends_requests.remove(friendRequest);
                        FriendRequestAdapter.this.notifyDataSetChanged();

                        Toast.makeText(this.context, "Friend request rejected successfully.", Toast.LENGTH_SHORT).show();
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