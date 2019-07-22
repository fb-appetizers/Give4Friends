package com.example.give4friends.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.give4friends.CharitySearch;
import com.example.give4friends.DonateFinalActivity;
import com.example.give4friends.DonateSearchCharity;
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;
import java.util.regex.Pattern;

import static com.example.give4friends.DonateActivity.friend;

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

        if(this.remove_links) {
            holder.tvMoreInfo.setVisibility(View.GONE);
            holder.tvDonateNow.setVisibility(View.GONE);

        }
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

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                CharityAPI selectedCharity = mCharity.get(position);
                Charity charityy;

                ParseQuery<Charity> charityParseQuery = new ParseQuery<Charity>(Charity.class);
                charityParseQuery.include(Charity.KEY_CHARITY_ID);

                charityParseQuery.whereEqualTo("charityName", selectedCharity.getEin());

//                charityParseQuery.getFirstInBackground(new FindCallback<Charity>() {
//                    @Override
//                    public void done(List<Charity> objects, ParseException e) {
//                        if(e != null){
//                            Log.e("CharitySearchAdapter", "Error with query of charity");
//                            e.printStackTrace();
//                            return;
//                        }
//                        else{
////                            charityy = objects;
//                        }
//                    }
//                });
////                Charity charity = selectedCharity.getCharity();


                Intent intent = new Intent(view.getContext(), DonateFinalActivity.class);
                intent.putExtra("friend", (Parcelable) DonateSearchCharity.friendInfo);
                intent.putExtra("charity", selectedCharity.getName());
                view.getContext().startActivity(intent);
            }
        }
    }
}
