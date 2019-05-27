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

import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.ForumModel;
import com.example.pantallafirstview.R;

import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ForumAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<ForumModel> forumList;

    public ForumAdapter (Context context, ArrayList<ForumModel> array){
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
            convertView = layoutInflater.inflate(R.layout.forum_design,null);
        }
        TextView nombreForum = (TextView) convertView.findViewById(R.id.nombreForo);
        TextView numberMessages = (TextView) convertView.findViewById(R.id.numberMessagesForum);
        TextView dataCreacionForum = (TextView) convertView.findViewById(R.id.dataCreacioForum);
        TextView creadorForum = (TextView) convertView.findViewById(R.id.nombreCreadorForum);
        TextView descriptionForum = (TextView) convertView.findViewById(R.id.descriptionForum);


        nombreForum.setText(forumList.get(position).getTitle());
        numberMessages.setText(String.format("%d", forumList.get(position).getComments().size()));
        //System.out.println(forumList.get(position).getComments().get(position).getTamaño());
        String fechaString = forumList.get(position).getCreationDate();
        Date dateNew = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        try {
            dateNew = format.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PrettyTime prettyTime = new PrettyTime(new Date(), Locale.getDefault());
        dataCreacionForum.setText(prettyTime.format(dateNew));

        creadorForum.setText(forumList.get(position).getCreatorMail());
        descriptionForum.setText(forumList.get(position).getDescription());
        return convertView;
    }


}
