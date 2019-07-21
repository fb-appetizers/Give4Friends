package com.example.give4friends.Cutom_Classes;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;

public class CustomDialog extends DialogFragment {


    private static final String TAG = "CustomDialog";

    public CharityAPI charity;

    public TextView tvCharityName;
    public TextView tvMission;

    public TextView tvCause;
    public ImageView ivRating;


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

        // The name in the dialog isn't clickable but we can change this later
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        tvCharityName.setText(charity.getName() + " (" + charity.getCategoryName() + ")");



        tvMission.setText(Html.fromHtml(charity.getMission()));


        tvCause.setText(Html.fromHtml("<font color=\"#434040\"><b>Cause:</b></font> "+charity.getCauseName()));

        Glide.with(getContext())
                .load(charity.getRatingsUrl())
                .into(ivRating);



        return view;
    }
}
