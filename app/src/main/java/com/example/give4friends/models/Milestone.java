package com.example.give4friends.models;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.example.give4friends.Adapters.DonateAdapter;
import com.example.give4friends.R;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public final class Milestone {

    static List<Pair<String, Integer>> milestones = Arrays.asList(Pair.create("First Donation", R.drawable.ic_filled_heart_80),
            Pair.create("First Raised", R.drawable.ic_happy_face_80),
            //check after transaction
            Pair.create("First Friend", R.drawable.ic_first_friend_80px),
            Pair.create("Raised $20", R.drawable.ic_accessibility_80),
            Pair.create("Donated $20", R.drawable.ic_filled_money_80),
            Pair.create("Used For A Year", R.drawable.ic_cake_80),
            Pair.create("Made 10 Friends", R.drawable.ic_people_80)

            //check after adding friend - make sure to not do it twice if they remove their friend
             /*,
            ,
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


    public static void milestoneAchieved(String milestone, @NonNull DonateAdapter.ViewHolder holder, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Congrats!!! You have achieved the " + milestone + " milestone");
        builder.setMessage("After 2 seconds, this dialog will be closed automatically!");
        builder.setCancelable(true);

        final AlertDialog dlg = builder.create();
        dlg.show();
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                dlg.dismiss(); // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 2000); // after 2 second (or 2000 miliseconds), the task will be active

    }


}

