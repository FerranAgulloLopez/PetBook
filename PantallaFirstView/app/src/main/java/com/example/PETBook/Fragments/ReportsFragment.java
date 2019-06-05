package com.example.PETBook.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.PETBook.Adapters.ReportsAdapter;
import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.ReportModel;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReportsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReportsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportsFragment extends Fragment implements AsyncResult {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View MyView;
    private ListView lista;
    private ArrayList<ReportModel> reports;
    private ReportsAdapter reportsAdapter;

    private OnFragmentInteractionListener mListener;

    public ReportsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportsFragment newInstance(String param1, String param2) {
        ReportsFragment fragment = new ReportsFragment();
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


        MyView =  inflater.inflate(R.layout.fragment_reports, container, false);
        lista = MyView.findViewById(R.id.list_reports);
        // Set tittle to the fragment
        getActivity().setTitle("Reports");


        Conexion con = new Conexion(ReportsFragment.this);
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/reports","GET", null);


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReportModel reportModel = reports.get(position);
            }
        });


        // Inflate the layout for this fragment
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

    @Override
    public void OnprocessFinish(JSONObject output) {

        try {
            if (output.getInt("code") == 200) {
                reports = new ArrayList<ReportModel>();
                JSONArray jsonArray = output.getJSONArray("array");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObjectHijo = jsonArray.getJSONObject(i);
                    ReportModel reportModel = new ReportModel();

                    // TODO mirar resputa server y coger datos


                    reportModel.setId(jsonObjectHijo.getString("id"));
                    reportModel.setEmailUserReporting(jsonObjectHijo.getString("creatorMail"));
                    reportModel.setEmailUserReported(jsonObjectHijo.getString("suspectMail"));
                    reportModel.setDescription(jsonObjectHijo.getString("description"));
                    reportModel.setCreationDate(jsonObjectHijo.getString("creationDate"));

                    Integer approvedVotes = jsonObjectHijo.getJSONArray("approved").length();
                    Integer rejectedVotes = jsonObjectHijo.getJSONArray("rejected").length();



                    //if (rejectedVotes == 0 && approvedVotes == 0) {

                    //}

                    reports.add(reportModel);

                    reportsAdapter = new ReportsAdapter(getActivity(), reports);

                    lista.setAdapter(reportsAdapter);

                }
            }
            else{
                System.out.print("El sistema no logra mostrar la lista de pets de reportes\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
}
