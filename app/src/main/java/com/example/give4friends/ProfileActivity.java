package com.example.give4friends;

import androidx.appcompat.app.AppCompatActivity;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
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
    private Button btEditProfile;
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
    File photoFile;

    ParseUser myUser = ParseUser.getCurrentUser();

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        context = this;

        btEditProfile = findViewById(R.id.btEditProfile);


        btEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


        btChangePic = findViewById(R.id.btChangePic);


        btChangePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangePicDialog(context);

            }
        });


        //Below for recycler view of charities


        //find the RecyclerView
        rvCharities = (RecyclerView) findViewById(R.id.rvFavCharities);

        // get favorite charities from user

        // initialize the array list of charities
        charities = new ArrayList<Charity>();

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
                    recyclerSetup();

                }

            }
        });

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setReverseLayout(true);
        rvCharities.setLayoutManager(linearLayoutManager);


        // Below for static elements of profile


        ivProfileImage = (ImageView) findViewById((R.id.ivProfileImage));
        tvUserName = (TextView) findViewById(R.id.tvName);
        tvBio = (TextView) findViewById(R.id.tvBio);
        tvTotalRaised = (TextView) findViewById((R.id.tvTotalRaised));
        tvTotalDonated = (TextView) findViewById((R.id.tvTotalDonated));
        tvFullName = (TextView) findViewById(R.id.tvFullName);


        tvUserName.setText(myUser.getUsername());
        tvBio.setText(myUser.getString("Bio"));
        tvTotalDonated.setText("Total Donated: $" + myUser.getNumber("totalDonated"));
        tvTotalRaised.setText("Total Raised: $" + myUser.getNumber("totalRaised"));
        tvFullName.setText(myUser.getString("firstName") + " " + myUser.getString("lastName"));


        // Handles images
        Glide.with(context)
                .load(myUser.getParseFile("profileImage").getUrl())
                .apply(new RequestOptions()
                        .transforms(new CenterCrop(), new RoundedCorners(20))
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background))
                .into(ivProfileImage);


    }


    public void recyclerSetup() {

        //construct the adapter from this datasource
        feedAdapter = new FavCharitiesAdapter(charities);

        //RecyclerView setup (layout manager, use adapter)

        rvCharities.setAdapter(feedAdapter);
        rvCharities.scrollToPosition(0);

    }

    // Change profile picture


    private void showChangePicDialog(Context c) {
        String[] options = {"Take photo", "Choose from gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(c);
                dialog.setTitle("Change Profile Picture");
                dialog.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       changePic(i);
                    }
                });
        dialog.show();
    }


    public void changePic(int i) {
        if(i == 0)
        {
            launchCamera();
        }
        else
        {
            launchSelect();

        }
    }

    public void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(ProfileActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(ProfileActivity.this.getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }


    public void launchSelect() {

        //photoFile = getPhotoFileUri(photoFileName);

        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Create a File reference to access to future access

        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivProfileImage.setImageBitmap(takenImage);

            } else { // Result was a failure
                Toast.makeText(ProfileActivity.this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == SELECT_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri photoUri = data.getData();

                //photoFile = new File(photoUri.getPath());
                photoFile = new File(getRealPathFromURI(ProfileActivity.this, photoUri));

                // Do something with the photo based on Uri
                //Bitmap selectedImage = null;
                try {
                    Bitmap selectedImage = MediaStore.Images.Media.getBitmap(ProfileActivity.this.getContentResolver(), photoUri);
                    ivProfileImage.setImageBitmap(selectedImage);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Load the selected image into a preview
                // ivPostImage.setImageBitmap(selectedImage);
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(ProfileActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Profile Activity");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d("Profile Activity", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    private void savePost(ParseUser parseUser, final File photoFile, View view) {

        //pb.setVisibility(ProgressBar.VISIBLE);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");


        // Retrieve the object by id
        ParseUser.getCurrentUser().put("profileImage", new ParseFile(photoFile));
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            if (e != null) {
                                                                e.printStackTrace();
                                                                //pb.setVisibility(ProgressBar.INVISIBLE);
                                                                return;
                                                            } else {
                                                                // run a background job and once complete
                                                                //pb.setVisibility(ProgressBar.INVISIBLE);
                                                            }
                                                        }
                                                    }
        );


    }




    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }}}






















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



