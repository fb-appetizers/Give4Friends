package com.example.give4friends.models;

import android.util.Pair;

import com.example.give4friends.R;

import java.util.Arrays;
import java.util.List;

public final class Milestone {

    static List<Pair<String, Integer>> milestones = Arrays.asList(Pair.create("First Donation", R.drawable.ic_filled_heart_80),
            Pair.create("First Raised", R.drawable.ic_happy_face_80),
            //check after transaction
            Pair.create("First Friend", R.drawable.ic_first_friend_80px)
            //check after adding friend - make sure to not do it twice if they remove their friend
             /*Pair.create("Raised $20", R.drawable.ic_baseline_star_rate_18px),
            Pair.create("Donated $20", R.drawable.ic_baseline_star_rate_18px),
            Pair.create("Raised $50", R.drawable.ic_baseline_star_rate_18px),
            Pair.create("Donated $100", R.drawable.ic_baseline_star_rate_18px),
            Pair.create("Raised $100", R.drawable.ic_baseline_star_rate_18px),
            Pair.create("Donated $100", R.drawable.ic_baseline_star_rate_18px)*/


            /*Pair.create("Raised $250", R.drawable.ic_baseline_star_rate_18px),
            Pair.create("Raised $500", R.drawable.ic_baseline_star_rate_18px),
            Pair.create("Raised $1,000", R.drawable.ic_baseline_star_rate_18px),
            Pair.create("Raised $5,000", R.drawable.ic_baseline_star_rate_18px),
            Pair.create("Raised $10,000", R.drawable.ic_baseline_star_rate_18px)*/
    );



    public static List<Pair<String, Integer>> getAllMilestones(){
        final List<Pair<String, Integer>> milestones1 = milestones;
        return milestones1;
    }


}

