package com.example.give4friends.Cutom_Classes;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.give4friends.DonateActivity;
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;

import java.util.Date;

import static com.example.give4friends.DonateActivity.charityName2;

public class CustomDialogProfileImage extends DialogFragment {


    public ImageView ivProfile;
    public String imageURL;
    public Date imageDate;


    public CustomDialogProfileImage(String imageURL, Date createdAt) {
        this.imageURL = imageURL;
        this.imageDate = createdAt;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_profile,container,false);

        ivProfile = view.findViewById(R.id.ivProfile);

        // The name in the dialog isn't clickable but we can change this later
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);

        if(imageURL!=null) {
            Glide.with(getContext())
                    .load(imageURL)
                    .apply(new RequestOptions()
                            .transforms(new RoundedCorners(20))

//                        .placeholder(R.drawable.user_outline_24)
                            .signature(new ObjectKey(imageDate))
                            .error(R.drawable.user_outline_24))
                    .into(ivProfile);
        }else{
            Glide.with(getContext())
                    .load(R.drawable.user_outline_24)
                    .apply(new RequestOptions()
                            .transforms(new CenterCrop(), new RoundedCorners(20))
                            .circleCropTransform()
                            .placeholder(R.drawable.user_outline_24)
                            .error(R.drawable.user_outline_24))
                    .into(ivProfile);


        }

        return view;
    }

}
