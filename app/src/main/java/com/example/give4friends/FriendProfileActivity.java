package com.example.give4friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.give4friends.Adapters.CharitySuggAdapter;
import com.example.give4friends.Adapters.FavCharitiesAdapter;
import com.example.give4friends.models.Charity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class FriendProfileActivity extends AppCompatActivity {

    com.example.give4friends.Adapters.FavCharitiesAdapter feedAdapter;
    ArrayList<Charity> charities;
    RecyclerView rvCharities;
    private SwipeRefreshLayout swipeContainer;

    public ImageView ivProfileImage;
    public TextView tvUserName;
    public TextView tvBio;
    public TextView tvFullName;


    ParseUser myUser;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        context = this;

        myUser = getIntent().getParcelableExtra("user");

        configureToolbar();

        //Below for recycler view of charities
        rvCharities = (RecyclerView) findViewById(R.id.rvFavCharities);
        // initialize the array list of charities
        charities = new ArrayList<Charity>();
        populateRelations();
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //feedAdapter.clear();
                //feedAdapter.addAll(charities);
                populateRelations();
                swipeContainer.setRefreshing(false);
            }

        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Below for static elements of profile
        ivProfileImage = (ImageView) findViewById((R.id.ivProfileImage));
        tvUserName = (TextView) findViewById(R.id.tvName);
        tvBio = (TextView) findViewById(R.id.tvBio);
        tvFullName = (TextView) findViewById(R.id.tvFullName);

        tvUserName.setText("@" + myUser.getUsername());
        if(myUser.getString("bio") == null){
            tvBio.setText(" ");
        }
        else{
            tvBio.setText(myUser.getString("bio"));
        }
        tvBio.setEnabled(false);
        tvFullName.setText(myUser.getString("firstName") + " " + myUser.getString("lastName"));

        //Handles images
        ParseFile file = myUser.getParseFile("profileImage");

        if (file!=null) {
            Glide.with(context)
                    .load(file.getUrl())
                    .apply(new RequestOptions()
                            .transforms(new CenterCrop(), new RoundedCorners(20))
                            .circleCropTransform()
                            .placeholder(R.drawable.user_outline_24)
                            .error(R.drawable.user_outline_24))
                    .into(ivProfileImage);
        }
        else{
            Glide.with(context)
                    .load(R.drawable.user_outline_24)
                    .apply(new RequestOptions()
                            .transforms(new CenterCrop(), new RoundedCorners(20))
                            .circleCropTransform()
                            .placeholder(R.drawable.user_outline_24)
                            .error(R.drawable.user_outline_24))
                    .into(ivProfileImage);
        }
    }
    //add tool bar
    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.TransactionHistory:
                Toast.makeText(this, "Transaction History selected", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.reportUser:
                Toast.makeText(this, "User reported", Toast.LENGTH_LONG).show();
                return true;
            case R.id.logOut:
                Toast.makeText(this, "logging out...", Toast.LENGTH_LONG).show();
                logOut();
            default:
//                Log.e()
        }
        return true;
    }

    public void logOut(){
        ParseUser.logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    private void populateRelations() {
        //Get relation
        final ParseRelation<Charity> favCharities = myUser.getRelation("favCharities");
        //Get all charities in relation
        favCharities.getQuery().findInBackground(new FindCallback<Charity>() {
            @Override
            public void done(List<Charity> objects, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    // results have all the charities the current user liked.
                    // go through relation adding charities
                    for (int i = 0; i < objects.size(); i++) {
                        charities.add((Charity) objects.get(i));

                    }
                    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FriendProfileActivity.this);
                    rvCharities.setLayoutManager(linearLayoutManager);

                    //construct the adapter from this datasource
                    //feedAdapter = new CharitySuggAdapter(charities, false, false,true);
                    //RecyclerView setup (layout manager, use adapter)
                    rvCharities.setAdapter(feedAdapter);
                    rvCharities.scrollToPosition(0);

                }
            }
        });

    }

}






// sample popup with edit textbox
/*
    private void showAddItemDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Change Profile Picture")
                .setView(taskEditText)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

*/

