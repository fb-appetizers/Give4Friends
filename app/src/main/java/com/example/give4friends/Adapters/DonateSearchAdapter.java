package com.example.give4friends.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.give4friends.DonateFinalActivity;
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

import static com.example.give4friends.DonateActivity.charity;

public class DonateSearchAdapter extends RecyclerView.Adapter<DonateSearchAdapter.ViewHolder> {

    private List<CharityAPI> mCharity;
    private Context context;
    private Charity newCharity;

    public DonateSearchAdapter(List<CharityAPI> mCharity) {
        this.mCharity = mCharity;
    }


    @Override
    public DonateSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View charityView = inflater.inflate(R.layout.item_donate_search, parent, false);

        // Return a new holder instance
        DonateSearchAdapter.ViewHolder viewHolder = new DonateSearchAdapter.ViewHolder(charityView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DonateSearchAdapter.ViewHolder holder, int position) {
        CharityAPI charity = mCharity.get(position);

        holder.charityName.setText(charity.getName());

        holder.charityCause.setText(Html.fromHtml("<font color=\"#434040\"><b>Cause:</b></font> " + charity.getCauseName()));

        Glide.with(context)
                .load(R.drawable.charity_temp_profile)
                .into(holder.charityImage);
    }

    @Override
    public int getItemCount() {
        return mCharity.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView charityName;
        public TextView charityCause;
        public ImageView charityImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            charityName = itemView.findViewById(R.id.charityName);
            charityCause = itemView.findViewById(R.id.charityCause);
            charityImage = itemView.findViewById(R.id.charityImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                final CharityAPI selectedCharity = mCharity.get(position);

                ParseQuery<Charity> charityParseQuery = new ParseQuery<Charity>(Charity.class);
                charityParseQuery.include(Charity.KEY_CHARITY_ID);

                charityParseQuery.whereEqualTo("charityName", selectedCharity.getEin());

                charityParseQuery.getFirstInBackground(new GetCallback<Charity>() {
                    @Override
                    public void done(Charity object, ParseException e) {
                        if (e != null) {
                            if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                                addNewCharity(selectedCharity);
                            } else {
                                Log.e("CharitySearchAdapter", "Error with query of charity");
                            }
                        } else {
                            charity = object;
                        }
                    }
                });

                Intent intent = new Intent(view.getContext(), DonateFinalActivity.class);
                view.getContext().startActivity(intent);
            }
        }
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
                    charity = newCharity;
                } else {
                    Log.d("CharitySearchAdapter", "Invalid charity");
                    e.printStackTrace();
                }
            }
        });
    }
}
