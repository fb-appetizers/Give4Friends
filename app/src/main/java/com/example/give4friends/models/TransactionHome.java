package com.example.give4friends.models;

import com.parse.ParseFile;

import java.util.Date;

public class TransactionHome {

    public String friendName;
    public ParseFile friendProfile;
    public String objectId;
    public String donorName;
    public ParseFile donorProfile;
    public String message;
    public Number amountDonated;
    public Number likesCount;
    public String charityName;
    public Date createdAt;

    public ParseFile getFriendProfile() {
        return friendProfile;
    }

    public void setFriendProfile(ParseFile friendProfileURL) {
        this.friendProfile = friendProfileURL;
    }

    public ParseFile getDonorProfile() {
        return donorProfile;
    }

    public void setDonorProfile(ParseFile donorProfile) {
        this.donorProfile = donorProfile;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Number getAmountDonated() {
        return amountDonated;
    }

    public void setAmountDonated(Number amountDonated) {
        this.amountDonated = amountDonated;
    }

    public Number getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Number likesCount) {
        this.likesCount = likesCount;
    }


    public String getCharityName() {
        return charityName;
    }

    public void setCharityName(String charityName) {
        this.charityName = charityName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }






    public static TransactionHome fromParse(Transaction transaction){

        TransactionHome transactionHome = new TransactionHome();

        transactionHome.setAmountDonated(transaction.getKeyAmountDonated());
        transactionHome.setCharityName(transaction.getKeyCharityName());
        transactionHome.setDonorName(transaction.getKeyDonorName());
        transactionHome.setLikesCount(transaction.getKeyLikesCount());
        transactionHome.setMessage(transaction.getKeyMessage());
        transactionHome.setDonorProfile(transaction.getKeyDonorImage());
        transactionHome.setFriendProfile(transaction.getKeyFriendImage());
        transactionHome.setFriendName(transaction.getKeyFriendName());

        return transactionHome;

    }
}
