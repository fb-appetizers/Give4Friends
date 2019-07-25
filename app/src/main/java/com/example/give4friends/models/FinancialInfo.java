package com.example.give4friends.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("financailInfo")
public class FinancialInfo extends ParseObject {
    public static final String KEY_CREDIT_CARD_NUM = "credit_card_num";
    public static final String KEY_EXP_DATE  = "expDate";
    public static final String KEY_CVC  = "cvc";
    public static final String KEY_ZIPCODE  = "zipCode";
    public static final String KEY_EMAIL  = "email";
    public static final String KEY_PHONE_NUM  = "phoneNumber";

    public FinancialInfo(){}

    public int getKeyCreditCardNum() {
        return getInt(KEY_CREDIT_CARD_NUM);
    }

    public void setKeyCreditCardNum(int credit){
        put(KEY_CREDIT_CARD_NUM, credit);
    }

    public int getKeyExpDate() {
        return getInt(KEY_EXP_DATE);
    }

    public void setKeyExpDate(int expDate){
        put(KEY_EXP_DATE, expDate);
    }

    public int getKeyCvc() {
        return getInt(KEY_CVC);
    }

    public void setKeyCvc(int cvc){
        put(KEY_CVC, cvc);
    }

    public int getKeyZipcode() {
        return getInt(KEY_ZIPCODE);
    }

    public void setKeyZipcode(int zip){
        put(KEY_ZIPCODE, zip);
    }

    public String getKeyEmail() {
        return getString(KEY_EMAIL);
    }

    public void setKeyEmail(String email){
        put(KEY_EMAIL, email);
    }

    public int getKeyPhoneNum() {
        return getInt(KEY_PHONE_NUM);
    }

    public void setKeyPhoneNum(int phoneNum){
        put(KEY_PHONE_NUM, phoneNum);
    }
}
