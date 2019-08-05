package com.example.give4friends.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;


@ParseClassName("Comments")
public class Comments extends ParseObject {
    public static final String USER = "user";
    public static final String MESSAGE = "Message";


    public ParseUser getUser(){

        return getParseUser(USER);
    }

    public void setUser(ParseUser user){

        put(USER, user);
    }

    public String getMessage(){
        return getString(MESSAGE);
    }

    public void setMessage(String message){
        put(MESSAGE, message);
    }
}
