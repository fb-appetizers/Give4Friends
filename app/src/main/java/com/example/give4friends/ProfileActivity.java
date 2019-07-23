package com.example.give4friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.give4friends.Adapters.FavCharitiesAdapter;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.ProfilePicture;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.app.AlertDialog.Builder;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ProfileActivity extends AppCompatActivity {

    com.example.give4friends.Adapters.FavCharitiesAdapter feedAdapter;
    ArrayList<Charity> charities;
    RecyclerView rvCharities;
    private SwipeRefreshLayout swipeContainer;
    private Object FavCharitiesAdapter;
    private Button btEditBio;
    private ImageButton btChangePic;


    public ImageView ivProfileImage;
    public TextView tvUserName;
    public TextView tvBio;
    public TextView tvTotalRaised;
    public TextView tvTotalDonated;
    public TextView tvFullName;


    //for changing picture
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int SELECT_IMAGE_REQUEST_CODE = 1111;
    public String photoFileName = "photo.jpg";
    private File photoFile;
    private Bitmap photo;


    ParseUser myUser = ParseUser.getCurrentUser();

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;

        btEditBio = findViewById(R.id.btEditProfile);
        btChangePic = findViewById(R.id.btChangePic);

        configureToolbar();


        btEditBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddItemDialog(context);
            }
        });


        btChangePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfilePicture.changePhoto(context);
            }
        });


        //Below for recycler view of charities\
        //find the RecyclerView
        rvCharities = (RecyclerView) findViewById(R.id.rvFavCharities);


        // initialize the array list of charities
        charities = new ArrayList<Charity>();

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        rvCharities.setLayoutManager(linearLayoutManager);

        //construct the adapter from this datasource
        feedAdapter = new FavCharitiesAdapter(charities);
        //RecyclerView setup (layout manager, use adapter)
        rvCharities.setAdapter(feedAdapter);
        rvCharities.scrollToPosition(0);


        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                feedAdapter.clear();
                feedAdapter.addAll(charities);
                populate();
                swipeContainer.setRefreshing(false);
            }

        });


        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        populate();


        // Below for static elements of profile


        ivProfileImage = (ImageView) findViewById((R.id.ivProfileImage));
        tvUserName = (TextView) findViewById(R.id.tvName);
        tvBio = (TextView) findViewById(R.id.tvBio);
        tvTotalRaised = (TextView) findViewById((R.id.tvTotalRaised));
        tvTotalDonated = (TextView) findViewById((R.id.tvTotalDonated));
        tvFullName = (TextView) findViewById(R.id.tvFullName);


        tvUserName.setText(myUser.getUsername());
        tvBio.setText("Bio: " + myUser.getString("bio"));
        tvBio.setEnabled(false);
        tvTotalDonated.setText("Total Donated: $" + myUser.getNumber("totalDonated"));
        tvTotalRaised.setText("Total Raised: $" + myUser.getNumber("totalRaised"));
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
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Give4Friends");
            setSupportActionBar(toolbar);

            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayShowTitleEnabled(false);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //No need to have an intent from the profile activity to the profile activity
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

            return true;
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.transactionHistory:
                    Toast.makeText(this, "Transaction History selected", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.useOffline:
                    Toast.makeText(this, "Use Offline selected", Toast.LENGTH_LONG).show();
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



    @Override
    public void onActivityResult ( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                photo = (Bitmap) data.getExtras().get("data");

                Bitmap selectedImageRotate = ProfilePicture.RotateBitmapFromBitmap(photo,270);


                Glide.with(context)
                        .load(selectedImageRotate)
                        .apply(new RequestOptions()
                                .transforms(new CenterCrop(), new RoundedCorners(20))
                                .circleCropTransform()
                                .placeholder(R.drawable.user_outline_24)
                                .error(R.drawable.user_outline_24))
                        .into(ivProfileImage);






                ProfilePicture.updatePhoto(ParseUser.getCurrentUser(), selectedImageRotate);


            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == SELECT_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri photoUri = data.getData();
                try {
                    photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                photoFile = new File(ProfilePicture.getRealPathFromURI(context, photoUri));
                // Do something with the photo based on Uri
                try {
                    Bitmap selectedImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photoUri);

                    ivProfileImage.setImageBitmap(selectedImage);
                    ProfilePicture.updatePhoto(ParseUser.getCurrentUser(), photo);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    };




    public void updateBio(String bio){
        ParseUser user = ParseUser.getCurrentUser();
        user.put("bio", bio);
        user.saveInBackground();
    }

    private void showAddItemDialog(Context c) {
        final EditText taskEditText = new EditText(c);
            taskEditText.setText(myUser.getString("bio"));
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Edit Bio")
                .setView(taskEditText)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String bio = String.valueOf(taskEditText.getText());
                        updateBio(bio);
                        tvBio.setText(bio);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }



private void populate(){


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
            }

        }
    });
}}







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








        // Might be valuable code for later -- getting one user from database
        // Didn't use because we are just looking at current user

        /*

        // Actually query and fill
        //get query -- get user info and fill views
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        //Test
        query.whereEqualTo(User.KEY_ID, "RClE3nhbpc");
        // query.whereEqualTo(User.KEY_ID, ParseUser.getCurrentUser());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            //iterate through query
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null ){
                    // fill views
                    User myUser = (User) object;
                    tvUserName.setText(myUser.getKeyUsername());
                    tvBio.setText(myUser.getKeyBio());
                    tvTotalDonated.setText("$" + myUser.getKeyTotalDonated());
                    tvTotalRaised.setText("$" +  myUser.getKeyTotalRaised());

                    // Handles images
                    Glide.with(context)
                            .load(myUser.getKeyProfileImage().getUrl())
                            .apply(new RequestOptions()
                                    .transforms(new CenterCrop(), new RoundedCorners(20)))
                            .into(ivProfileImage);

                }else {
                    Log.e("ProfileActivity", "Can't get Charity");
                    e.printStackTrace();
                }
            }
        });





        /*
        // query.whereEqualTo(User.KEY_ID, ParseUser.getCurrentUser());
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            //iterate through query
            @Override
            public void done(ParseUser object, ParseException e) {

                if (e == null ){


                    // fill views


                    tvUserName.setText(object.getUsername());
                    tvBio.setText(object.getString("Bio"));
                    tvTotalDonated.setText("$" + object.getNumber("totalDonated"));
                    tvTotalRaised.setText("$" +  object.getNumber("totalRaised"));

                    // Handles images
                    Glide.with(context)
                            .load(object.getParseFile("profileImage").getUrl())
                            .apply(new RequestOptions()
                                    .transforms(new CenterCrop(), new RoundedCorners(20)))
                            .into(ivProfileImage);
                    */
        // query.whereEqualTo(User.KEY_ID, ParseUser.getCurrentUser());



