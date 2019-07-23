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

import java.io.Serializable;
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
    public List<String> likedUser;
    public Charity ParseCharity;

    public void setName(String name) {
        this.name = name;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setEin(String ein) {
        this.ein = ein;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCauseName(String causeName) {
        this.causeName = causeName;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public void setRatingsUrl(String ratingsUrl) {
        this.ratingsUrl = ratingsUrl;
    }

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

    public void setParseCharity(Charity charity){
        this.ParseCharity = charity;
    }

    public Charity getParseCharity(){
        return ParseCharity;
    }


// Returns a CharityAPI instance from the expected JSON

    public static CharityAPI fromParse(Charity charity){
        CharityAPI charityAPI = new CharityAPI();
        charityAPI.setName(charity.getKeyName());
        charityAPI.setCategoryName(charity.getKeyCategoryName());
        charityAPI.setCauseName(charity.getKeyCauseName());
        charityAPI.setEin(charity.getKeyCharityID());
        charityAPI.setMission(charity.getKeyMission());
        charityAPI.setRatingsUrl(charity.getKeyRatingURL());
        charityAPI.setWebsiteUrl(charity.getKeyUrl());

        return charityAPI;
    }

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
                        .getString("large");//set to small if you want a small image
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


        List<Charity> charityList = new ArrayList<Charity>();



        // This step is to save the first three results of the request into the Parse Server
        // for the suggestions. Along with some of our own directly inputted into Parse


        // Before you save the entries onto Parse first check if they're any duplicates using a query
        ParseQuery<Charity> postQuery = new ParseQuery<Charity>(Charity.class);
        postQuery.include(Charity.KEY_ID);
        postQuery.addDescendingOrder("createdAt");


        try {
            for(Charity charity : postQuery.find()){
                //Searches all of the current charities
                currentCharityIDs.add(charity.getKeyCharityID());

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }



        for(int i = 0; i<array.length();i++){
            JSONObject charityJson;



            try {

                charityJson = array.getJSONObject(i);

            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            CharityAPI charityAPI = CharityAPI.fromJSON(charityJson);

            // Add the charity if it's not null, if it's the first three search results and if its not already present
            if (charityAPI !=null && i<charitySavedSize && !currentCharityIDs.contains(charityAPI.getEin())){
                Charity charity = new Charity();
                charity.setKeyName(charityAPI.getName());
                charity.setKeyCategoryName(charityAPI.getCategoryName());
                charity.setKeyMission(charityAPI.getMission());
                charity.setKeyCauseName(charityAPI.getCauseName());
                charity.setKeyUrl(charityAPI.getWebsiteUrl());
                charity.setKeyRatingURL(charityAPI.getRatingsUrl());
                charity.setKeyCharityID(charityAPI.getEin());



                // First save the newly created charity in background if the charity is new.

                try {
                    charity.save();
                    charityList.add(charity);


                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
            //Possibly add if already in the list
//            if (charityAPI!=null && i<charitySavedSize && currentCharityIDs.contains(charityAPI.getEin())){
//
//
//
//                charityList.add(charity);
//            }
            if(charityAPI !=null){

                charities.add(charityAPI);
            }

        }

//        mainUser.put("charityArray", charityList);
        mainUser.addAllUnique("charityArray",charityList);

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
