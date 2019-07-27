package com.example.give4friends;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.give4friends.Adapters.DonateAdapter;

public class SearchPeople extends DonateActivity {

@Override
protected void recyclerSetUp(){
    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
    rvFriends.setLayoutManager(linearLayoutManager);
    adapter = new DonateAdapter(friends, true);
    rvFriends.setAdapter(adapter);
    rvFriends.setLayoutManager(new LinearLayoutManager(this));
}

}
