package com.example.give4friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.give4friends.Adapters.CharitySuggAdapter;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static com.example.give4friends.DonateActivity.charity;

public class CharityProfile extends AppCompatActivity {


    TextView tvCPname;
    TextView tvCPCategory;
    TextView tvCPCause;
    TextView tvCPMission;
    TextView tvCPLikedNum;
    ImageButton ibCPLike;
    RecyclerView rvCPComments;
    CharityAPI charity;
    Charity parseCharity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_profile);

        configureToolbar();

        tvCPname = findViewById(R.id.tvCPname);
        tvCPCategory = findViewById(R.id.tvCPCategory);
        tvCPCause = findViewById(R.id.tvCPCause);
        tvCPMission = findViewById(R.id.tvCPMission);
        tvCPLikedNum = findViewById(R.id.tvCPLikedNum);
        ibCPLike = findViewById(R.id.ibCPLike);
        //rvCPComments = findViewById(R.id.rvCPComments);


        charity = (CharityAPI) Parcels.unwrap(getIntent().getParcelableExtra("Charity"));

        tvCPname.setText(Html.fromHtml("<a href=\'"+charity.getWebsiteUrl()+"\'>"
                +charity.getName()+ "</a>"));

        tvCPMission.setText(Html.fromHtml(charity.getMission()));
        tvCPCategory.setText(Html.fromHtml("<font color=\"#434040\"><b>Category:</b></font> "+charity.getCategoryName()));
        tvCPCause.setText(Html.fromHtml("<font color=\"#434040\"><b>Cause:</b></font> "+charity.getCauseName()));

        // For like button

        convertCharity(charity);


        final boolean is_empty;

        //check if user is in likes list
        final List<Charity> array = ParseUser.getCurrentUser().getList("favCharities" );

        // if user is in likesUsers - start yellow
        if(array == null || !(array.contains(parseCharity))) {
            is_empty = true;
            ibCPLike.setImageResource(R.drawable.ic_like_icon);
            ibCPLike.setColorFilter(Color.BLACK);
            ibCPLike.setRotation(2);
        }
        else{
            is_empty = false;
            ibCPLike.setImageResource(R.drawable.ic_like_filled_con);
            ibCPLike.setColorFilter(Color.YELLOW);
            ibCPLike.setRotation(1);
        }

        ibCPLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean is_empty = (ibCPLike.getRotation() == 2);

                if (is_empty) {
                    ibCPLike.setImageResource(R.drawable.ic_like_filled_con);
                    ibCPLike.setColorFilter(Color.YELLOW);
                    ibCPLike.setRotation(1);
                    //update parse
                    //increment likes for charity
                    parseCharity.incrementLikes(1);
                    //add user to array
                    parseCharity.addLikesUser(ParseUser.getCurrentUser().getObjectId());
                    parseCharity.saveInBackground();

                    //updateUser
                    ParseUser.getCurrentUser().add("favCharities", parseCharity);
                    ParseUser.getCurrentUser().saveInBackground();

                } else {
                    ibCPLike.setImageResource(R.drawable.ic_like_icon);
                    ibCPLike.setColorFilter(Color.BLACK);
                    ibCPLike.setRotation(2);

                    //update parse

                    //update transaction
                    parseCharity.incrementLikes(-1);
                    //add user to array
                    array.remove(ParseUser.getCurrentUser().getObjectId());
                    parseCharity.setKeyLikesUsers(array);
                    parseCharity.saveInBackground();

                    //update user
                    array.remove(parseCharity);
                    ParseUser.getCurrentUser().put("favCharities", array);
                    ParseUser.getCurrentUser().saveInBackground();

                }


            }
        });
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


    public void convertCharity(final CharityAPI selectedCharity){
        ParseQuery<Charity> charityParseQuery = new ParseQuery<Charity>(Charity.class);
        charityParseQuery.include(Charity.KEY_CHARITY_ID);


        charityParseQuery.whereEqualTo("charityName", selectedCharity.getEin());

        charityParseQuery.getFirstInBackground(new GetCallback<Charity>() {
            @Override
            public void done(Charity object, ParseException e) {
                if(e != null){
                    if(e.getCode() == ParseException.OBJECT_NOT_FOUND)
                    {
                        final Charity newCharity = new Charity();
                        newCharity.setKeyCategoryName(selectedCharity.getCategoryName());
                        newCharity.setKeyCauseName(selectedCharity.getCauseName());
                        newCharity.setKeyCharityID(selectedCharity.getEin());
                        newCharity.setKeyMission(selectedCharity.getMission());
                        newCharity.setKeyName(selectedCharity.getName());
                        newCharity.setKeyRatingURL(selectedCharity.getRatingsUrl());
                        newCharity.setKeyUrl(selectedCharity.getWebsiteUrl());

                        newCharity.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null){
                                    Log.d("CharitySearchAdapter", "Created new charity");
                                    parseCharity = newCharity;
                                }
                                else{
                                    Log.d("CharitySearchAdapter", "Invalid charity");
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    else
                    {
                        Log.e("CharitySearchAdapter", "Error with query of charity");                            }
                }
                else{
                    parseCharity = object;
                }
            }

        });
    }



}
