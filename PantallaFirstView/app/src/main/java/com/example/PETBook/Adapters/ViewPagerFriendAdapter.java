package com.example.PETBook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.PETBook.Models.FriendRequestModel;
import com.example.pantallafirstview.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;

public class ViewPagerFriendAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> friends_fragments;
    private ArrayList<String> titles_fragments;

    public ViewPagerFriendAdapter(FragmentManager fm, ArrayList<Fragment> friends_fragments, ArrayList<String> titles_fragments){
        super(fm);
        this.friends_fragments = friends_fragments;
        this.titles_fragments = titles_fragments;
    }

    @Override
    public int getCount() {
        return friends_fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return friends_fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles_fragments.get(position);
    }

}
