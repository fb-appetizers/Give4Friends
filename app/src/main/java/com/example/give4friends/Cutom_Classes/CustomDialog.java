package com.example.give4friends.Cutom_Classes;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.give4friends.DonateActivity;
import com.example.give4friends.DonateFinalActivity;
import com.example.give4friends.Fragments.Charity_Profile_Fragment;
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import static com.example.give4friends.DonateActivity.charityName2;
import static com.example.give4friends.DonateActivity.currentCharity;

public class CustomDialog extends DialogFragment {


    private static final String TAG = "CustomDialog";

    public CharityAPI charity;
    private Charity newCharity;

    public TextView tvCharityName;
    public TextView tvMission;

    public TextView tvCause;
    public ImageView ivRating;
    public TextView tvDonateNow;

    public TextView tvMoreInfo;


    public CustomDialog(CharityAPI charity) {
        this.charity = charity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.item_charity_search,container,false);
        tvCharityName = view.findViewById(R.id.tvCharityName);
        tvMission = view.findViewById(R.id.tvMission);

        tvCause = view.findViewById(R.id.tvCause);
        ivRating = view.findViewById(R.id.ivRating);
        tvDonateNow = view.findViewById(R.id.tvDonateNow);
        tvMoreInfo = view.findViewById(R.id.tvMoreInfo);

        // The name in the dialog isn't clickable but we can change this later
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        tvCharityName.setText(charity.getName() + " (" + charity.getCategoryName() + ")");

        tvDonateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentCharity();

                charityName2 = charity.getName();
                Intent intent = new Intent(view.getContext(), DonateActivity.class);
                intent.putExtra("donateNow", true);
                view.getContext().startActivity(intent);
            }
        });




        tvMission.setText(Html.fromHtml(charity.getMission()));


        tvCause.setText(Html.fromHtml("<font color=\"#434040\"><b>Cause:</b></font> "+charity.getCauseName()));

        Glide.with(getContext())
                .load(charity.getRatingsUrl())
                .into(ivRating);

        return view;
    }

    private void setCurrentCharity(){
        ParseQuery<Charity> charityParseQuery = new ParseQuery<Charity>(Charity.class);
        charityParseQuery.include(Charity.KEY_CHARITY_ID);

        charityParseQuery.whereEqualTo("charityName", charity.getEin());

        charityParseQuery.getFirstInBackground(new GetCallback<Charity>() {
            @Override
            public void done(Charity object, ParseException e) {
                if (e != null) {
                    if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                        addNewCharity(charity);
                        return;
                    } else {
                        Log.e("CharitySearchAdapter", "Error with query of charity");
                    }
                } else {
                    currentCharity = object;
                }
            }
        });
    }

    private void addNewCharity(CharityAPI selectedCharity) {
        newCharity = new Charity();
        newCharity.setKeyCategoryName(selectedCharity.getCategoryName());
        newCharity.setKeyCauseName(selectedCharity.getCauseName());
        newCharity.setKeyCharityID(selectedCharity.getEin());
        newCharity.setKeyMission(selectedCharity.getMission());
        newCharity.setKeyName(selectedCharity.getName());
        newCharity.setKeyRatingURL(selectedCharity.getRatingsUrl());
        newCharity.setKeyUrl(selectedCharity.getWebsiteUrl());

        newCharity.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("CharitySearchAdapter", "Created new charity");
                    currentCharity = newCharity;
                } else {
                    Log.d("CharitySearchAdapter", "Invalid charity");
                    e.printStackTrace();
                }
            }
        });
    }
}
