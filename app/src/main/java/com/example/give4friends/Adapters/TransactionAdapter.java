package com.example.give4friends.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.give4friends.R;
import com.example.give4friends.models.Transaction;
import com.example.give4friends.models.User;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;



public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private String friendsName;

    private List<Transaction> transactions;
    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    Context context;

    // for each row inflate the layout and cache references into ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View CharityView = inflater.inflate(R.layout.item_transaction, parent, false);
        ViewHolder viewHolder = new ViewHolder(CharityView);
        return viewHolder;
    }

    // bind the values based on the position of the element
    @Override
    public void  onBindViewHolder(final ViewHolder holder, int position) {
        // get data according to position.
        final Transaction transaction = transactions.get(position);
        final boolean is_empty;

        //check if user is in likes list
        final List<String> array = transaction.getKeyLikesUsers();

        // if user is in likesUsers - start red
        if(array == null || !(array.contains(ParseUser.getCurrentUser().getObjectId()))) {
            is_empty = true;
            holder.ibEmptyHeart.setImageResource(R.drawable.ic_vector_heart_stroke);
            holder.ibEmptyHeart.setColorFilter(Color.BLACK);
            holder.ibEmptyHeart.setRotation(2);
        }
        else{
            is_empty = false;
            holder.ibEmptyHeart.setImageResource(R.drawable.ic_vector_heart);
            holder.ibEmptyHeart.setColorFilter(Color.RED);
            holder.ibEmptyHeart.setRotation(1);
        }

            holder.ibEmptyHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean is_empty = (holder.ibEmptyHeart.getRotation() == 2);

                    if(is_empty) {
                        holder.ibEmptyHeart.setImageResource(R.drawable.ic_vector_heart);
                        holder.ibEmptyHeart.setColorFilter(Color.RED);
                        holder.ibEmptyHeart.setRotation(1);
                        //update transaction
                        //increment likes for transaction
                        transaction.incrementLikes(1);
                        //add user to array
                        transaction.addLikesUser(ParseUser.getCurrentUser().getObjectId());
                        transaction.saveInBackground();

                    }else{
                        holder.ibEmptyHeart.setImageResource(R.drawable.ic_vector_heart_stroke);
                        holder.ibEmptyHeart.setColorFilter(Color.BLACK);
                        holder.ibEmptyHeart.setRotation(2);

                        //update parse

                        //update transaction
                        transaction.incrementLikes(-1);
                        //add user to array
                        array.remove(ParseUser.getCurrentUser().getObjectId());
                        transaction.setKeyLikesUsers(array);
                        transaction.saveInBackground();

                    }
                }
            });



        //populate the views according to this data

        holder.message.setText(transaction.getKeyMessage());

        if (transaction.getKeyCharityId() != null) {
            transaction.getKeyCharityId().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if(object != null){
                        holder.charity.setText("To: " + object.getString("name"));
                    }
                }
            });
        }

        transaction.getKeyDonorId().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                holder.donor.setText(Html.fromHtml("<font color=\"#434040\"><b>" + object.getString("firstName") + "</b></font>"));
                holder.donor.append(" donated on behalf of ");

                transaction.getKeyFriendId().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        holder.donor.append(Html.fromHtml("<font color=\"#434040\"><b>" + object.getString("firstName") + "</b></font>"));
                    }
                });
            }
        });

        transaction.getKeyFriendId().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {

                ParseFile image = object.getParseFile("profileImage");

                if(image != null){
                    Glide.with(context)
                            .load(image.getUrl())
                            .apply(new RequestOptions()
                                    .transforms(new CenterCrop(), new RoundedCorners(20))
                                    .circleCrop())

                            .into(holder.friendPhoto);
                }
            }
        });
        transaction.getKeyDonorId().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {

                ParseFile image = object.getParseFile("profileImage");

                if(image != null){
                    Glide.with(context)
                            .load(image.getUrl())
                            .apply(new RequestOptions()
                                    .transforms(new CenterCrop(), new RoundedCorners(20))
                                    .circleCrop())

                            .into(holder.donorPhoto);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    // create ViewHolder Class
    public class ViewHolder extends RecyclerView.ViewHolder {

        //public TextView charityName;
        public TextView donor;
        public TextView friend;
        public ImageView donorPhoto;
        public ImageView friendPhoto;
        public TextView charity;
        public TextView message;

        //like button
        public ImageButton ibEmptyHeart;

        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups
            donor = (TextView) itemView.findViewById(R.id.tvDonor);
            charity = (TextView) itemView.findViewById(R.id.tvCharity);
            donorPhoto= (ImageView) itemView.findViewById(R.id.ivDonor);
            friendPhoto = (ImageView) itemView.findViewById(R.id.ivFriend);
            message = (TextView) itemView.findViewById(R.id.tvMessage);

            // like button
            ibEmptyHeart = (ImageButton) itemView.findViewById(R.id.ib_empty_heart);
        }


        // Clean all elements of the recycler
        public void clear() {
            transactions.clear();
            notifyDataSetChanged();
        }

        // Add a list of items -- change to type used
        public void addAll(List<Transaction> list) {
            transactions.addAll(list);
            notifyDataSetChanged();
        }



    }


}