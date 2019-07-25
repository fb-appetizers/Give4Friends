package com.example.give4friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.give4friends.Fragments.Charity_Search_Fragment;
import com.example.give4friends.Fragments.Main_Transaction_Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Main_Fragment_Branch extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    // define your fragments here
    final Fragment fragment1 = new Main_Transaction_Fragment();
    final Fragment fragment2 = new Charity_Search_Fragment();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Testing the Fragment manager
        setContentView(R.layout.activity_fragment_home);

        configureToolbar();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //final Fragment fragment3 = new User_Profile_Fragment();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment1).commit();
                        break;
                    case R.id.action_search:
                        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment2).commit();
                        break;
                    case R.id.action_profile:
                        //fragmentManager.beginTransaction().replace(R.id.flContainer, fragment3).commit();
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivityForResult(intent,0);
                        break;
                    default:
                        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment1).commit();
                        break;
                }
                return true;
            }
        });
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment1).commit();
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 0) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment1).commit();
            bottomNavigationView.setSelectedItemId(R.id.action_home);
        }
    }

    protected void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Give4Friends");
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

    }
}
