package com.example.give4friends;

import android.app.Application;

import com.example.give4friends.models.Charity;
import com.example.give4friends.models.User;
import com.parse.Parse;
import com.parse.ParseObject;


public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //ParseObject.registerSubclass(Charity.class);

            /*
            final Parse.Configuration configuration = new Parse.Configuration.Builder(this).build()
            */

            /*
            // Use for troubleshooting -- remove this line for production
            Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);


            // Use for monitoring Parse OkHttp traffic
            // Can be Level.BASIC, Level.HEADERS, or Level.BODY
            // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.networkInterceptors().add(httpLoggingInterceptor);
            */

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("give4friends") // should correspond to APP_ID env variable
                .clientKey("apitizersgiving4friends")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("http://give4friends.herokuapp.com/parse")
                .build();


        ParseObject.registerSubclass(Charity.class);
        ParseObject.registerSubclass(User.class);
        Parse.initialize(configuration);
            /*
            // New test creation of object below
            ParseObject testObject = new ParseObject("TestObject");
            testObject.put("foo", "bar");
            testObject.saveInBackground();
            */
    }
}



