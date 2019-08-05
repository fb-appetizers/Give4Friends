package com.example.give4friends.models;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.give4friends.Adapters.CharityProfileAdapter;
import com.example.give4friends.Adapters.DonateSearchAdapter;
import com.example.give4friends.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public final class FavoriteCharities {

    public static void setUpFavorites(Charity parseCharity, ParseUser myUser, ImageButton ibCPLike, TextView tvCPLikedNum)
    {
        //check if user is in likes list
        List<User> array = parseCharity.getList("likesUsers");

        if (array == null || !(array.contains(myUser.getObjectId())) || array.size() == 0) {
            ibCPLike.setImageResource(R.drawable.ic_like_icon);
            ibCPLike.setColorFilter(Color.BLACK);
        } else {
            ibCPLike.setImageResource(R.drawable.ic_baseline_star_rate_18px);
            ibCPLike.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        }

        ibCPLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseRelation<ParseObject> relation = myUser.getRelation("favCharities");
                List<User> array = parseCharity.getList("likesUsers");
                if (array == null || !(array.contains(myUser.getObjectId()))) {
                    ibCPLike.setImageResource(R.drawable.ic_baseline_star_rate_18px);
                    ibCPLike.setColorFilter(Color.YELLOW);

                    //update parse
                    //updateUser
                    relation.add(parseCharity);
                    myUser.saveInBackground();

                    Integer num_likes = ((Integer)parseCharity.getKeyNumLikes()) + 1;
                    tvCPLikedNum.setText(num_likes.toString());
                    //increment likes for charity
                    parseCharity.incrementLikes(1);
                    //add user to array
                    parseCharity.addLikesUser(myUser.getObjectId());
                    parseCharity.saveInBackground();
                } else {
                    ibCPLike.setImageResource(R.drawable.ic_like_icon);
                    ibCPLike.setColorFilter(Color.BLACK);

                    //update user
                    relation.remove(parseCharity);
                    myUser.saveInBackground();

                    Integer num_likes = ((Integer)parseCharity.getKeyNumLikes()) - 1;
                    tvCPLikedNum.setText(num_likes.toString());

                    //update charity
                    parseCharity.incrementLikes(-1);
                    //add user to array
                    array.remove(myUser.getObjectId());
                    parseCharity.setKeyLikesUsers(array);
                    parseCharity.saveInBackground();
                }
            }
        });
    }
}
