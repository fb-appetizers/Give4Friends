package com.example.give4friends.Adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.give4friends.Fragments.Favorite_Charity_Fragment;
import com.example.give4friends.Fragments.Milestone_Fragment;
import com.parse.ParseUser;

public class ProfilePagerAdapter extends FragmentPagerAdapter {

    public int tabNum;
    public ParseUser user;
    public ProfilePagerAdapter(@NonNull FragmentManager fm, int tabNum, ParseUser user) {
        super(fm, tabNum);
        this.user = user;
        this.tabNum = tabNum;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                Favorite_Charity_Fragment fragment0 = new Favorite_Charity_Fragment(user);
                return fragment0;

            case 1:
                Milestone_Fragment fragment1 = new Milestone_Fragment();
                return fragment1;

            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return tabNum;
    }



}
