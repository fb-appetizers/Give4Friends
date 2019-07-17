package com.example.give4friends.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Charity")
public class Charity extends ParseObject {

    public static final String KEY_NAME = "name";
    public static final String KEY_ID= "objectId";
    public static final String KEY_MISSION  = "mission";
    public static final String KEY_RATING  = "rating";
    public static final String KEY_CATEGORY_NAME  = "categoryName";
    public static final String KEY_URL  = "websiteUrl";
    public static final String KEY_CAUSE_NAME  = "causeName";

    public String getKeyName() {
        return getString(KEY_NAME);
    }

    public void setKeyName(String name){
        put(KEY_NAME, name);
    }

    public String getKeyId() {
        return getString(KEY_ID);
    }


    public String getKeyMission() {
        return getString(KEY_MISSION);
    }

    public void setKeyMission(String mission){
        put(KEY_MISSION, mission);
    }

    public ParseFile getKeyRating() {
        return getParseFile(KEY_RATING);
    }

    public void setKeyRating(Number rating){
        put(KEY_RATING, rating);
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
