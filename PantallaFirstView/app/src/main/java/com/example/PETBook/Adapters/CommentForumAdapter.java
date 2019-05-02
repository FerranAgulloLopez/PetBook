package com.example.PETBook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.PETBook.Models.CommentForumModel;
import com.example.PETBook.Models.ForumModel;
import com.example.pantallafirstview.R;

import java.util.ArrayList;

public class CommentForumAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CommentForumModel> forumList;

    public CommentForumAdapter (Context context, ArrayList<CommentForumModel> array){
        this.context = context;
        forumList = array;
    }

    @Override
    public int getCount() {
        return forumList.size();
    }

    @Override
    public Object getItem(int position) {
        return forumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.activity_hilo_forum,null);
        }

        TextView nombreForo = (TextView) convertView.findViewById(R.id.nombreForo);
        TextView creadorForo = (TextView) convertView.findViewById(R.id.nombreCreadorForum);
        TextView dataCreacionForum = (TextView) convertView.findViewById(R.id.dataCreacioForum);
        TextView descripcionForum = (TextView) convertView.findViewById(R.id.descripcionForo);

        //nombreForo.setText(forumList.get(position).get());
        creadorForo.setText(forumList.get(position).getCreatorMail());
        dataCreacionForum.setText(forumList.get(position).getCreationDate());
        descripcionForum.setText(forumList.get(position).getDescription());

        return convertView;
    }
}
