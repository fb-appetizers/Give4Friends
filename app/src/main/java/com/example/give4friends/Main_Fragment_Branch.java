package com.example.give4friends;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.give4friends.Fragments.Main_Transaction_Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class Main_Fragment_Branch extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Testing the Fragment manager


        setContentView(R.layout.activity_fragment_home);
        configureToolbar();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        bottomNavigationView = findViewById(R.id.bottom_navigation);

//        // define your fragments here
        final Fragment fragment1 = new Main_Transaction_Fragment();
//        final Fragment fragment2 = new ComposeFragment();
//        final Fragment fragment3 = new ProfileFragment(ParseUser.getCurrentUser());
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                Fragment fragment;
//                switch (menuItem.getItemId()) {
//                    case R.id.action_home:
//
//                        fragment = fragment1;
////                        finish();
//                        break;
//                    case R.id.action_compose:
//                        fragment = fragment2;
//
//                        break;
//                    case R.id.action_profile:
//                        fragment = fragment3;
//                        break;
//                    case R.id.action_settings:
//                        fragment = fragment4;
//                    default:
////                        showProgressBar();
//                        fragment = fragment4;
//
//                        break;
//                }
//                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
//
//                return true;
//            }
//        });




        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment1).commit();

        bottomNavigationView.setSelectedItemId(R.id.action_home);
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
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

    }



}
