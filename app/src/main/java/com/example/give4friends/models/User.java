package com.example.give4friends.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.List;

@ParseClassName("User")
public class User extends ParseUser {
    public static final String KEY_USERNAME = "username";
    public static final String KEY_ID = "objectId";
    public static final String KEY_BIO  = "bio";
    public static final String KEY_FIRST_NAME  = "firstName";
    public static final String KEY_LAST_NAME  = "lastName";
    public static final String KEY_TOTAL_DONATED  = "totalDonated";
    public static final String KEY_TOTAL_RAISED  = "totalRaised";
    public static final String KEY_FAVORITE_CHARITIES  = "favCharities";
    public static final String KEY_PROFILE_IMAGE = "profileImage";
    public static final String KEY_CHARITY_ARRAY = "charityArray";

    public User(){}

    public String getKeyFirstName() {
        return getString(KEY_FIRST_NAME);
    }

    public String getKeyLastName() {
        return getString(KEY_LAST_NAME);
    }

    public List<Charity> getKeyCharityArray() {

        return getList(KEY_CHARITY_ARRAY);
    }

    public void setKeyFirstName(String firstName) { put(KEY_FIRST_NAME, firstName); }

    public void setKeyLastName(String lastName) { put(KEY_LAST_NAME, lastName); }

    public void setKeyCharityArray(List<Charity> charityArray) { put(KEY_CHARITY_ARRAY, charityArray); }

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
        return getString(KEY_FIRST_NAME);
    }

    public void setKeyFirstname(String name){
        put(KEY_FIRST_NAME, name);
    }

    public String getKeyLastname() {
        return getString(KEY_LAST_NAME);
    }

    public void setKeyLastname(String name){
        put(KEY_LAST_NAME, name);
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
