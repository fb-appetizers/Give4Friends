package com.example.give4friends.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;



public class FavCharitiesAdapter extends RecyclerView.Adapter<FavCharitiesAdapter.ViewHolder> {



    private List<Charity> charities;
    public FavCharitiesAdapter(ArrayList<Charity> charities) {
        this.charities = charities;
    }

    Context context;

    // for each row inflate the layout and cache references into ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View CharityView = inflater.inflate(R.layout.item_charity, parent, false);
        ViewHolder viewHolder = new ViewHolder(CharityView);
        return viewHolder;
    }

    // bind the values based on the position of the element
    @Override
    public void  onBindViewHolder(ViewHolder holder, int position) {

        // get data according to position.
        Charity charity = (Charity) charities.get(position);



        //populate the views according to this data
        holder.name.setText(charity.getKeyName());
        holder.causeName.setText(charity.getKeyCauseName());
        holder.categoryName.setText(charity.getKeyCategoryName());
        holder.mission.setText(charity.getKeyMission());



        if(charity.getKeyRatingURL() != null){
            // Handles images
            Glide.with(context)
                    .load(charity.getKeyRatingURL())
                    .apply(new RequestOptions()
                            .transforms(new CenterCrop(), new RoundedCorners(20)))
                    .into(holder.rating);

        }



        /*



        // Handles images
        Glide.with(context)
                .load(Charity.getImage()
                        .getUrl())
                .apply(new RequestOptions()
                        .transforms(new CenterCrop(), new RoundedCorners(20))
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background))
                .into(holder.ivCharityImage);



*/
    }


    @Override
    public int getItemCount() {
        return charities.size();
    }



    // create ViewHolder Class
    public class ViewHolder extends RecyclerView.ViewHolder {

        //public TextView charityName;

        public TextView name;
        public TextView mission;
        public ImageView rating;
        public TextView ein; // Organization ID for the charity
        public TextView categoryName;
        public TextView causeName;
        public TextView websiteUrl;
        public TextView ratingsUrl;


        public ViewHolder(View itemView) {
            super(itemView);



            // perform findViewById lookups
            name = (TextView) itemView.findViewById(R.id.tvCharityName);
            mission = (TextView) itemView.findViewById(R.id.tvMission);
            categoryName = (TextView) itemView.findViewById(R.id.tvCategory);
            causeName = (TextView) itemView.findViewById(R.id.tvCause);
            rating = (ImageView) itemView.findViewById(R.id.ivRating);

          /*


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // gets item position
                    int position = getAdapterPosition();
                    // make sure the position is valid, i.e. actually exists in the view
                    if (position != RecyclerView.NO_POSITION) {
                        // get the movie at the position, this won't work if the class is static
                        Charity Charity = Charitys.get(position);
                        // create intent for the new activity
                        Intent intent = new Intent(context, DetailsActivity.class);
                        // serialize the tweet using parceler, use its short name as a key
                        intent.putExtra(Charity.class.getSimpleName(), Parcels.wrap(Charity));
                        //intent.putExtra( "id" , Charity.getObjectId());
                        // show the activity
                        context.startActivity(intent);
                    }
                }
            });
        }
        */

    }


    // Clean all elements of the recycler
    public void clear() {
        charities.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Charity> list) {
        charities.addAll(list);
        notifyDataSetChanged();
    }

}}
