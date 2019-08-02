package com.example.give4friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.give4friends.Adapters.CharityProfileAdapter;
import com.example.give4friends.Adapters.CharitySuggAdapter;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;

public class CharityProfile extends AppCompatActivity {


    RecyclerView rvCPProfile;
    ArrayList<Object> items;
    CharityAPI charity;
    CharityProfileAdapter itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_profile);

        rvCPProfile = findViewById(R.id.rvCPProfile);
        items = new ArrayList<Object>();
        itemsAdapter = new CharityProfileAdapter(items);

        // attach the adapter to the RecyclerView
        rvCPProfile.setAdapter(itemsAdapter);
        // Set layout manager to position the items
        rvCPProfile.setLayoutManager(new LinearLayoutManager(this));

        charity = (CharityAPI) Parcels.unwrap(getIntent().getParcelableExtra("Charity"));
        populateProfile();
    }

    private void populateProfile(){
        items.add(charity);
        itemsAdapter.notifyItemInserted(items.size() - 1);

    }

    private void configureToolbar() {

    }


    public void logOut(){
        ParseUser.logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}
