package com.example.give4friends.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;

import java.util.List;

import static java.security.AccessController.getContext;

public class CharityViewAdapter extends RecyclerView.Adapter<CharityViewAdapter.ViewHolder> {

    private List<CharityAPI> mCharity;
    private Context context;

    public CharityViewAdapter(List<CharityAPI> mCharity) {
        this.mCharity = mCharity;

    }


    @Override
    public CharityViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View bookView = inflater.inflate(R.layout.item_charity, parent, false);

        // Return a new holder instance
        CharityViewAdapter.ViewHolder viewHolder = new CharityViewAdapter.ViewHolder(bookView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull CharityViewAdapter.ViewHolder holder, int position) {
        CharityAPI charity = mCharity.get(position);
        holder.tvCharityName.setText(charity.getName());
        holder.tvMission.setText(charity.getMission());
        holder.tvCategory.setText(charity.getCategoryName());
        holder.tvCause.setText(charity.getCauseName());

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
        public TextView tvCategory;
        public TextView tvCause;
        public ImageView ivRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvCharityName = itemView.findViewById(R.id.tvCharityName);
            tvMission = itemView.findViewById(R.id.tvMission);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvCause = itemView.findViewById(R.id.tvCause);
            ivRating = itemView.findViewById(R.id.ivRating);

        }

        @Override
        public void onClick(View view) {

        }
    }
}
