package com.example.PETBook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.PETBook.Models.FriendModel;
import com.example.pantallafirstview.R;

import java.util.ArrayList;

public class FriendAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<FriendModel> user_friends;

    public FriendAdapter(Context context, ArrayList<FriendModel> array){
        this.context = context;
        user_friends = array;
    }

    @Override
    public int getCount() {
        return user_friends.size();
    }

    @Override
    public Object getItem(int position) {
        return user_friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.friend_design,null);
        }
        TextView inputFullName = (TextView) convertView.findViewById(R.id.fullNameInput);

        inputFullName.setText(user_friends.get(position).getName() +" " + user_friends.get(position).getSurnames());
        return convertView;
    }


}