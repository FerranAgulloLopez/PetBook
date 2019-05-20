package com.example.PETBook.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.PETBook.Adapters.ForumAdapter;
import com.example.PETBook.Adapters.WallAdapter;
import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.ForumInfo;
import com.example.PETBook.Models.CommentForumModel;
import com.example.PETBook.Models.ForumModel;
import com.example.PETBook.Models.Image;
import com.example.PETBook.Models.WallModel;
import com.example.PETBook.NewForum;
import com.example.PETBook.SingletonUsuario;
import com.example.PETBook.WallInfo;
import com.example.pantallafirstview.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeWallFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeWallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeWallFragment extends Fragment implements AsyncResult {
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
    private WallAdapter wallAdapter;
    private ArrayList<WallModel> wallModel;
    private OnFragmentInteractionListener mListener;

    public HomeWallFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeWallFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeWallFragment newInstance(String param1, String param2) {
        HomeWallFragment fragment = new HomeWallFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        MyView = inflater.inflate(R.layout.activity_wall, container, false);
        // Set tittle to the fragment
        getActivity().setTitle("Profile");

        Conexion con = new Conexion(HomeWallFragment.this);
        SingletonUsuario su = SingletonUsuario.getInstance();

        con.execute("http://10.4.41.146:9999/ServerRESTAPI/users/" + su.getEmail() + "/WallPosts" ,"GET", null);

        /*lista = MyView.findViewById(R.id.list_walls);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WallModel forumSeleccionado = wallModel.get(position);
                Intent intent = new Intent(getActivity(), WallInfo.class);
                intent.putExtra("wall", forumSeleccionado);
                startActivity(intent);
            }
        });*/
        /*FloatingActionButton fab = MyView.findViewById(R.id.addForum);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewForum.class);
                startActivity(intent);
            }
        });
*/
        return MyView;
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
    private String transformacionFechaHora(String fechaHora){
        Integer fin = 0;
        String result = fechaHora.replace("T", " ");
        result = result.replace("+0000", " ");
        System.out.println(result.split("."));
        return result;
    }
    @Override
    public void OnprocessFinish(JSONObject json) {
        try{
            if(json.getInt("code") == 200){
                wallModel = new ArrayList<>();
                JSONArray jsonArray = json.getJSONArray("array");
                //ArrayList<CommentForumModel> comments = new ArrayList<CommentForumModel>();
                for(int i = 0; i < jsonArray.length(); ++i){
                    JSONObject wall = jsonArray.getJSONObject(i);
                    WallModel w = new WallModel();
                    w.setDescription(wall.getString("description"));
                    w.setCreationDate(transformacionFechaHora(wall.getString("creationDate")));

                    wallModel.add(w);
                }
                wallAdapter = new WallAdapter(getActivity(), wallModel);
                lista = (ListView) MyView.findViewById(R.id.list_walls);
                lista.setAdapter(wallAdapter);
                /*System.out.println(forumModel.get(2).getTitle());
                System.out.println(forumModel.get(2).getComments().get(1).getDescription());*/
                System.out.print(json.getInt("code") + " se muestran correctamente la lista de walls\n");
            }
            else{
                System.out.print("El sistema no logra mostrar la lista de walls del creador\n");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}




