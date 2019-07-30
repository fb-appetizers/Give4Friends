package com.example.give4friends;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.give4friends.models.Transaction;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.facebook.ParseFacebookUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private Button signUp;
    private Button loginButton;
    private static final String EMAIL = "email";

    ProgressBar loadingProgressBar;
    CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.createAccount);
        loadingProgressBar = findViewById(R.id.loading);

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseFacebookUtils.initialize(getApplicationContext());

                fbLogin();
            }
        });

//        ((LoginButton) loginButton).setReadPermissions(Arrays.asList(EMAIL));

//        callbackManager = CallbackManager.Factory.create();
//
//        // Callback registration
//        ((LoginButton) loginButton).registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                Toast.makeText(getApplicationContext(), "Login failure. Please try again.", Toast.LENGTH_LONG).show();
//            }
//        });

        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(LoginActivity.this, Main_Fragment_Branch.class);
            startActivity(intent);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            loadingProgressBar.setVisibility(View.VISIBLE);
            login(username.getText().toString(), password.getText().toString());
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
            }
        });
    }

    private void fbLogin(){
        List<String> permissions = Arrays.asList("first_name", "last_name", "email", "id");

        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

//    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
//        @Override
//        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//
//        }
//    };
//
//    private void loadUserProfile(AccessToken newAccessToken){
//        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
//            @Override
//            public void onCompleted(JSONObject object, GraphResponse response) {
//                try {
//                    String firstName = object.getString("first_name");
//                    String lastName = object.getString("lastName");
//                    String id = object.getString("id");
//                    String email = object.getString("email");
//                    String imageUrl = "https://graph.facebook.com/"+id+"/picture?type=normal";
//
//                    ParseQuery<ParseUser> query = ParseUser.getQuery();
//                    query.whereEqualTo("email", email);
//
//                    query.findInBackground(new FindCallback<ParseUser>() {
//                        @Override
//                        public void done(List<ParseUser> objects, ParseException e) {
//                            if(e == null){
//                                login()
//                            }
//                            else{
//                                ParseUser newUser = new ParseUser();
//
//                                new
//                            }
//                        }
//                    });
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "first_name,last_name,email,id");
//        request.setParameters(parameters);
//        request.executeAsync();
//    }

    @Override
    protected void onResume() {
        super.onResume();

        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(LoginActivity.this, Main_Fragment_Branch.class);
            startActivity(intent);
        }
    }

    private void login(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null){
                    Log.d("login", "Login Successful");
                    Intent intent = new Intent(LoginActivity.this, Main_Fragment_Branch.class);
                    startActivity(intent);

                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    user.saveInBackground();
                    user.saveInBackground(); // does it save onto phone
                }
                else{
                    Log.e("login", "Login failure.");
                    Toast.makeText(getApplicationContext(), "Login Failure", Toast.LENGTH_SHORT).show();
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
                }
        });
    }
}
