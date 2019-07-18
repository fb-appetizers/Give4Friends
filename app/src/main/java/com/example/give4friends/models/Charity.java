package com.example.give4friends.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Charity")
public class Charity extends ParseObject {

    public static final String KEY_NAME = "name";
    public static final String KEY_ID= "objectId";
    public static final String KEY_MISSION  = "mission";
    public static final String KEY_RATING  = "ratingURL";
    public static final String KEY_CATEGORY_NAME  = "categoryName";
    public static final String KEY_URL  = "websiteUrl";
    public static final String KEY_CAUSE_NAME  = "causeName";
    public static final String KEY_CHARITY_ID  = "charityID";

    public Charity(){}

    public String getKeyName() {
        return getString(KEY_NAME);
    }

    public void setKeyName(String name){ put(KEY_NAME, name); }

    public String getKeyObjectId() {
        return getString(KEY_ID);
    }

    public String getKeyCharityID(){return getString(KEY_CHARITY_ID);}

    public void setKeyCharityID(String ID){put(KEY_CHARITY_ID, ID );}


    public String getKeyMission() {
        return getString(KEY_MISSION);
    }

    public void setKeyMission(String mission){
        put(KEY_MISSION, mission);
    }


    public String getKeyRatingURL() {
        return getString(KEY_RATING);
    }

    public void setKeyRatingURL(String url) {
        put(KEY_RATING, url);
    }


    public String getKeyCategoryName() {
        return getString(KEY_CATEGORY_NAME);
    }

    public void setKeyCategoryName(String name){
        put(KEY_CATEGORY_NAME, name);
    }

    public String getKeyUrl() {
        return getString(KEY_URL);
    }

    public void setKeyUrl(String url){
        put(KEY_URL, url);
    }

    public String getKeyCauseName() {
        return getString(KEY_CAUSE_NAME);
    }

    public void setKeyCauseName(String name){
        put(KEY_CAUSE_NAME, name);
    }
}
