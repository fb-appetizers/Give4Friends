package com.example.give4friends.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

@ParseClassName("Charity")
public class Charity extends ParseObject {

    public static final String KEY_NAME = "name";
    public static final String KEY_ID= "objectId";
    public static final String KEY_MISSION  = "mission";
    public static final String KEY_RATING  = "ratingURL";
    public static final String KEY_CATEGORY_NAME  = "categoryName";
    public static final String KEY_URL  = "websiteUrl";
    public static final String KEY_CAUSE_NAME  = "causeName";
    public static final String KEY_CHARITY_ID  = "charityName";
    public static final String KEY_LIKES_COUNT  = "likesCount";
    public static final String KEY_LIKES_USERS  = "likesUsers";
    public static final String KEY_WEBSITE = "websiteUrl";
    public static final String KEY_NUM_LIKES = "likesCount";
    public static final String KEY_CODE = "payPal";

    public Charity(){}

    public String getKeyName() {

        try {
            return fetchIfNeeded().getString(KEY_NAME);
        } catch (ParseException e) {
            e.printStackTrace();
            return("missing name");
        }
    }

    public void setKeyName(String name){ put(KEY_NAME, name); }

    public String getKeyObjectId() {
        return getString(KEY_ID);
    }

    public void setKeyCode(String code){ put(KEY_CODE, code); }

    public String getKeyCode() {
        return getString(KEY_CODE);
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

    public String getKeyWebsiteURL() {
        return getString(KEY_WEBSITE);
    }

    public void setKeyWebsiteURL(String url) {
        put(KEY_WEBSITE, url);
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

    public Number getKeyLikesCount() {
        return getNumber(KEY_LIKES_COUNT);
    }

    public void incrementLikes(int amount){
        increment(KEY_LIKES_COUNT, amount );
    }


    public List getKeyLikesUsers() {
        return getList(KEY_LIKES_USERS);
    }

    public void setKeyLikesUsers(List<User> list){
        put(KEY_LIKES_USERS, list);
    }

    public void addLikesUser(String user){
        add(KEY_LIKES_USERS, user);
    }

    public int getKeyNumLikes(){
        return getInt(KEY_NUM_LIKES);
    }
}
