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
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.give4friends.DonateActivity;
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import static com.example.give4friends.DonateActivity.charityName2;
import static com.example.give4friends.DonateActivity.currentCharity;

public class InfoDialog extends DialogFragment {

    private static final String TAG = "InfoDialog";
    public TextView giveWell;

    public InfoDialog() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_information, container, false);
        giveWell = view.findViewById(R.id.giveWell);

        // The name in the dialog isn't clickable but we can change this later
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);

        giveWell.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }

}