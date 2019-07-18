package com.example.give4friends.models;


import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel

public class CharityAPI {

    public String name;
    public String mission;
    public Integer rating;
    public String ein; // Organization ID for the charity
    public String categoryName;
    public String causeName;
    public String websiteUrl;
    public String ratingsUrl;


    public String getName() {
        return name;
    }

    public String getMission() {
        return mission;
    }

    public Integer getRating() {
        return rating;
    }

    public String getEin() {
        return ein;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCauseName() {
        return causeName;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getRatingsUrl() {
        return ratingsUrl;
    }
// Returns a CharityAPI from the expected JSON

    public static CharityAPI fromJSON(JSONObject object) {
        CharityAPI charityAPI = new CharityAPI();

        try {


            charityAPI.name = object.getString("charityName");
            if (object.has("mission")) {
                charityAPI.mission = object.getString("mission");
            }
            if (object.has("currentRating")) {
                charityAPI.rating = object.getJSONObject("currentRating").getInt("rating");
                charityAPI.ratingsUrl = object.getJSONObject("currentRating")
                        .getJSONObject("ratingImage")
                        .getString("large");
            }

            if (object.has("category")) {
                charityAPI.categoryName = object.getJSONObject("category").getString("categoryName");
            }
            if (object.has("cause")) {
                charityAPI.causeName = object.getJSONObject("cause").getString("causeName");
            }


            charityAPI.ein = object.getString("ein");
            charityAPI.websiteUrl = object.getString("websiteURL");


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return charityAPI;
    }



    public static ArrayList<CharityAPI> fromJSON(JSONArray array){

        //This number controls how many charities will be saved on Parse whenever you call this
        //function

        Integer charitySavedSize = 3;

        ArrayList<CharityAPI> charities = new ArrayList<>(array.length());

        final List<String> currentCharityIDs = new ArrayList<String>();
        ParseUser mainUser = ParseUser.getCurrentUser();
        List<Charity> charityList = mainUser.getList("charityArray");



        // This step is to save the first three results of the request into the Parse Server
        // for the suggestions. Along with some of our own directly inputted into Parse


        // Before you save the entries onto Parse first check if they're any duplicates using a query
        ParseQuery<Charity> postQuery = new ParseQuery<Charity>(Charity.class);
        postQuery.include(Charity.KEY_ID);
        postQuery.addDescendingOrder("createdAt");


        try {
            for(Charity charity : postQuery.find()){
                currentCharityIDs.add(charity.getKeyCharityID());

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }



        for(int i = 0; i<array.length();i++){
            JSONObject charityJson;
            Charity charity = new Charity();


            try {
                charityJson = array.getJSONObject(i);

            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            CharityAPI charityAPI = CharityAPI.fromJSON(charityJson);

            if (charityAPI !=null && i<charitySavedSize && !currentCharityIDs.contains(charityAPI.getEin())){
                charity.setKeyName(charityAPI.getName());
                charity.setKeyCategoryName(charityAPI.getCategoryName());
                charity.setKeyMission(charityAPI.getMission());
                charity.setKeyCauseName(charityAPI.getMission());
                charity.setKeyUrl(charityAPI.getWebsiteUrl());
                charity.setKeyRatingURL(charityAPI.getRatingsUrl());
                charity.setKeyCharityID(charityAPI.getEin());


                // First save the newly created charity in background

                charity.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e!=null){
                            Log.e("Charity API", "error while saving a specific charity");
                        }else{
                            Log.d("Charity API", "Saved a specific charity successfully");
                        }
                    }
                });


                charityList.add(charity);

            }
            if(charityAPI !=null){
                charities.add(charityAPI);
            }

        }

        mainUser.put("charityArray", charityList);

        mainUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e("Charity API", "error while saving Charity List in use");
                }else{
                    Log.d("Charity API", "Saved Charity List successfully");
                }
            }
        });

        return charities;

    }
}
