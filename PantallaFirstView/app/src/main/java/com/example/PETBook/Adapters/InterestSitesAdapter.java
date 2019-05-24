package com.example.PETBook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.PETBook.Models.InterestSiteModel;
import com.example.pantallafirstview.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class InterestSitesAdapter extends BaseAdapter {

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

        TextView title = (TextView) convertView.findViewById(R.id.TitleSite);
        TextView type = (TextView) convertView.findViewById(R.id.TypeSite);
        TextView location = (TextView) convertView.findViewById(R.id.LocationSite);
        Button vote = (Button) convertView.findViewById(R.id.VoteButton);
        return convertView;
    }
}
