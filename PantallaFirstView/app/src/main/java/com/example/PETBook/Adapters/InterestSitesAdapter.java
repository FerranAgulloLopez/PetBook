package com.example.PETBook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.InterestSiteModel;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class InterestSitesAdapter extends BaseAdapter implements AsyncResult {

    private Context context;
    private ArrayList<InterestSiteModel> interestSites;

    public InterestSitesAdapter(Context context, ArrayList<InterestSiteModel> interestSites){
        this.context = context;
        this.interestSites = interestSites;
    }

    @Override
    public int getCount() {
        return interestSites.size();
    }

    @Override
    public Object getItem(int position) {
        return interestSites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.interest_site_design, null);
        }

        final InterestSiteModel is = interestSites.get(position);

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
        }
        return convertView;
    }

    public void hacerVotar(final Button votar, final Integer id){
        votar.setText("Vote");
        votar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Conexion con = new Conexion(InterestSitesAdapter.this);
                    con.execute("http://10.4.41.146:9999/ServerRESTAPI/interestSites/" + id + "/vote/", "POST", null);
                    hacerUnvote(votar, id);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void hacerUnvote(final Button votar, final Integer id){
        votar.setText("Unvote");
        votar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Conexion con = new Conexion(InterestSitesAdapter.this);
                    con.execute("http://10.4.41.146:9999/ServerRESTAPI/interestSites/" + id + "/unVote/", "POST", null);
                    hacerVotar(votar, id);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void OnprocessFinish(JSONObject output){
        try{
            System.out.println(output.getInt("code"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
