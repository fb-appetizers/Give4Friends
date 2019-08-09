package com.example.give4friends.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.INotificationSideChannel;
import android.text.InputFilter;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.give4friends.Adapters.ProfilePagerAdapter;
import com.example.give4friends.Cutom_Classes.CustomDialogProfileImage;

import com.example.give4friends.LoginActivity;
import com.example.give4friends.R;
import com.example.give4friends.SettingsActivity;
import com.example.give4friends.models.Milestone;
import com.example.give4friends.models.ProfilePicture;
import com.example.give4friends.models.Transaction;
import com.google.android.material.tabs.TabLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class User_Profile_Fragment extends Fragment{
    int total = 0;

    private static final String URL_HEADER = "https://give4friends.000webhostapp.com/pictures/";
    private static final Integer MAX_BIO_LENGTH = 130;
    private ImageButton btEditBio;
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
    ProgressBar progressBarHome;
    private Bitmap photo;

    ParseUser myUser;
    boolean from_fragment;
    Context context;
    private File photoFile;
    private String photoFileName = "image.png";
    TabLayout FavMileToolbar;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    public User_Profile_Fragment(ParseUser myUser, boolean from_another_fragment) {
        this.myUser = myUser;
        this.from_fragment = from_another_fragment;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();
        btEditBio = view.findViewById(R.id.btEditProfile);
        FavMileToolbar = view.findViewById(R.id.FavMileToolbar);
        viewPager = view.findViewById(R.id.viewPager);

        FavMileToolbar.addTab(FavMileToolbar.newTab().setText("Favorites"));
        FavMileToolbar.addTab(FavMileToolbar.newTab().setText("Milestones"));

        FavMileToolbar.setTabGravity(FavMileToolbar.GRAVITY_FILL);

        pagerAdapter = new ProfilePagerAdapter(getChildFragmentManager(), FavMileToolbar.getTabCount(), myUser);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(FavMileToolbar));

        FavMileToolbar.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btChangePic = view.findViewById(R.id.btChangePic);

        if(!from_fragment) {
            configureToolbar();
        }else{
            configureToolbarStripped();
        }
        setHasOptionsMenu(true);

        btEditBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddItemDialog(context);
            }
        });

        btChangePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changePhoto();
            }
        });





        // Below for static elements of profile
        ivProfileImage = (ImageView) view.findViewById((R.id.ivProfileImage));
        tvUserName = (TextView) view.findViewById(R.id.tvName);
        tvBio = (TextView) view.findViewById(R.id.tvBio);
        tvTotalRaised = (TextView) view.findViewById((R.id.tvTotalRaised));
        tvTotalDonated = (TextView) view.findViewById((R.id.tvTotalDonated));
        tvFullName = (TextView) view.findViewById(R.id.tvFullName);

        tvUserName.setText("@" + myUser.getUsername());
        if(myUser.getString("bio") == null){
            tvBio.setText("Looks like you don't have a bio yet! Edit your bio to let your friends know what you are passionate about.");
        }
        else{
            tvBio.setText(myUser.getString("bio"));
        }
        tvBio.setEnabled(false);
        Number sum = myUser.getNumber("totalDonated");
        // error check
        if(sum == null){
            sum = 0;
        }
        tvTotalDonated.setText("Total Donated: $" + sum);
        //get total amount others donated on behalf of user
        getRaised();
        tvFullName.setText(myUser.getString("firstName") + " " + myUser.getString("lastName"));
        //Handles images

        final String imageURL = myUser.getString("profileImageURL");


        if (imageURL!=null) {
            Date imageDate = myUser.getDate("profileImageCreatedAt");
            Glide.with(context)
                    .load(imageURL)

                    .apply(new RequestOptions()
                            .transforms(new CenterCrop(), new RoundedCorners(20))
                            .circleCropTransform()
                            .placeholder(R.drawable.user_outline_24)
                            .error(R.drawable.user_outline_24)
                            .signature(new ObjectKey(imageDate))
                    )

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


        ivProfileImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Date imageDate = myUser.getDate("profileImageCreatedAt");
                CustomDialogProfileImage dialog = new CustomDialogProfileImage(imageURL, imageDate);
                dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "CustomDialogCharity");

                return false;
            }
        });
    }

    //add tool bar
    private void configureToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTextSize(30);
        toolbarTitle.setText("Profile");



        toolbar.setNavigationIcon(R.drawable.ic_settings);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
                ((Activity)getContext()).overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });
    }

    protected void configureToolbarStripped() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);


        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTextSize(24);
        toolbarTitle.setText(myUser.getUsername());

        toolbar.setNavigationIcon(R.drawable.ic_x);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pops back the fragment if you cancel
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater main_activity_inflater = getActivity().getMenuInflater();
        if(!from_fragment) {
            main_activity_inflater.inflate(R.menu.charity_menu, menu);
        }



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.likedTransactionsProfile:
                Fragment fragment1 = new Liked_Transactions_Fragment(ParseUser.getCurrentUser(), true);
                FragmentManager fragmentManager1 = ((AppCompatActivity)context).getSupportFragmentManager();
                fragmentManager1.beginTransaction().
                        replace(R.id.flContainer, fragment1)
                        .addToBackStack(null).commit();

                return true;
            case R.id.friends:
                Fragment fragment2 = new Friend_List_Fragment();
                FragmentManager fragmentManager2 = ((AppCompatActivity)context).getSupportFragmentManager();
                fragmentManager2.beginTransaction().
                        replace(R.id.flContainer, fragment2)
                        .addToBackStack(null).commit();
                return true;
            case R.id.useOffline:
                Toast.makeText(getContext(), "Use Offline selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logOut:
                Toast.makeText(getContext(), "logging out...", Toast.LENGTH_SHORT).show();
                logOut();
            default:

        }
        return true;
    }

    public void logOut(){
        ParseUser.logOut();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }



    @Override
    public void onActivityResult ( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                photo = ProfilePicture.rotateBitmapOrientation(photoFile.getAbsolutePath());


                Glide.with(context)
                        .load(photo)
                        .apply(new RequestOptions()
                                .transforms(new CenterCrop(), new RoundedCorners(20))
                                .circleCropTransform()
                                .placeholder(R.drawable.user_outline_24)
                                .error(R.drawable.user_outline_24))
                        .into(ivProfileImage);

                String imagePath = ParseUser.getCurrentUser().getUsername() + "_profileImage";

                new ProfilePicture.UploadImage(photo, imagePath, getContext(), false).execute();
                ProfilePicture.updatePhotoURL(ParseUser.getCurrentUser(),URL_HEADER + imagePath + ".JPG");

            } else { // Result was a failure

                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == SELECT_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri photoUri = data.getData();

                try {
                    photo = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                ExifInterface exifInterface = null;
                try {
                    InputStream inputStream = context.getContentResolver().openInputStream(photoUri);
                    photo = ProfilePicture.rotateBitmapOrientation(photo, inputStream);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Glide.with(context)
                        .load(photo)
                        .apply(new RequestOptions()
                                .transforms(new CenterCrop(), new RoundedCorners(20))
                                .circleCropTransform()
                                .placeholder(R.drawable.user_outline_24)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .error(R.drawable.user_outline_24))
                        .into(ivProfileImage);

                String imagePath = ParseUser.getCurrentUser().getUsername() + "_profileImage";
                new ProfilePicture.UploadImage(photo, imagePath, getContext(), false).execute();
                ProfilePicture.updatePhotoURL(ParseUser.getCurrentUser(),URL_HEADER + imagePath + ".JPG");

            }
        }

    }


    public void updateBio(String bio){
        ParseUser user = ParseUser.getCurrentUser();
        user.put("bio", bio);
        user.saveInBackground();
    }

    private void showAddItemDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        taskEditText.setText(myUser.getString("bio"));

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(MAX_BIO_LENGTH);
        taskEditText.setFilters(FilterArray);



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



    // this is inefficient and I will hopefully be able to come back and make it more efficient later - Jessica
    protected void getRaised(){

        //get query
        total = 0;
        ParseQuery<Transaction> postQueryFriend = new ParseQuery<Transaction>(Transaction.class)
                .whereEqualTo(Transaction.KEY_FRIEND_ID, ParseUser.getCurrentUser());
        List<ParseQuery<Transaction>> queries = new ArrayList<ParseQuery<Transaction>>();
        queries.add(postQueryFriend);
        ParseQuery<Transaction> mainQuery = ParseQuery.or(queries);
        mainQuery.findInBackground(new FindCallback<Transaction>() {
                                       //iterate through query
                                       @Override
                                       public void done(List<Transaction> objects, ParseException e) {
                                           if (e == null){
                                               for (int i = 0; i < objects.size(); ++i){
                                                   if(objects.get(i).getKeyAmountDonated() != null) {
                                                       total = (total + (int) (objects.get(i).getKeyAmountDonated()));
                                                   }
//                        transactionAdapter.notifyItemInserted(transactions.size() - 1);
                                               }
                                               if(total > 50){
                                                   Milestone.addMilestone("Raised $50", getContext());
                                               }
                                               if(total > 25 )
                                               {
                                                   Milestone.addMilestone("Raised $20", getContext());
                                               }
                                               if(total > 0){
                                                   Milestone.addMilestone("First Raised", getContext());
                                               }
                                               tvTotalRaised.setText("Total Raised: $" + total);
                                           }else {
                                               Log.e("MainActivity", "Can't get transaction");
                                               e.printStackTrace();
                                           }
                                       }
                                   }
        );

    }

    public void changePhoto(){
        String[] options = {"Take photo", "Choose from gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Change Profile Picture");
        dialog.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0) {
                    onLaunchCamera(); }
                else {
                    onLaunchSelect();
                }
            }
        });
        dialog.show();

    }

    private void onLaunchCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = ProfilePicture.getPhotoFileUri(photoFileName, getContext());


        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.example.give4friends", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);


        if (intent.resolveActivity(getContext().getPackageManager()) != null) {

            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public void onLaunchSelect() {

        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE);
        }
    }



}
