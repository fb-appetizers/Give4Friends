package com.example.give4friends.Adapters;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

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
        View CharityView = inflater.inflate(R.layout.item_charity_favorites, parent, false);
        ViewHolder viewHolder = new ViewHolder(CharityView);
        return viewHolder;
    }

    // bind the values based on the position of the element
    @Override
    public void  onBindViewHolder(ViewHolder holder, int position) {

        // get data according to position.
       // Charity charity = (Charity) charities.get(position);
        Charity charity = charities.get(position);

        holder.name.setMovementMethod(LinkMovementMethod.getInstance());

        holder.name.setText(Html.fromHtml("<a href=\'"+charity.getKeyWebsiteURL()+"\'>"
                +charity.getKeyName() + " ("
                + charity.getKeyCategoryName() + ")"+ "</a>"));


        holder.causeName.setText(Html.fromHtml("<font color=\"#434040\"><b>Cause:</b></font> "+charity.getKeyCauseName()));



        }




    @Override
    public int getItemCount() {
        return charities.size();
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



    // create ViewHolder Class
    public class ViewHolder extends RecyclerView.ViewHolder {

        //public TextView charityName;

        public TextView name;
        public TextView causeName;


        public ViewHolder(View itemView) {
            super(itemView);



            // perform findViewById lookups
            name = (TextView) itemView.findViewById(R.id.tvCharityName);
            causeName = (TextView) itemView.findViewById(R.id.tvCause);

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



}}


