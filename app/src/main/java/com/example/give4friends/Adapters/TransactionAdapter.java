package com.example.give4friends.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.give4friends.R;
import com.example.give4friends.models.TransactionHome;
import com.example.give4friends.models.User;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.Transaction;

import java.util.ArrayList;
import java.util.List;



public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {


    private List<TransactionHome> transactions;
    public TransactionAdapter(List<TransactionHome> transactions) {
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
        TransactionHome transaction = transactions.get(position);


        // like button
        holder.ibFullHeart.setVisibility(View.INVISIBLE);
        holder.ibFullHeart.setClickable(false);


        holder.ibEmptyHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ibFullHeart.setVisibility(View.VISIBLE);
                holder.ibEmptyHeart.setVisibility(View.INVISIBLE);
                holder.ibFullHeart.setClickable(true);
                holder.ibEmptyHeart.setClickable(false);
            }
        });

        holder.ibFullHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ibFullHeart.setVisibility(View.INVISIBLE);
                holder.ibEmptyHeart.setVisibility(View.VISIBLE);
                holder.ibFullHeart.setClickable(false);
                holder.ibEmptyHeart.setClickable(true);
            }
        });




        //populate the views according to this data

//        if((transaction.getKeyDonorId() != null) && ((transaction.getKeyDonorId()).getKeyFirstName()) != null){
            holder.donor.setText(transaction.getDonorName());
//        }
//        if( (transaction.getKeyFriendId()) != null  && ((transaction.getKeyFriendId()).getKeyFirstName()) != null){
            holder.friend.setText(transaction.getFriendName());
//        }
//        if( ((transaction.getKeyCharityId()) != null)  && ((transaction.getKeyCharityId()).getKeyName()) != null){
            holder.charity.setText(transaction.getCharityName());
//        }
//        if((transaction.getKeyMessage()) != null){
            holder.message.setText("Message: " + transaction.getMessage());
//        }
//
//
//
//
////        if(transaction.getKeyDonorId().getKeyProfileImage() != null){
////            // Handles images
            Glide.with(context)
                    .load(transaction.getDonorProfile().getUrl())
                    .apply(new RequestOptions()
                            .transforms(new CenterCrop(), new RoundedCorners(20)))
//                            .circleCropTransform()
                    .into(holder.donorPhoto);

        }

//        if(transaction.getKeyFriendId().getKeyProfileImage() != null){
//            // Handles images
//            Glide.with(context)
//                    .load(transaction.getFriendProfile().getUrl())
//                    .apply(new RequestOptions()
//                            .transforms(new CenterCrop(), new RoundedCorners(20)))
//                            .circleCropTranform())
//                    .into(holder.friendPhoto);
//////
////        }
//
//
//
//    }


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
        public ImageButton ibFullHeart;



        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups
            donor = (TextView) itemView.findViewById(R.id.tvDonor);
            friend= (TextView) itemView.findViewById(R.id.tvFriend);
            charity = (TextView) itemView.findViewById(R.id.tvCharity);
            donorPhoto= (ImageView) itemView.findViewById(R.id.ivDonor);
            friendPhoto = (ImageView) itemView.findViewById(R.id.ivFriend);
            message = (TextView) itemView.findViewById(R.id.tvMessage);

            // like button
            ibEmptyHeart = (ImageButton) itemView.findViewById(R.id.ib_empty_heart);
            ibFullHeart = (ImageButton) itemView.findViewById(R.id.ib_full_heart);


        }


        // Clean all elements of the recycler
        public void clear() {
            transactions.clear();
            notifyDataSetChanged();
        }

        // Add a list of items -- change to type used
        public void addAll(List<TransactionHome> list) {
            transactions.addAll(list);
            notifyDataSetChanged();
        }



    }


}