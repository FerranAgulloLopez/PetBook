package com.example.PETBook.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.EditProfile;
import com.example.PETBook.EditWall;
import com.example.PETBook.Fragments.HomeWallFragment;
import com.example.PETBook.MainActivity;
import com.example.PETBook.Models.InterestSiteModel;
import com.example.PETBook.Models.WallModel;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WallAdapter extends BaseAdapter implements AsyncResult {

    private Context context;
    private ArrayList<WallModel> wallList;
    private TextView idComment;
    private String tipoConexion;

    public WallAdapter (Context context, ArrayList<WallModel> array){
        this.context = context;
        wallList = array;
    }

    @Override
    public int getCount() {
        return wallList.size();
    }

    @Override
    public Object getItem(int position) {
        return wallList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.wall_design,null);
        }
        /*final InterestSiteModel is = interestSites.get(position);

        TextView title = (TextView) convertView.findViewById(R.id.TitleSite);
        title.setText(is.getTitulo());
        TextView type = (TextView) convertView.findViewById(R.id.TypeSite);
        type.setText(is.getTipo());
        TextView location = (TextView) convertView.findViewById(R.id.LocationSite);
        location.setText(is.getDireccion());
        final Button vote = (Button) convertView.findViewById(R.id.VoteButton);
        if (is.getVotantes().isEmpty() || !is.getVotantes().contains(SingletonUsuario.getInstance().getEmail())) {
            hacerVotar(vote, is.getId());
        }
        else {
            hacerUnvote(vote, is.getId());
        }*/
        final WallModel w = wallList.get(position);
        TextView descriptionWall = (TextView) convertView.findViewById(R.id.descriptionWall);
        TextView dataCreacioWall = (TextView) convertView.findViewById(R.id.dataCreacionWall);
        idComment = (TextView) convertView.findViewById(R.id.idComment);
        TextView numlikes = convertView.findViewById(R.id.numLikes);
        TextView numFavs = convertView.findViewById(R.id.numFavs);
        TextView numRetweets = convertView.findViewById(R.id.numRetweets);
        final ImageButton option = convertView.findViewById(R.id.optionButton);

        final String[] opcions = {"Edit", "Delete"};
        final View finalConvertView = convertView;
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(finalConvertView.getContext());
                // System.out.println("idcomment:" + builder.getContext());
                builder.setItems(opcions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]

                        //System.out.println("idComment: " + ViewIDComment.getText().toString());
                        System.out.println("numero which:" + which);
                        if(which == 0){
                            editComment();
                        }
                        else if(which == 1){
                            deletePost();
                        }
                    }
                });
                builder.show();
            }
        });

        idComment.setText(wallList.get(position).getIDWall().toString());
        descriptionWall.setText(wallList.get(position).getDescription());

        System.out.println("numlikes: " + String.valueOf(wallList.get(position).getLikes().size()));
        System.out.println("numFavs: " + String.valueOf(wallList.get(position).getFavs().size()));
        System.out.println("numRetweets: " + String.valueOf(wallList.get(position).getRetweets().size()));
        numlikes.setText(String.valueOf(wallList.get(position).getLikes().size()));
        numFavs.setText(String.valueOf(wallList.get(position).getFavs().size()));
        numRetweets.setText(String.valueOf(wallList.get(position).getRetweets().size()));
        final ImageButton like = convertView.findViewById(R.id.likeButton);
        final ImageButton fav = convertView.findViewById(R.id.favButton);
        final ImageButton retweet = convertView.findViewById(R.id.retweetButton);

        if (w.getLikes().isEmpty()|| !w.getLikes().contains(SingletonUsuario.getInstance().getEmail())) {
            like(like, w.getIDWall());
        }
        else {
            unlike(like, w.getIDWall());
        }

        String fechaString = wallList.get(position).getCreationDate();
        Date dateNew = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        try {
            dateNew = format.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PrettyTime prettyTime = new PrettyTime(new Date(), Locale.getDefault());
        dataCreacioWall.setText(prettyTime.format(dateNew));

        return convertView;
    }
    private void editComment(){
        Bundle bundle = new Bundle();
        bundle.putString("id", idComment.getText().toString());

        Intent intent = new Intent(this.context, EditWall.class);
        intent.putExtra("idComment", idComment.getText().toString());
        context.startActivity(intent);
    }
    private void deletePost(){
        tipoConexion="deletePost";
        Conexion con = new Conexion(this);
        System.out.println("idComment: " + idComment.getText());
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/users/WallPosts/" + idComment.getText(), "DELETE", null);
        System.out.println("conexio walls ben feta");
    }

    public Integer getLikes(final Integer id){
        Conexion con = new Conexion(WallAdapter.this);
        //con.execute()
        return 1;
    }
    public void like(final ImageButton like, final Integer id){
        ///votar.setText("Vote");

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //like.setColorFilter(Color.argb(100,0,0,0));

                    like.setColorFilter(Color.argb(100,255,132,7));
                    Conexion con = new Conexion(WallAdapter.this);
                    con.execute("http://10.4.41.146:9999/ServerRESTAPI/users/" + SingletonUsuario.getInstance().getEmail() + "/WallPosts/" + id + "/Like", "POST", null);
                    like(like, id);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void unlike(final ImageButton like, final Integer id){
        //votar.setText("Unvote");


        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    like.setColorFilter(Color.argb(100,0,0,0));
                    Conexion con = new Conexion(WallAdapter.this);
                    con.execute("http://10.4.41.146:9999/ServerRESTAPI/users/" + SingletonUsuario.getInstance().getEmail() + "/WallPosts/" + id + "/UnLike", "POST", null);
                    unlike(like, id);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void OnprocessFinish(JSONObject json) {
        if(tipoConexion.equals("deletePost")){
            try {
                if (json.getInt("code")==200) {
                    /*Toast.makeText(HomeWallFragment.this, "Post deleted succesfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);*/
                    Toast.makeText(this.context, "Post deleted succesfully", Toast.LENGTH_SHORT).show();
                    //HomeWallFragment.getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                    Intent intent = new Intent(this.context, MainActivity.class);
                    context.startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
