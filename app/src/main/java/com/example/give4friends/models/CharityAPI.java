package com.example.give4friends.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

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

        ArrayList<CharityAPI> charities = new ArrayList<>(array.length());

        for(int i = 0; i<array.length();i++){
            JSONObject charityJson;


            try {
                charityJson = array.getJSONObject(i);

            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            CharityAPI charityAPI = CharityAPI.fromJSON(charityJson);

            if(charityAPI !=null){
                charities.add(charityAPI);
            }

        }

        return charities;

    }
}
