package com.example.give4friends;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.icu.util.Calendar;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

//import com.braintreepayments.cardform.view.CardForm;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.give4friends.models.ProfilePicture;
import com.example.give4friends.models.User;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {
    public final String APP_TAG = "SignUpActivity";

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText userName;
    private TextView addProfilePic;
    private Button signUp;
    private ImageView profilePic;
    private ShowHidePasswordEditText passWord;
    Context context;


    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int SELECT_IMAGE_REQUEST_CODE = 1111;
    private static final String URL_HEADER = "https://give4friends.000webhostapp.com/pictures/";
    private File photoFile;
    private String photoFileName = "image.png";
    private Bitmap photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        userName = findViewById(R.id.userName);
        passWord = findViewById(R.id.password);
        profilePic = findViewById(R.id.profilePic);
        signUp = findViewById(R.id.signUpBtn);
        addProfilePic = findViewById(R.id.addProfilePic);

        // Make sure to logout before you sign up !!
        ParseUser.logOut();

        Glide.with(this)
                .load(R.drawable.com_facebook_profile_picture_blank_portrait)
                .apply(new RequestOptions()
                        .transforms(new CenterCrop(), new RoundedCorners(20))
                        .circleCropTransform()
                        .error(R.drawable.user_outline_24))
                .into(profilePic);

        context = this;
        addProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onLaunchCamera();
                changePhoto();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String first = firstName.getText().toString();
                final String last = lastName.getText().toString();
                final String emailInput = email.getText().toString();
                final String username = userName.getText().toString();
                final String password = passWord.getText().toString();

                signUp(first, last, emailInput, username, password);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        .into(profilePic);


            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == SELECT_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri photoUri = data.getData();
                try {
                    photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
//                    photo = ProfilePicture.RotateBitmapFromBitmap(photo,90);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //photoFile = new File(photoUri.getPath());
                photoFile = new File(ProfilePicture.getRealPathFromURI(context, photoUri));


                Glide.with(context)
                        .load(photo)
                        .apply(new RequestOptions()
                                .transforms(new CenterCrop(), new RoundedCorners(20))
                                .circleCropTransform()
                                .placeholder(R.drawable.user_outline_24)
                                .error(R.drawable.user_outline_24))
                        .into(profilePic);




            }
        }

    }


    private void signUp(String firstName, String lastName, String email, String username, String password) {
        // Create the ParseUser
        ParseUser user = new ParseUser();


        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.put("firstName", firstName);
        user.put("lastName", lastName);

        Context context = getApplicationContext();
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(context,"Success sign-up", Toast.LENGTH_SHORT).show();
                    Log.d("signUp", "Sign Up Successful");
                    ParseUser currentUser = ParseUser.getCurrentUser();

                    if(photo!=null) {
                        String imagePath = ParseUser.getCurrentUser().getUsername() + "_profileImage";
                        new ProfilePicture.UploadImage(photo, imagePath, context, true).execute();
                        currentUser.put("profileImageURL", URL_HEADER + imagePath + ".JPG");
                        currentUser.put("profileImageCreatedAt", Calendar.getInstance().getTime());
                    }

                    currentUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Intent intent = new Intent(SignUpActivity.this, CreditCardInfo.class);
                                startActivity(intent);
                            } else {
                                e.printStackTrace();
                                Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                    e.printStackTrace();
                    Toast.makeText(context,e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });

    }



    public void changePhoto(){
        String[] options = {"Take photo", "Choose from gallery"};
        android.app.AlertDialog.Builder dialog = new AlertDialog.Builder(this);
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
        photoFile = ProfilePicture.getPhotoFileUri(photoFileName, this);


        Uri fileProvider = FileProvider.getUriForFile(this, "com.example.give4friends", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);


        if (intent.resolveActivity(getPackageManager()) != null) {

            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public void onLaunchSelect() {

        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE);
        }
    }

}

