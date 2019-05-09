package com.example.PETBook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.PETBook.Models.FriendSuggestionModel;
import com.example.pantallafirstview.R;

import java.util.ArrayList;

public class FriendSuggestionAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<FriendSuggestionModel> user_friends_suggestions;

    public FriendSuggestionAdapter(Context context, ArrayList<FriendSuggestionModel> array){
        this.context = context;
        user_friends_suggestions = array;
    }

    @Override
    public int getCount() {
        return user_friends_suggestions.size();
    }

    @Override
    public Object getItem(int position) {
        return user_friends_suggestions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.friend_suggestion_design,null);
        }
        TextView inputFullName = (TextView) convertView.findViewById(R.id.fullNameInput);

        inputFullName.setText(user_friends_suggestions.get(position).getName() +" " + user_friends_suggestions.get(position).getSurnames());
        return convertView;
    }


}