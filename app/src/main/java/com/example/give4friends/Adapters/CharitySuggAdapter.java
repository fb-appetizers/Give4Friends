package com.example.give4friends.Adapters;

import android.content.Context;
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

public class CharitySuggAdapter extends RecyclerView.Adapter<CharitySuggAdapter.ViewHolder> {


    private List<CharityAPI> mCharity;
    private Context context;

    public CharitySuggAdapter(List<CharityAPI> mCharity) {
        this.mCharity = mCharity;

    }


    @Override
    public CharitySuggAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View bookView = inflater.inflate(R.layout.item_charity_sugg, parent, false);

        // Return a new holder instance
        CharitySuggAdapter.ViewHolder viewHolder = new CharitySuggAdapter.ViewHolder(bookView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull CharitySuggAdapter.ViewHolder holder, int position) {
        CharityAPI charity = mCharity.get(position);
        holder.tvCharityNameSugg.setText(charity.getName());

        holder.tvCategorySugg.setText(charity.getCategoryName());
        holder.tvCauseSugg.setText(charity.getCauseName());

        Glide.with(context)
                .load(charity.getRatingsUrl())
                .into(holder.ivRatingSugg);


    }

    @Override
    public int getItemCount() {
        return mCharity.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tvCharityNameSugg;
        public TextView tvCategorySugg;
        public TextView tvCauseSugg;
        public ImageView ivRatingSugg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvCharityNameSugg = itemView.findViewById(R.id.tvCharityNameSugg);
            tvCategorySugg = itemView.findViewById(R.id.tvCategorySugg);
            tvCauseSugg = itemView.findViewById(R.id.tvCauseSugg);
            ivRatingSugg = itemView.findViewById(R.id.ivRatingSugg);

        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();

        }
    }
}
