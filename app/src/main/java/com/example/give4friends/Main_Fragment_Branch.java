package com.example.give4friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.give4friends.Fragments.Charity_Search_Fragment;
import com.example.give4friends.Fragments.Main_Transaction_Fragment;
import com.example.give4friends.Fragments.User_Profile_Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class Main_Fragment_Branch extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ImageButton suggBtn;

    //        // define your fragments here
    final Fragment fragment1 = new Main_Transaction_Fragment();
    final Fragment fragment2 = new Charity_Search_Fragment();
    final Fragment fragment3 = new User_Profile_Fragment(ParseUser.getCurrentUser(), false);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Testing the Fragment manager

        setContentView(R.layout.activity_fragment_home);
        suggBtn = findViewById(R.id.suggBtn);


        suggBtn.setBackgroundDrawable(null);

        suggBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DonateActivity.class);
                intent.putExtra("donateNow", false);
                startActivity(intent);
            }
        });

        configureToolbar();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        bottomNavigationView = findViewById(R.id.bottom_navigation);


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
                        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment3).commit();
                        break;
                    case R.id.action_transaction:
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

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Give4Friends");
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_settings);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);



            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.charity_menu, menu);
//        return true;
////        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        return true;
//    }
}
