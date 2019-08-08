package com.example.give4friends;

import android.app.Application;

import com.example.give4friends.models.Charity;
import com.example.give4friends.models.Comments;
import com.example.give4friends.models.Transaction;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;


public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax

        ParseObject.registerSubclass(Charity.class);
        ParseObject.registerSubclass(Transaction.class);
        ParseObject.registerSubclass(Comments.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("give4friends") // should correspond to APP_ID env variable
                .clientKey("apitizersgiving4friends")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("http://give4friends.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", "641550357369");
        installation.saveInBackground();

    }
}



