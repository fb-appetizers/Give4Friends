package com.example.give4friends.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.Date;

@ParseClassName("Transaction")
public class Transaction extends ParseObject{

    public static final String KEY_FRIEND_ID = "friendID";
    public static final String KEY_OBJECT_ID= "objectId";
    public static final String KEY_DONOR_ID = "donorID";
    public static final String KEY_MESSAGE  = "message";
    public static final String KEY_AMOUNT_DONATED  = "amountDonated";
    public static final String KEY_LIKES_COUNT  = "likesCount";
    public static final String KEY_LIKES_USERS  = "likesUsers";
    public static final String KEY_CHARITY_ID  = "charityID";
    public static final String KEY_CREATED_AT  = "createdAt";



    public Transaction(){}


    public User getKeyFriendId() {
        return (User) getParseUser(KEY_FRIEND_ID);
    }

    public void setKeyFriendId(ParseUser friend){
        put(KEY_FRIEND_ID, friend);
    }

    public String getKeyObjectId() {
        return getString(KEY_OBJECT_ID);
    }

    public User getKeyDonorId() {
        return (User) getParseUser(KEY_DONOR_ID);
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

    public void incrementLikes(){
        increment(KEY_LIKES_COUNT, 1 );
    }

    public JSONArray getKeyLikesUsers() {
        return getJSONArray(KEY_LIKES_USERS);
    }

    public Charity getKeyCharityId() {
        return (Charity) getParseObject(KEY_CHARITY_ID);
    }

    public void setKeyCharityId(Charity charity){
        put(KEY_CHARITY_ID, charity);
    }

    public Date getKeyCreatedAt(){
        return getDate(KEY_CREATED_AT);
    }
}
