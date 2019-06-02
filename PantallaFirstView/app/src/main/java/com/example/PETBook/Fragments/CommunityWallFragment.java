package com.example.PETBook.Fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.PETBook.Adapters.CommunityWallAdapter;
import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.CommunityWallModel;
import com.example.PETBook.NewWallCommunity;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WallFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityWallFragment extends Fragment implements AsyncResult {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /* ATRIBUTOS   */
    private View MyView;
    private ListView lista;
    private CommunityWallAdapter communityWallAdapter;
    private ArrayList<CommunityWallModel> communityWallModel;
    private OnFragmentInteractionListener mListener;
    private String tipoConexion;
    private ProgressBar spinner;
    private ImageView addCommentWalL;
    private TextView emptyWalls;
    private ImageView imageButtonAdd;
    private ImageView helpIcon;
    private TextView helpText;
    private ImageButton addComment;
    private ImageView imatgeUser;

    public CommunityWallFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WallFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WallFragment newInstance(String param1, String param2) {
        WallFragment fragment = new WallFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private void getPicture(){
        System.out.println("entro a mostrar imatge");
        tipoConexion = "getImatge";
        Conexion con = new Conexion(this);
        SingletonUsuario su = SingletonUsuario.getInstance();
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/getPicture/" + su.getEmail(), "GET", null);
        System.out.println("conexio walls ben feta");

    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        MyView = inflater.inflate(R.layout.activity_community_wall, container, false);
        // Set tittle to the fragment
        getActivity().setTitle("Home");
        addCommentWalL = MyView.findViewById(R.id.addCommunityWall);
        spinner=(ProgressBar)MyView.findViewById(R.id.progressBar);
        emptyWalls = MyView.findViewById(R.id.emptyWalls);
        imageButtonAdd = MyView.findViewById(R.id.createCommentWall2);

        helpIcon = MyView.findViewById(R.id.help1PostIcon);
        helpText = MyView.findViewById(R.id.help1Post);

        mostrarWalls();

        addCommentWalL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewWallCommunity.class);

                startActivity(intent);

            }
        });
        imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewWallCommunity.class);

                startActivity(intent);

            }
        });

        lista = (ListView) MyView.findViewById(R.id.listaCommunityWall);
        lista.setClickable(true);

        lista.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        return MyView;
    }

    @TargetApi(Build.VERSION_CODES.O)
    private String crearFechaActual() {
        Date date = new Date();
        return Long.toString(date.getTime());
    }

    private void mostrarWalls(){
        System.out.println("entro a mostrar walls");
        tipoConexion = "getWalls";
        Conexion con = new Conexion(this);
        SingletonUsuario su = SingletonUsuario.getInstance();
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/WallPosts/GetInitialWallPosts", "GET", null);
        System.out.println("conexio walls ben feta");

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void OnprocessFinish(JSONObject json) {
        spinner.setVisibility(View.VISIBLE);
        if (tipoConexion.equals("getWalls")){
            try {
                if (json.getInt("code") == 200) {
                    communityWallModel = new ArrayList<CommunityWallModel>();
                    JSONArray jsonArray = json.getJSONArray("array");
                    //ArrayList<CommentForumModel> comments = new ArrayList<CommentForumModel>();
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject wall = jsonArray.getJSONObject(i);
                        CommunityWallModel w = new CommunityWallModel();
                        w.setIDWall(wall.getInt("id"));
                        //System.out.println("idwall: " + w.getIDWall());
                        w.setDescription(wall.getString("description"));
                        w.setCreationDate(wall.getString("creationDate"));
                        //likes
                        JSONArray likes = wall.getJSONArray("likes");
                        ArrayList<String> likers = new ArrayList<String>();
                        for (int j = 0; j < likes.length(); ++j){
                            likers.add(likes.getString(j));
                        }
                        w.setLikes(likers);
                        //favs
                        JSONArray favs = wall.getJSONArray("loves");
                        ArrayList<String> favers = new ArrayList<String>();
                        for (int k = 0; k < favs.length(); ++k){
                            favers.add(favs.getString(k));
                        }
                        w.setFavs(favers);
                        //retweets
                        JSONArray retweets = wall.getJSONArray("retweets");
                        ArrayList<String> retweeters = new ArrayList<String>();
                        for (int l = 0; l < retweets.length(); ++l){
                            retweeters.add(retweets.getString(l));
                        }
                        w.setRetweets(retweeters);
                        w.setRetweetId(wall.getInt("retweetId"));
                        w.setRetweeted(wall.getBoolean("retweet"));
                        if((wall.getBoolean("retweet"))){
                            w.setRetweetText(wall.getString("retweetText"));
                        }
                        //w.setRetweetText(wall.getString("retweetText"));
                        communityWallModel.add(w);
                    }
                    if(communityWallModel.size()==0){
                        emptyWalls.setVisibility(View.VISIBLE);
                        imageButtonAdd.setVisibility(View.VISIBLE);
                        addCommentWalL.setVisibility(View.INVISIBLE);
                        helpText.setVisibility(View.INVISIBLE);
                        helpIcon.setVisibility(View.INVISIBLE);
                    }
                    else if(communityWallModel.size() == 1){
                        emptyWalls.setVisibility(View.INVISIBLE);
                        imageButtonAdd.setVisibility(View.INVISIBLE);
                        addCommentWalL.setVisibility(View.VISIBLE);
//                        helpText.setVisibility(View.VISIBLE);
                        //helpIcon.setVisibility(View.VISIBLE);
                    }
                    else {
                        emptyWalls.setVisibility(View.INVISIBLE);
                        imageButtonAdd.setVisibility(View.INVISIBLE);
                        addCommentWalL.setVisibility(View.VISIBLE);
                      //  helpText.setVisibility(View.INVISIBLE);
                       // helpIcon.setVisibility(View.INVISIBLE);
                    }
                    communityWallAdapter= new CommunityWallAdapter(getActivity(), communityWallModel);
                    lista = (ListView) MyView.findViewById(R.id.listaCommunityWall);
                    lista.setAdapter(communityWallAdapter);

                    /*System.out.println(forumModel.get(2).getTitle());
                    System.out.println(forumModel.get(2).getComments().get(1).getDescription());*/
                    System.out.print(json.getInt("code") + " se muestran correctamente la lista de walls\n");

                    spinner.setVisibility(View.GONE);
                } else {
                    System.out.print("El sistema no logra mostrar la lista de walls del creador\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(tipoConexion.equals("deletePost")){
            try {
                if (json.getInt("code")==200) {
                    /*Toast.makeText(WallFragment.this, "Post deleted succesfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);*/
                    Toast.makeText(getActivity(), "Post deleted succesfully", Toast.LENGTH_SHORT).show();
                    getFragmentManager().beginTransaction().detach(this).attach(this).commit();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}




