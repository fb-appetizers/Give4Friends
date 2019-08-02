package com.example.give4friends.models;

import android.media.Image;
import android.util.Pair;

import com.example.give4friends.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Milestone {

    static List<Pair<String, Integer>> milestones = Arrays.asList(Pair.create("First Donation", R.drawable.ic_baseline_star_rate_18px),
            Pair.create("First Raised", R.drawable.ic_baseline_star_rate_18px),
            Pair.create("Raised $20", R.drawable.ic_baseline_star_rate_18px));


    public static List<Pair<String, Integer>> getAllMilestones(){
        final List<Pair<String, Integer>> milestones1 = milestones;
        return milestones1;
    }


}

