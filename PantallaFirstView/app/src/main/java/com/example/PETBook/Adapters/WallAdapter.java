package com.example.PETBook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.PETBook.Models.WallModel;
import com.example.pantallafirstview.R;

import java.util.ArrayList;

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
        dataCreacioWall.setText(wallList.get(position).getCreationDate());

        return convertView;
    }


}
