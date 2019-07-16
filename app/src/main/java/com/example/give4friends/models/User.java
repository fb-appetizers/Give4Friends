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

    public static String getKeyUsername() {
        return KEY_USERNAME;
    }

    public String getKeyId() {
        return getString(KEY_ID);
    }


    public String getKeyBio() {
        return getString(KEY_BIO);
    }

    public String getKeyFirstname() {
        return getString(KEY_FIRSTNAME);
    }

    public String getKeyLastname() {
        return getString(KEY_LASTNAME);
    }

    public Number getKeyTotalDonated() {
        return getNumber(KEY_TOTAL_DONATED);
    }

    public Number getKeyTotalRaised() {
        return getNumber(KEY_TOTAL_RAISED);
    }

    public JSONArray getKeyFavoriteCharities() {
        return getJSONArray(KEY_FAVORITE_CHARITIES);
    }

    public ParseFile getKeyProfileImage() {
        return getParseFile(KEY_PROFILE_IMAGE);
    }
}
