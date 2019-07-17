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
        View postView = inflater.inflate(R.layout.item_charity, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    // bind the values based on the position of the element
    @Override
    public void  onBindViewHolder(ViewHolder holder, int position) {

        //Temporarily only filling with string of charity name
        // get data according to position.
        Charity charity = charities.get(position);

        //populate the views according to this data
       // holder.charityName.setText(charity);



        /*
        //populate the views according to this data
        holder.tvUserName.setText(charity.getUser().getUsername());
        holder.tvUserName2.setText(post.getUser().getUsername());
        holder.tvDescription.setText(post.getDescription());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        holder.tvCreatedAt.setText(sdf.format( post.getCreatedAt()));




        // we don't have profile pic yet

        // Handles images
        Glide.with(context)
                .load(post.getUser().getParseFile("profileImage").getUrl())
                .apply(new RequestOptions()
                        .transforms(new CenterCrop(), new RoundedCorners(20)))
                .into(holder.ivProfileImage);




        // Handles images
        Glide.with(context)
                .load(post.getImage()
                        .getUrl())
                .apply(new RequestOptions()
                        .transforms(new CenterCrop(), new RoundedCorners(20))
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background))
                .into(holder.ivPostImage);



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
        public Integer rating;
        public TextView ein; // Organization ID for the charity
        public TextView categoryName;
        public TextView causeName;
        public TextView websiteUrl;
        public TextView ratingsUrl;


        public ViewHolder(View itemView) {
            super(itemView);



            // perform findViewById lookups
            name = (TextView) itemView.findViewById(R.id.charityName);

          /*
            ivProfileImage = (ImageView) itemView.findViewById((R.id.ivProfileImage));
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvUserName2 = (TextView) itemView.findViewById(R.id.tvUserName2);
            tvDescription = (TextView) itemView.findViewById((R.id.tvDescription));
            ivPostImage = (ImageView) itemView.findViewById((R.id.iv_PostImage));
            tvCreatedAt = (TextView) itemView.findViewById((R.id.tvCreatedAt));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // gets item position
                    int position = getAdapterPosition();
                    // make sure the position is valid, i.e. actually exists in the view
                    if (position != RecyclerView.NO_POSITION) {
                        // get the movie at the position, this won't work if the class is static
                        Post post = posts.get(position);
                        // create intent for the new activity
                        Intent intent = new Intent(context, DetailsActivity.class);
                        // serialize the tweet using parceler, use its short name as a key
                        intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                        //intent.putExtra( "id" , post.getObjectId());
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
