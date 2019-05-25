package com.example.PETBook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.PETBook.Models.WallModel;
import com.example.pantallafirstview.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WallAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<WallModel> wallList;

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
        TextView descriptionWall = (TextView) convertView.findViewById(R.id.descriptionWall);
        TextView dataCreacioWall = (TextView) convertView.findViewById(R.id.dataCreacionWall);
        TextView idComment = (TextView) convertView.findViewById(R.id.idComment);

        idComment.setText(wallList.get(position).getIDWall().toString());
        descriptionWall.setText(wallList.get(position).getDescription());

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


}
