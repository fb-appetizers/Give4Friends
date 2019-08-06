package com.example.give4friends.models;

import androidx.preference.ListPreference;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;



import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParseClassName("Transaction")
public class Transaction extends ParseObject{

    public static final String KEY_FRIEND_ID = "friendID";
    public static final String KEY_OBJECT_ID= "objectId";
    public static final String KEY_FRIEND_NAME = "friendName";
    public static final String KEY_DONOR_NAME= "donorName";
    public static final String KEY_CHARITY_NAME= "charityName";

    public static final String KEY_DONOR_ID = "donorID";
    public static final String KEY_MESSAGE  = "message";
    public static final String KEY_AMOUNT_DONATED  = "amountDonated";
    public static final String KEY_LIKES_COUNT  = "likesCount";
    public static final String KEY_LIKES_USERS  = "likesUsers";
    public static final String KEY_CHARITY_ID  = "charityId";
    public static final String KEY_CREATED_AT  = "createdAt";
    public static final String KEY_DONOR_IMAGE  = "donorProfile";
    public static final String KEY_FRIEND_IMAGE  = "friendProfile";

    public Transaction(){}

    public ParseUser getKeyFriendId() {
        return getParseUser(KEY_FRIEND_ID);
    }

    public void setKeyFriendId(ParseUser friend){
        put(KEY_FRIEND_ID, friend);
    }
    public ParseUser getKeyDonorId() {
        return getParseUser("donorID");

    }
    public void setKeyDonorId(ParseUser donor){
        put(KEY_DONOR_ID, donor);
    }

    public String getKeyMessage() {
        return getString(KEY_MESSAGE);
    }

    public void setKeyMessage(String message){
        put(KEY_MESSAGE, message);
    }

    public Number getKeyAmountDonated() {
        return getNumber(KEY_AMOUNT_DONATED);
    }

    public void setKeyAmountDonated(Number amount){
        put(KEY_AMOUNT_DONATED,amount );
    }

    public Number getKeyLikesCount() {
        return getNumber(KEY_LIKES_COUNT);
    }

    public void incrementLikes(int amount){
        increment(KEY_LIKES_COUNT, amount );
    }

    public List getKeyLikesUsers() {
        return getList(KEY_LIKES_USERS);
    }

    public void setKeyLikesUsers(List<String> list){
        put(KEY_LIKES_USERS, list);
    }

    public void addLikesUser(String user){
        add(KEY_LIKES_USERS, user);
    }

    public Charity getKeyCharityId() {
        return (Charity) getParseObject(KEY_CHARITY_ID);
    }

    public void setKeyCharityId(Charity charity){
        put(KEY_CHARITY_ID, charity);
    }


}
