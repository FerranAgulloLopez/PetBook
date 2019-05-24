package com.example.PETBook.Fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.Button;
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

import com.example.PETBook.Adapters.WallAdapter;
import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.EditProfile;
import com.example.PETBook.EditWall;
import com.example.PETBook.MainActivity;
import com.example.PETBook.Models.Image;
import com.example.PETBook.Models.WallModel;
import com.example.PETBook.NewWall;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.example.pantallafirstview.R.id.idComment;

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
    private String tipoConexion;
    private TextView inputFullName;
    private TextView inputEmail;
    private TextView inputBirthday;
    private TextView inputPostalCode;
    private ImageView imatgePerfil;
    private Button buttonEditProfile;
    private ProgressBar spinner;
    private ImageView iconoDir;
    private ImageView iconoNac;
    private ImageButton addCommentWalL;
    private ImageButton botonOpcion;
    private TextView emptyWalls;
    private String idComment;
    private TextView ViewIDComment;
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
        mostrarPerfil();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        MyView = inflater.inflate(R.layout.activity_wall, container, false);
        // Set tittle to the fragment
        getActivity().setTitle("Home");
        imatgePerfil = MyView.findViewById(R.id.imatgePerfilHome);
        inputFullName = MyView.findViewById(R.id.fullNameInput);
        inputEmail = MyView.findViewById(R.id.emailInput);
        inputBirthday = MyView.findViewById(R.id.birthdayInput);
        inputPostalCode = MyView.findViewById(R.id.postalCodeInput);
        iconoDir = MyView.findViewById(R.id.iconoDir);
        iconoNac = MyView.findViewById(R.id.iconoNac);
        addCommentWalL = MyView.findViewById(R.id.addCommentWall);
        spinner=(ProgressBar)MyView.findViewById(R.id.progressBar);
        emptyWalls = MyView.findViewById(R.id.emptyWalls);

        mostrarPerfil();
        // mostrarWalls();
        buttonEditProfile = (Button) MyView.findViewById(R.id.editProfileButton);

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);

            }
        });
        addCommentWalL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewWall.class);

                startActivity(intent);

            }
        });
        lista = (ListView) MyView.findViewById(R.id.list_walls);
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
        final String[] opcions = {"Edit", "Delete"};
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //System.out.println("arg0:" + MyView.findViewById(R.id.descriptionWall).callOnClick());

                ViewIDComment = arg1.findViewById(R.id.idComment);
                idComment = ViewIDComment.getText().toString();
                System.out.println("idComment: " + idComment);
                //idComment = lista.findViewById(R.id.idComment);
                AlertDialog.Builder builder = new AlertDialog.Builder(MyView.getContext());
               // System.out.println("idcomment:" + builder.getContext());
                builder.setItems(opcions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]

                        System.out.println("idComment: " + ViewIDComment.getText().toString());
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
                return true;
            }

        });

        return MyView;
    }

    private void editComment(){
        /*Intent intent = new Intent(HomeWallFragment.this, EditWall.class);
        startActivity(intent);*/
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
        String fechaRetorno = año + "-" + mes+ "-" + dia + "T" + hora + ":" + minutos + ":" + segundos + ".000Z";

        System.out.println(fechaRetorno);
        return fechaRetorno;
    }

    private void deletePost(){
        tipoConexion="deletePost";
        Conexion con = new Conexion(this);
        System.out.println("idComment: " + idComment);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/users/WallPosts?wallPostId=" + idComment, "DELETE", null);
        System.out.println("conexio walls ben feta");
    }

    private void getPicture(){
        System.out.println("entro a mostrar imatge");
        tipoConexion = "getImatge";
        Conexion con = new Conexion(this);
        SingletonUsuario su = SingletonUsuario.getInstance();
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/getPicture/" + su.getEmail(), "GET", null);
        System.out.println("conexio walls ben feta");

    }
    private void mostrarWalls(){
        System.out.println("entro a mostrar walls");
        tipoConexion = "getWalls";
        Conexion con = new Conexion(this);
        SingletonUsuario su = SingletonUsuario.getInstance();
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/users/" + su.getEmail() + "/WallPosts", "GET", null);
        System.out.println("conexio walls ben feta");

    }
    private void mostrarPerfil(){
        System.out.println("entro a mostrar perfil");
        tipoConexion = "getUser";
        Conexion con = new Conexion(this);
        SingletonUsuario su = SingletonUsuario.getInstance();
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/GetUser/" + su.getEmail() , "GET", null);
        System.out.println("conexion mostrar user bien hecha");
        //mostrarWalls();
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
        spinner.setVisibility(View.VISIBLE);
        if (tipoConexion.equals("getWalls")){
            try {
                if (json.getInt("code") == 200) {
                    wallModel = new ArrayList<WallModel>();
                    JSONArray jsonArray = json.getJSONArray("array");
                    //ArrayList<CommentForumModel> comments = new ArrayList<CommentForumModel>();
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject wall = jsonArray.getJSONObject(i);
                        WallModel w = new WallModel();
                        w.setIDWall(wall.getInt("id"));
                        System.out.println("idwall: " + w.getIDWall());
                        w.setDescription(wall.getString("description"));
                        w.setCreationDate(transformacionFechaHora(wall.getString("creationDate")));

                        wallModel.add(w);
                    }
                    if(wallModel.size()==0){
                        emptyWalls.setVisibility(View.VISIBLE);
                    }
                    else{
                        emptyWalls.setVisibility(View.INVISIBLE);
                    }
                    wallAdapter = new WallAdapter(getActivity(), wallModel);
                    lista = (ListView) MyView.findViewById(R.id.list_walls);
                    lista.setAdapter(wallAdapter);

                    /*System.out.println(forumModel.get(2).getTitle());
                    System.out.println(forumModel.get(2).getComments().get(1).getDescription());*/
                    System.out.print(json.getInt("code") + " se muestran correctamente la lista de walls\n");
                    addCommentWalL.setVisibility(View.VISIBLE);

                    getPicture();
                    spinner.setVisibility(View.GONE);
                } else {
                    System.out.print("El sistema no logra mostrar la lista de walls del creador\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(tipoConexion.equals("getUser")){
            try {
                System.out.println("entro a mostrar el user");
                if (json.getInt("code")==200) {
                    inputFullName.setText(json.getString("firstName") + " " + json.getString("secondName"));
                    inputEmail.setText("@" + json.getString("email"));
                    inputBirthday.setText(json.getString("dateOfBirth"));
                    inputPostalCode.setText(json.getString("postalCode"));
                    iconoNac.setVisibility(View.VISIBLE);
                    iconoDir.setVisibility(View.VISIBLE);
                    mostrarWalls();
                } else {
                    //Toast.makeText(HomeWallFragment.this, "There was a problem during the process.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(tipoConexion.equals("getImatge")){
            try {
                System.out.println("entro a mostrar la imagen");
                if (json.getInt("code")==200) {
                    // convert string to bitmap
                    SingletonUsuario user = SingletonUsuario.getInstance();
                    Image imagenConversor = Image.getInstance();
                    String image = json.getString("image");
                    Bitmap profileImage = imagenConversor.StringToBitMap(image);
                    imatgePerfil.setImageBitmap(profileImage);
                    //user.setProfilePicture(profileImage);



                    spinner.setVisibility(View.GONE);
                } else {
                    //Toast.makeText(HomeWallFragment.this, "There was a problem during the process.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(tipoConexion.equals("deletePost")){
            try {
                if (json.getInt("code")==200) {
                    /*Toast.makeText(HomeWallFragment.this, "Post deleted succesfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);*/
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}




