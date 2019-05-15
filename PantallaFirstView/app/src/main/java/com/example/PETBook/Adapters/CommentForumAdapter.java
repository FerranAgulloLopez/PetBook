package com.example.PETBook.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.PETBook.Models.CommentForumModel;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CommentForumAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CommentForumModel> forumList;
    /*private ImageButton editCommentButton;
    private ImageButton deleteCommentButton;*/

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
    @TargetApi(Build.VERSION_CODES.O)
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.comment_design,null);
        }

        TextView userCreatorComment = convertView.findViewById(R.id.userCreatorComment);
        TextView dataComment = convertView.findViewById(R.id.dataComment);
        TextView descripcioComment = convertView.findViewById(R.id.descripcionComment);
        TextView editable = (TextView) convertView.findViewById(R.id.editedView);
        //ImageButton editComment = (ImageButton) convertView.findViewById(R.id.editCommentButton);

        LocalDateTime ahora= LocalDateTime.now();
        Integer minutosActual = ahora.getMinute();
        System.out.println(forumList.get(position).getCreationDate());
        //Integer minutoCreacion = forumList.get(position).getCreationDate()
        /*if(minutosActual == ){

        }*/

        userCreatorComment.setText(forumList.get(position).getCreatorMail());
        dataComment.setText(forumList.get(position).getCreationDate());
        descripcioComment.setText(forumList.get(position).getDescription());

        return convertView;
    }
}
