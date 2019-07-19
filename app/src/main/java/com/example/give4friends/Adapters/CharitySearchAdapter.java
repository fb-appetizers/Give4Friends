package com.example.give4friends.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.give4friends.R;
import com.example.give4friends.models.CharityAPI;

import java.util.List;

public class CharitySearchAdapter extends RecyclerView.Adapter<CharitySearchAdapter.ViewHolder> {

    private List<CharityAPI> mCharity;
    private Context context;

    public CharitySearchAdapter(List<CharityAPI> mCharity) {
        this.mCharity = mCharity;

    }


    @Override
    public CharitySearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View bookView = inflater.inflate(R.layout.item_charity_search, parent, false);

        // Return a new holder instance
        CharitySearchAdapter.ViewHolder viewHolder = new CharitySearchAdapter.ViewHolder(bookView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull CharitySearchAdapter.ViewHolder holder, int position) {
        CharityAPI charity = mCharity.get(position);
        holder.tvCharityName.setText(charity.getName() + " (" + charity.getCategoryName() + ")");
        holder.tvMission.setText(charity.getMission());


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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvCharityName = itemView.findViewById(R.id.tvCharityName);
            tvMission = itemView.findViewById(R.id.tvMission);

            tvCause = itemView.findViewById(R.id.tvCause);
            ivRating = itemView.findViewById(R.id.ivRating);

        }

        @Override
        public void onClick(View view) {

        }
    }
}
