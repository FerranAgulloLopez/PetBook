package com.example.PETBook.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.PETBook.Adapters.FriendSuggestionAdapter;
import com.example.PETBook.Adapters.UserAdapter;
import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.FriendSuggestionModel;
import com.example.PETBook.Models.UserModel;
import com.example.PETBook.NewPet;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchUsersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchUsersFragment extends Fragment implements AsyncResult {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View convertView;
    private ArrayList<UserModel> model;
    private ListView lista;
    private UserAdapter user;
    private EditText inputName;
    private Spinner inputTypePet;
    private EditText inputPostalCode;
    private Button   button;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    public SearchUsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchUsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchUsersFragment newInstance(String param1, String param2) {
        SearchUsersFragment fragment = new SearchUsersFragment();
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_search_users, container, false);
        }

        getActivity().setTitle("Search Users");
        inputName = (EditText) convertView.findViewById(R.id.nameInput);
        inputTypePet = (Spinner) convertView.findViewById(R.id.typePetInput);
        inputPostalCode = (EditText) convertView.findViewById(R.id.postalCodeInput);

        button   = (Button) convertView.findViewById(R.id.searchButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "access succesfully", Toast.LENGTH_SHORT).show();
                searchUser();
            }
        });

        return convertView;
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

    private void searchUser() {
        Conexion con = new Conexion(SearchUsersFragment.this);
        String ByName = inputName.getText().toString().trim();
        String ByTypePet = inputTypePet.getSelectedItem().toString();
        String ByZone = inputPostalCode.getText().toString().trim();
       // con.execute("http://10.4.41.146:9999/ServerRESTAPI/Search/User?petType="+ ByTypePet + "&postalCode="+ ByZone + "&userName=" + ByName,"GET", null);


        int num_params = 0;
        if(!ByName.isEmpty()) {
            num_params++;
        } else if (!ByTypePet.isEmpty()) {
            num_params++;
        } else if (!ByZone.isEmpty()){
            num_params++;
        }

        if(num_params != 0) {
            if (num_params == 1) {
                if(!ByName.isEmpty()) {
                    con.execute("http://10.4.41.146:9999/ServerRESTAPI/Search/User?userName=" + ByName,"GET", null);
                } else if (!ByTypePet.isEmpty()) {
                    con.execute("http://10.4.41.146:9999/ServerRESTAPI/Search/User?petType=" + ByTypePet,"GET", null);
                } else if (!ByZone.isEmpty()){
                    con.execute("http://10.4.41.146:9999/ServerRESTAPI/Search/User?postalCode="+ ByZone,"GET", null);
                }
            } else if (num_params == 2) {
                if(ByName.isEmpty()) {
                    con.execute("http://10.4.41.146:9999/ServerRESTAPI/Search/User?petType=" + ByTypePet + "&postalCode=" + ByZone,"GET", null);
                } else if (ByTypePet.isEmpty()) {
                    con.execute("http://10.4.41.146:9999/ServerRESTAPI/Search/User?postalCode=" + ByZone + "&userName=" + ByName,"GET", null);
                } else if (ByZone.isEmpty()){
                    con.execute("http://10.4.41.146:9999/ServerRESTAPI/Search/User?petType="+ ByTypePet + "&userName=" + ByName,"GET", null);
                }
            } else if(num_params == 3){
                con.execute("http://10.4.41.146:9999/ServerRESTAPI/Search/User?petType="+ ByTypePet + "&postalCode="+ ByZone + "&userName=" + ByName,"GET", null);
            }
        }
    }

    @Override
    public void OnprocessFinish(JSONObject json) {

        try{
            if(json.getInt("code") == 200){
                model = new ArrayList<>();
                JSONArray jsonArray = json.getJSONArray("array");
                for(int i = 0; i < jsonArray.length(); ++i){
                    JSONObject user_selected = jsonArray.getJSONObject(i);
                    UserModel e = new UserModel();
                    e.setFirstName(user_selected.getString("firstName"));
                    e.setSecondName(user_selected.getString("secondName"));
                    e.setEmail (user_selected.getString("email"));
                    model.add(e);
                }
                user = new UserAdapter(getActivity(), model);
                lista = (ListView) convertView.findViewById(R.id.list_users);
                lista.setAdapter(user);
                System.out.print(json.getInt("code") + " se muestran correctamente la lista de usuarios\n");
            }
            else{
                System.out.print("El sistema no logra mostrar la lista de usuarios del usuario\n");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
