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
    TextView tvCPname;
    TextView tvCPCategory;
    TextView tvCPCause;
    TextView tvCPMission;
    TextView tvCPLikedNum;
    ImageButton ibCPLike;
    RecyclerView rvCPComments;

    RecyclerView rvCPProfile;
    ArrayList<Object> items;
    CharityAPI charity;
    CharityProfileAdapter itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_profile);

        configureToolbar();


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
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Give4Friends");
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CharityProfile.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.toolbar_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.charity_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void logOut(){
        ParseUser.logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.etCharity:
                Toast.makeText(this, "Charity Search selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.transactionHistory:
                Toast.makeText(this, "Transaction History selected", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.useOffline:
                Toast.makeText(this, "Use Offline selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_LONG).show();
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.logOut:
                Toast.makeText(this, "logging out...", Toast.LENGTH_LONG).show();
                logOut();
            default:
//                Log.e()
        }
        return true;
    }
}