package com.example.give4friends.Adapters;

import android.content.Context;

import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.give4friends.DonateActivity;
import com.example.give4friends.R;

import com.example.give4friends.models.CharityAPI;

import java.util.List;

public class CharitySearchAdapter extends RecyclerView.Adapter<CharitySearchAdapter.ViewHolder> {

    private List<CharityAPI> mCharity;
    private boolean remove_links;
    private Context context;

    public CharitySearchAdapter(List<CharityAPI> mCharity, boolean is_in_donate_charity_search) {
        this.mCharity = mCharity;
        //This field is to tell whether the donate charity search page called this class. If so then
        //hide the more information text field along with the Donate Now page
        this.remove_links = is_in_donate_charity_search;
    }

    @Override
    public CharitySearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View charityView = inflater.inflate(R.layout.item_charity_search, parent, false);
        // Return a new holder instance
        CharitySearchAdapter.ViewHolder viewHolder = new CharitySearchAdapter.ViewHolder(charityView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CharitySearchAdapter.ViewHolder holder, int position) {
        CharityAPI charity = mCharity.get(position);

        holder.tvCharityName.setMovementMethod(LinkMovementMethod.getInstance());

        holder.tvCharityName.setText(Html.fromHtml("<a href=\'"+charity.getWebsiteUrl()+"\'>"
                +charity.getName() + " ("
                + charity.getCategoryName() + ")"+ "</a>"));

        holder.tvMission.setText(Html.fromHtml(charity.getMission()));

        holder.tvCause.setText(Html.fromHtml("<font color=\"#434040\"><b>Cause:</b></font> "+charity.getCauseName()));

        Glide.with(context)
                .load(charity.getRatingsUrl())
                .into(holder.ivRating);
    }

    @Override
    public int getItemCount() {
        return mCharity.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tvCharityName;
        public TextView tvMission;
        public TextView tvCause;
        public ImageView ivRating;
        public TextView tvMoreInfo;
        public TextView tvDonateNow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvCharityName = itemView.findViewById(R.id.tvCharityName);
            tvMission = itemView.findViewById(R.id.tvMission);

            tvCause = itemView.findViewById(R.id.tvCause);
            ivRating = itemView.findViewById(R.id.ivRating);
            tvMoreInfo = itemView.findViewById(R.id.tvMoreInfo);
            tvDonateNow = itemView.findViewById(R.id.tvDonateNow);

            tvDonateNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DonateActivity.class);
                    intent.putExtra("donateNow", true);
                    view.getContext().startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View view) {
        }
    }
}
