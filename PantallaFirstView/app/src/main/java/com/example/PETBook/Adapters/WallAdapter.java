package com.example.PETBook.Adapters;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.EditWall;
import com.example.PETBook.MainActivity;
import com.example.PETBook.Models.WallModel;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WallAdapter extends BaseAdapter implements AsyncResult {

    private Context context;
    private ArrayList<WallModel> wallList;
    private TextView idComment;
    private String tipoConexion="";
    private TextView numlikes ;
    private TextView dataCreacioWall;
    private TextView descriptionWall;
    private String data;
    private View view;

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.wall_design,null);
        }

        final WallModel w = wallList.get(position);
        descriptionWall = (TextView) convertView.findViewById(R.id.descriptionWall);
        dataCreacioWall = (TextView) convertView.findViewById(R.id.dataCreacionWall);
        idComment = (TextView) convertView.findViewById(R.id.idComment);

        final TextView numlikes = convertView.findViewById(R.id.numLikes);
        TextView numFavs = convertView.findViewById(R.id.numFavs);
        TextView numRetweets = convertView.findViewById(R.id.numRetweets);
        final ImageButton option = convertView.findViewById(R.id.optionButton);

        TextView isRetweeted = convertView.findViewById(R.id.retweeted);
        if(wallList.get(position).isRetweeted()){
            isRetweeted.setVisibility(View.VISIBLE);
            option.setVisibility(View.INVISIBLE);
        }
        else {
            isRetweeted.setVisibility(View.INVISIBLE);
            option.setVisibility(View.VISIBLE);

        }

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
                        System.out.println("numero which:" + which);
                        if (which == 0) {
                            editComment();
                        } else if (which == 1) {
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
        TextView retweetText = convertView.findViewById(R.id.retweetText);
        retweetText.setText(wallList.get(position).getRetweetText());

        if(wallList.get(position).isRetweeted() && !wallList.get(position).getRetweetText().equals("")){
            retweetText.setVisibility(View.VISIBLE);
            retweet.setVisibility(View.INVISIBLE);
            numRetweets.setVisibility(View.INVISIBLE);
        }
        else if (wallList.get(position).isRetweeted() && wallList.get(position).getRetweetText().equals("")){
                retweetText.setVisibility(View.GONE);
                retweet.setVisibility(View.INVISIBLE);
                numRetweets.setVisibility(View.INVISIBLE);
        }
        else{
            retweetText.setVisibility(View.GONE);
            retweet.setVisibility(View.VISIBLE);
            numRetweets.setVisibility(View.VISIBLE);

        }
        //interaction
        //likes
        if (w.getLikes().isEmpty() || !w.getLikes().contains(SingletonUsuario.getInstance().getEmail())) {
            //like.setColorFilter(Color.argb(100,0,0,0));
            like(like, w.getIDWall(), position, numlikes);
        }
        else {
            //like.setColorFilter(Color.argb(100,131,7,6));
            unlike(like, w.getIDWall(), position, numlikes);

        }
        //favs
        if (w.getFavs().isEmpty() || !w.getFavs().contains(SingletonUsuario.getInstance().getEmail())) {
            fav(fav, w.getIDWall());
        }
        else {
            //fav.setColorFilter(Color.argb(100,131,7,6));
            unfav(fav, w.getIDWall());
        }
        //retweets
        if (w.getRetweets().isEmpty() || !w.getRetweets().contains(SingletonUsuario.getInstance().getEmail())) {
            retweet(retweet, w.getIDWall(), convertView);
        }
        else {
            //retweet.setColorFilter(Color.argb(100,131,7,6));
            unretweet(retweet, w.getIDWall());
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


    public void like(final ImageButton like, final Integer id, final Integer position, final TextView numlikes){
        ///votar.setText("Vote");
        //like.setVisibility(View.VISIBLE);
        //unlike.setVisibility(View.INVISIBLE);
        like.setColorFilter(Color.argb(100,131,7,6));
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallAdapter.this.notifyDataSetChanged();

                tipoConexion = "someInteraction";
                numlikes.setText(String.valueOf(wallList.get(position).getLikes().size() + 1));

                try {
                    //like.setColorFilter(Color.argb(100,0,0,0));

                    Conexion con = new Conexion(WallAdapter.this);
                    con.execute("http://10.4.41.146:9999/ServerRESTAPI/users/" + SingletonUsuario.getInstance().getEmail() + "/WallPosts/" + id + "/Like", "POST", null);
                    like.setColorFilter(Color.argb(100,131,7,6));
                    like(like, id, position, numlikes);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void unlike(final ImageButton like, final Integer id, final Integer position, final TextView numlikes){
        //votar.setText("Unvote");
        tipoConexion = "someInteraction";


        like.setColorFilter(Color.argb(100,0,0,0));
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    numlikes.setText(String.valueOf(wallList.get(position).getLikes().size() - 1));

                    WallAdapter.this.notifyDataSetChanged();
                    Conexion con = new Conexion(WallAdapter.this);
                    con.execute("http://10.4.41.146:9999/ServerRESTAPI/users/" + SingletonUsuario.getInstance().getEmail() + "/WallPosts/" + id + "/UnLike", "POST", null);
                    unlike(like, id, position, numlikes);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void fav(final ImageButton fav, final Integer id){
        ///votar.setText("Vote");
        //like.setVisibility(View.VISIBLE);
        //unlike.setVisibility(View.INVISIBLE);
        tipoConexion = "someInteraction";
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //like.setColorFilter(Color.argb(100,0,0,0));
                    fav.setColorFilter(Color.argb(100,131,7,6));
                    Conexion con = new Conexion(WallAdapter.this);
                    con.execute("http://10.4.41.146:9999/ServerRESTAPI/users/" + SingletonUsuario.getInstance().getEmail() + "/WallPosts/" + id + "/Love", "POST", null);
                    fav(fav, id);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void unfav(final ImageButton fav, final Integer id){
        //votar.setText("Unvote");
        tipoConexion = "someInteraction";

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fav.setColorFilter(Color.argb(100,0,0,0));
                    Conexion con = new Conexion(WallAdapter.this);
                    con.execute("http://10.4.41.146:9999/ServerRESTAPI/users/" + SingletonUsuario.getInstance().getEmail() + "/WallPosts/" + id + "/UnLove", "POST", null);
                    unfav(fav, id);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.O)
    private String crearFechaActual() {
        LocalDateTime ahora= LocalDateTime.now();
        String año = String.valueOf(ahora.getYear());
        String mes = String.valueOf(ahora.getMonthValue());
        String dia = String.valueOf(ahora.getDayOfMonth());
        String hora = String.valueOf(ahora.getHour());
        String minutos = String.valueOf(ahora.getMinute());
        String segundos = String.valueOf(ahora.getSecond());
        if(ahora.getMonthValue() < 10) mes = "0" + mes;
        if(ahora.getDayOfMonth() < 10) dia = "0" + dia;
        if(ahora.getHour() < 10) hora = "0" + hora;
        if(ahora.getMinute() < 10) minutos = "0" + minutos;
        if(ahora.getSecond() < 10) segundos = "0" + segundos;
        String fechaRetorno = año + "-" + mes+ "-" + dia + "T" + hora + ":" + minutos + ":" + segundos + ".000Z";
        System.out.println(fechaRetorno);
        return fechaRetorno;
    }

    public void retweet(final ImageButton retweet, final Integer id, final View finalConvertView){

        tipoConexion = "someInteraction";

        retweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String[] opcions = {"Retweet", "Retweet with comment"};
                    retweet.setOnClickListener(new View.OnClickListener() {
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
                                    if (which == 0) {
                                        String YouEditTextValue = "";
                                        retweet.setColorFilter(Color.argb(100,131,7,6));
                                        Conexion con = new Conexion(WallAdapter.this);
                                        JSONObject jsonToSend = new JSONObject();
                                        String fechaHora = crearFechaActual();
                                        try {
                                            jsonToSend.accumulate("description", YouEditTextValue);
                                            jsonToSend.accumulate("creationDate", fechaHora);
                                            System.out.println(jsonToSend);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        con.execute("http://10.4.41.146:9999/ServerRESTAPI/users/" + SingletonUsuario.getInstance().getEmail() + "/WallPosts/" + id + "/Retweet", "POST", jsonToSend.toString());
                                        retweet(retweet, id, finalConvertView);
                                    } else if (which == 1) {
                                        AlertDialog.Builder alert = new AlertDialog.Builder(context);

                                        final EditText edittext = new EditText(context);
                                        alert.setMessage("Enter your retweet text");
                                        alert.setTitle("Retweet");
                                        alert.setView(edittext);

                                        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                String YouEditTextValue = edittext.getText().toString().trim();
                                                retweet.setColorFilter(Color.argb(100,131,7,6));
                                                Conexion con = new Conexion(WallAdapter.this);
                                                JSONObject jsonToSend = new JSONObject();
                                                String fechaHora = crearFechaActual();
                                                try {
                                                    jsonToSend.accumulate("description", YouEditTextValue);
                                                    jsonToSend.accumulate("creationDate", fechaHora);
                                                    System.out.println(jsonToSend);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                con.execute("http://10.4.41.146:9999/ServerRESTAPI/users/" + SingletonUsuario.getInstance().getEmail() + "/WallPosts/" + id + "/Retweet", "POST", jsonToSend.toString());
                                                retweet(retweet, id, finalConvertView);
                                            }
                                        });

                                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                // what ever you want to do with No option.
                                                /*retweet.setColorFilter(Color.argb(100,131,7,6));
                                                Conexion con = new Conexion(WallAdapter.this);
                                                JSONObject jsonToSend = new JSONObject();
                                                String fechaHora = crearFechaActual();
                                                String YouEditTextValue = "";
                                                try {
                                                    jsonToSend.accumulate("description", YouEditTextValue);
                                                    jsonToSend.accumulate("creationDate", fechaHora);
                                                    //jsonToSend.accumulate("ok", "true");
                                                    System.out.println(jsonToSend);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                //con.execute("http://10.4.41.146:9999/ServerRESTAPI/users/" + SingletonUsuario.getInstance().getEmail() + "/WallPosts/" + id + "/Retweet", "POST", jsonToSend.toString());
                                                retweet(retweet, id, finalConvertView);*/
                                            }
                                        });

                                        alert.show();
                                    }
                                }
                            });
                            builder.show();
                        }
                    });


                    //like.setColorFilter(Color.argb(100,0,0,0));


                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void unretweet(final ImageButton retweet, final Integer id){
        tipoConexion = "someInteraction";

        //votar.setText("Unvote");
        retweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    retweet.setColorFilter(Color.argb(255,0,0,0));
                    Conexion con = new Conexion(WallAdapter.this);
                    con.execute("http://10.4.41.146:9999/ServerRESTAPI/users/" + SingletonUsuario.getInstance().getEmail() + "/WallPosts/" + id + "/UnRetweet", "POST", null);
                    unretweet(retweet, id);
                    //deletePost();
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
        else
            try{
                if(tipoConexion.equals("someInteraction")){
                    if(json.getInt("code")==200){
                        System.out.println(json.getInt("code"));
                        WallAdapter.this.notifyDataSetChanged();
                        Intent intent = new Intent(this.context, MainActivity.class);
                        context.startActivity(intent);

                    }
                    else {
                        Toast.makeText(this.context, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (Exception e){
            e.printStackTrace();
        }
    }
}
