package com.example.PETBook.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.PETBook.Fragments.MyFriendRequestsFragment;

import com.example.PETBook.Adapters.ViewPagerFriendAdapter;
import com.example.PETBook.Adapters.FriendAdapter;
import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.EventInfo;
import com.example.PETBook.Models.FriendModel;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyFriendsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyFriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFriendsFragment extends Fragment implements AsyncResult {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /* ATRIBUTOS   */
    private View MyView;
    private TextView textViewMyfriends;
    private TextView textViewNotifications;
    private TextView textView6;
    private ListView lista;
    private FriendAdapter friendsUser;
    private ArrayList<FriendModel> model;

    private TabLayout tabLayout;
    private ViewPager vpContenido;
    private ViewPagerFriendAdapter adapter;
    private ArrayList<Fragment> friends_fragments;
    private ArrayList<String> titles_fragments;
    private int[] tabIcons = {R.drawable.ic_menu_gallery,R.drawable.ic_menu_manage,R.drawable.ic_menu_gallery};

    private OnFragmentInteractionListener mListener;

    public MyFriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFriendsFragment newInstance(String param1, String param2) {
        MyFriendsFragment fragment = new MyFriendsFragment();
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
        if (MyView == null) {
            MyView = inflater.inflate(R.layout.fragment_my_friends, container, false);
        }
        tabLayout = (TabLayout) MyView.findViewById(R.id.tlTab1);
        vpContenido =  (ViewPager) MyView.findViewById(R.id.vpContenido1);

        loadFragments();
        loadTitles();
        viewPagerEnTabLayout();

        // Set tittle to the fragment
        getActivity().setTitle("FRIENDS");



        return MyView;
    }

    public void loadFragments() {
        friends_fragments = new ArrayList<>();
        friends_fragments.add(new MyFriendSuggestionsFragment());
        friends_fragments.add(new MyFriendsAcceptedFragment());
        friends_fragments.add(new MyFriendRequestsFragment());

    }


    public void loadTitles() {
        titles_fragments = new ArrayList<>();
        titles_fragments.add("SUGGESTIONS");
        titles_fragments.add("FRIENDS");
        titles_fragments.add("REQUESTS");
    }

    private void viewPagerEnTabLayout() {
        adapter = new ViewPagerFriendAdapter(getChildFragmentManager(),friends_fragments,titles_fragments);
        vpContenido.setAdapter(adapter);
        vpContenido.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(vpContenido);
        tabIcons();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                iconColor(tab, "#3b5998");
               // adapter.notifyDataSetChanged();
               // vpContenido.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                iconColor(tab, "#E0E0E0");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void tabIcons() {
        for (int i = 0; i < 3; i++) {
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }
    }

    private void iconColor(TabLayout.Tab tab, String nuevoColor) {
        tab.getIcon().setColorFilter(Color.parseColor(nuevoColor), PorterDuff.Mode.SRC_IN);
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
        /*
        try{
            if(json.getInt("code") == 200){
                model = new ArrayList<>();
                JSONArray jsonArray = json.getJSONArray("array");
                for(int i = 0; i < jsonArray.length(); ++i){
                    JSONObject evento = jsonArray.getJSONObject(i);
                    FriendModel e = new FriendModel();
                    e.setName(evento.getString("firstName"));
                    e.setSurnames(evento.getString("secondName"));
                    model.add(e);
                }
                friendsUser = new FriendAdapter(getActivity(), model);
                lista = (ListView) MyView.findViewById(R.id.list_friends);
                lista.setAdapter(friendsUser);
                System.out.print(json.getInt("code") + " se muestran correctamente la lista de amigos\n");
            }
            else{
                System.out.print("El sistema no logra mostrar la lista de amigos del usuario\n");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        */
    }
}