package com.example.give4friends.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.json.JSONArray;

@ParseClassName("User")
public class User extends ParseObject {
    public static final String KEY_USERNAME = "username";
    public static final String KEY_ID = "objectId";
    public static final String KEY_BIO  = "bio";
    public static final String KEY_FIRSTNAME  = "firstName";
    public static final String KEY_LASTNAME  = "lastName";
    public static final String KEY_TOTAL_DONATED  = "totalDonated";
    public static final String KEY_TOTAL_RAISED  = "totalRaised";
    public static final String KEY_FAVORITE_CHARITIES  = "favCharities";
    public static final String KEY_PROFILE_IMAGE = "profileImage";

    public String getKeyUsername() {
        return getString(KEY_USERNAME);
    }

    public void setKeyUsername(String username){
        put(KEY_USERNAME, username);
    }


    public String getKeyId() {
        return getString(KEY_ID);
    }


    public String getKeyBio() {
        return getString(KEY_BIO);
    }

    public void setKeyBio(String bio){
        put(KEY_BIO, bio);
    }

    public String getKeyFirstname() {
        return getString(KEY_FIRSTNAME);
    }

    public void setKeyFirstname(String name){
        put(KEY_FIRSTNAME, name);
    }

    public String getKeyLastname() {
        return getString(KEY_LASTNAME);
    }

    public void setKeyLastname(String name){
        put(KEY_LASTNAME, name);
    }

    public Number getKeyTotalDonated() {
        return getNumber(KEY_TOTAL_DONATED);
    }

    // possibly want an increment function rather than set?
    public void setKeyTotalDonated(double amount){
        put(KEY_TOTAL_DONATED, amount);
    }

    public Number getKeyTotalRaised() {
        return getNumber(KEY_TOTAL_RAISED);
    }

    public void setKeyTotalRaised(double amount){
        put(KEY_TOTAL_RAISED, amount);
    }


    public JSONArray getKeyFavoriteCharities() {
        return getJSONArray(KEY_FAVORITE_CHARITIES);
    }

    public void addKeyFavoriteCharites(JSONArray list){
        add(KEY_FAVORITE_CHARITIES, list);
    }


    public ParseFile getKeyProfileImage() {
        return getParseFile(KEY_PROFILE_IMAGE);
    }

    public void setKeyProfileImageImage(ParseFile image){
        put(KEY_PROFILE_IMAGE, image);
    }
}
