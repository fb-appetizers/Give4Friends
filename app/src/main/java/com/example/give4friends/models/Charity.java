package com.example.give4friends.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel

public class Charity {

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
// Returns a Charity from the expected JSON

    public static Charity fromJSON(JSONObject object) {
        Charity charity = new Charity();

        try {


            charity.name = object.getString("charityName");
            if (object.has("mission")) {
                charity.mission = object.getString("mission");
            }
            if (object.has("currentRating")) {
                charity.rating = object.getJSONObject("currentRating").getInt("rating");
                charity.ratingsUrl = object.getJSONObject("currentRating")
                        .getJSONObject("ratingImage")
                        .getString("large");
            }

            if (object.has("category")) {
                charity.categoryName = object.getJSONObject("category").getString("categoryName");
            }
            if (object.has("cause")) {
                charity.causeName = object.getJSONObject("cause").getString("causeName");
            }


            charity.ein = object.getString("ein");
            charity.websiteUrl = object.getString("websiteURL");


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return charity;
    }



    public static ArrayList<Charity> fromJSON(JSONArray array){

        ArrayList<Charity> charities = new ArrayList<>(array.length());

        for(int i = 0; i<array.length();i++){
            JSONObject charityJson;


            try {
                charityJson = array.getJSONObject(i);

            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            Charity charity = Charity.fromJSON(charityJson);

            if(charity!=null){
                charities.add(charity);
            }

        }

        return charities;

    }
}
