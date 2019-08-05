package com.example.give4friends;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.give4friends.Fragments.Charity_Search_Fragment;
import com.example.give4friends.Fragments.Favorite_Charity_Fragment;
import com.example.give4friends.Fragments.History_Fragment;
import com.example.give4friends.Fragments.Main_Transaction_Fragment;
import com.example.give4friends.Fragments.Search_User_Fragment;
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
    final Fragment fragment4 = new History_Fragment(ParseUser.getCurrentUser(), false);
    //final Fragment fragment5 = new Search_User_Fragment();
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

                //This thread clears the Glide cache every time you switch fragments. In order make the images display faster
                // while also allowing you to change the image profiles if you want.

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

                        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment4).commit();

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
        toolbarTitle.setTextSize(30);
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

    public interface OnBackClickListener {
        boolean onBackClick();
    }

    private OnBackClickListener onBackClickListener;

    public void setOnBackClickListener(OnBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
    }

    @Override
    public void onBackPressed() {
        if (onBackClickListener != null && onBackClickListener.onBackClick()) {
            return;
        }
        super.onBackPressed();
    }




}
