package com.example.give4friends.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.give4friends.Cutom_Classes.ExpandableTextView;
import com.example.give4friends.Fragments.Charity_Profile_Fragment;
import com.example.give4friends.Fragments.Friend_Profile_Fragment;
import com.example.give4friends.Fragments.User_Profile_Fragment;
import com.example.give4friends.R;
import com.example.give4friends.models.CharityAPI;
import com.example.give4friends.models.Transaction;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transaction> transactions;
    private boolean friend;
    public TransactionAdapter(List<Transaction> transactions, boolean friend) {
        this.transactions = transactions;
        this.friend = friend;
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

//        .addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // Sets the like counter
        if(transaction.getKeyLikesCount()!=null){

            Integer likesCount = (Integer) transaction.getKeyLikesCount();
            if(likesCount !=0) {
                holder.tvLikesCount.setVisibility(View.VISIBLE);
                holder.tvLikesCount.setText(likesCount.toString());
            }else{
                holder.tvLikesCount.setVisibility(View.INVISIBLE);
            }

        }else{
            holder.tvLikesCount.setVisibility(View.INVISIBLE);
        }

        //check if user is in likes list
        List<String> array = transaction.getKeyLikesUsers();

        // if user is in likesUsers - start red
        if(array == null || !(array.contains(ParseUser.getCurrentUser().getObjectId()))) {
            holder.ibEmptyHeart.setImageResource(R.drawable.ic_vector_heart_stroke);
            holder.ibEmptyHeart.setColorFilter(Color.BLACK);
        }else{
            holder.ibEmptyHeart.setImageResource(R.drawable.ic_vector_heart);
            holder.ibEmptyHeart.setColorFilter(context.getResources().getColor(R.color.colorPrimary));
        }
            holder.ibEmptyHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<String> array = transaction.getKeyLikesUsers();
                    if(array == null || !(array.contains(ParseUser.getCurrentUser().getObjectId())) || array.size() ==0) {
                        holder.ibEmptyHeart.setImageResource(R.drawable.ic_vector_heart);
                        holder.ibEmptyHeart.setColorFilter(context.getResources().getColor(R.color.colorPrimary));
                        //update transaction
                        //increment likes for transaction

                        Number likesCountNum = transaction.getKeyLikesCount();
                        Integer likesCountInt;
                        if( likesCountNum != null){
                            likesCountInt = ((Integer) likesCountNum)+1;
                        }else{
                            likesCountInt = 1;
                        }


                        if(likesCountInt !=0) {
                            holder.tvLikesCount.setVisibility(View.VISIBLE);
                            holder.tvLikesCount.setText((likesCountInt).toString());
                        }else{
                            holder.tvLikesCount.setVisibility(View.INVISIBLE);
                        }

                        transaction.incrementLikes(1);
                        //add user to array
                        transaction.addLikesUser(ParseUser.getCurrentUser().getObjectId());

                        transaction.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e!=null){
                                    e.printStackTrace();
                                }
                            }
                        });

                    }else{
                        holder.ibEmptyHeart.setImageResource(R.drawable.ic_vector_heart_stroke);
                        holder.ibEmptyHeart.setColorFilter(Color.BLACK);

                        Integer likesCount = ((Integer) transaction.getKeyLikesCount())-1;

                        if(likesCount !=0) {
                            holder.tvLikesCount.setVisibility(View.VISIBLE);
                            holder.tvLikesCount.setText((likesCount).toString());
                        }else{
                            holder.tvLikesCount.setVisibility(View.INVISIBLE);
                        }

                        //update transaction
                        transaction.incrementLikes(-1);
                        //add user to array
                        array.remove(ParseUser.getCurrentUser().getObjectId());
                        transaction.setKeyLikesUsers(array);
                        transaction.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e!=null){
                                    e.printStackTrace();
                                }else{

                                }
                            }
                        });
                    }
                }
            });


        holder.ivarrow.setVisibility(View.GONE);
        holder.amount.setVisibility(View.GONE);

        //populate the views according to this data
        // if user is current user

        if(!friend ){
            holder.ivarrow.setVisibility(View.VISIBLE);
            holder.amount.setVisibility(View.VISIBLE);


            if (transaction.getKeyAmountDonated() != null) {
                holder.amount.setText("$" + transaction.getKeyAmountDonated().toString());
            } else {
                holder.amount.setText(((Integer) 0).toString());
            }

            if(transaction.getKeyDonorId().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
                holder.amount.setTextColor( context.getResources().getColor(R.color.colorPrimaryDark));

                holder.ivarrow.setImageResource(R.drawable.right_arrow);
                holder.ivarrow.setRotation(180);
                holder.ivarrow.setColorFilter(context.getResources().getColor((R.color.colorPrimaryDark)));

            }
            else{
                holder.amount.setTextColor(context.getResources().getColor((R.color.mainBlue)));
                holder.ivarrow.setImageResource(R.drawable.right_arrow);
                holder.ivarrow.setRotation(0);
                holder.ivarrow.setColorFilter(context.getResources().getColor((R.color.mainBlue)));
            }
        }




        holder.message.setText(transaction.getKeyMessage());

        transaction.getKeyDonorId().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                holder.donor.setText(Html.fromHtml("<font color=\"#434040\"><b>" + object.getString("firstName") + "</b></font>"));
                holder.donor.append(" donated on behalf of ");

                transaction.getKeyFriendId().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {

                        holder.friend.setText(Html.fromHtml("<font color=\"#434040\"><b>" + object.getString("firstName") + "</b></font>"));
                    }
                });
            }
        });

        if (transaction.getKeyCharityId() != null) {
            transaction.getKeyCharityId().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(final ParseObject object, ParseException e) {
                    if(object != null){
                        holder.charity.setText(Html.fromHtml("<font color=\"#644BDF\"><b>" + object.getString("name") + "</b></font>"));
                        holder.charity.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharityAPI charity = CharityAPI.fromParse(transaction.getKeyCharityId());
                                Fragment fragment = new Charity_Profile_Fragment(charity);
                                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                                fragmentManager.beginTransaction().
                                        replace(R.id.flContainer, fragment)
                                        .addToBackStack(null).commit();
                            }
                        });
                        holder.pin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharityAPI charity = CharityAPI.fromParse(transaction.getKeyCharityId());

                                Fragment fragment = new Charity_Profile_Fragment(charity);
                                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                                fragmentManager.beginTransaction().
                                        replace(R.id.flContainer, fragment)
                                        .addToBackStack(null).commit();
                            }
                        });
                    }
                }
            });
        }


        transaction.getKeyFriendId().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {

                String imageURL = object.getString("profileImageURL");

                if(imageURL != null){
                    Date imageDate = object.getDate("profileImageCreatedAt");
                    Glide.with(context)
                            .load(imageURL)
                            .apply(new RequestOptions()
                                    .transforms(new CircleCrop(), new RoundedCorners(20))
                                    .circleCrop()
                                            .signature(new ObjectKey(imageDate))
//                                    .placeholder(R.drawable.instagram_user_outline_24)

                                    )

                            .into(holder.friendPhoto);
                }
                else{
                    Glide.with(context)
                            .load(R.drawable.instagram_user_outline_24)

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


                String imageURL = object.getString("profileImageURL");
                if(imageURL != null){
                    Date imageDate = object.getDate("profileImageCreatedAt");

                    Glide.with(context)
                            .load(imageURL)

                            .apply(new RequestOptions()
                                    .transforms(new CenterCrop(), new RoundedCorners(20))
                                    .circleCrop()
                                    .signature(new ObjectKey(imageDate))
//                                            .placeholder(R.drawable.instagram_user_outline_24)

                                    )
                            .into(holder.donorPhoto);
                }else{
                    Glide.with(context)
                            .load(R.drawable.instagram_user_outline_24)
                            .apply(new RequestOptions()
                                    .transforms(new CenterCrop(), new RoundedCorners(20))
                                    .circleCrop())
                            .into(holder.donorPhoto);
                }
            }
        });
        transaction.getKeyFriendId().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
//                holder.friend.setText(object.getString("firstName"));
            }
        });


        // if yourself send to your profile
        holder.donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(transaction.getKeyDonorId().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())  ){
                    // Create a new fragment instead of an activity

                    Fragment fragment = new User_Profile_Fragment(ParseUser.getCurrentUser(), true);
                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                    fragmentManager.beginTransaction().
                            replace(R.id.flContainer, fragment)
                            .addToBackStack(null).commit();
                }
                else{

                    // Create a new fragment instead of an activity
                    Fragment fragment = new Friend_Profile_Fragment(transaction.getKeyDonorId());
                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                    fragmentManager.beginTransaction().
                            replace(R.id.flContainer, fragment)
                            .addToBackStack(null).commit();
                }
            }
        });

        holder.friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(transaction.getKeyFriendId().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())  ){
                    // Create a new fragment instead of an activity
                    Fragment fragment = new User_Profile_Fragment(ParseUser.getCurrentUser(), true);
                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                    fragmentManager.beginTransaction().
                            replace(R.id.flContainer, fragment)
                            .addToBackStack(null).commit();
                }
                else{

                    // Create a new fragment instead of an activity
                    Fragment fragment = new Friend_Profile_Fragment(transaction.getKeyFriendId());
                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                    fragmentManager.beginTransaction().
                            replace(R.id.flContainer, fragment)
                            .addToBackStack(null).commit();
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    // create ViewHolder Class
    public class ViewHolder extends RecyclerView.ViewHolder{

        //public TextView charityName;
        public TextView donor;
        public TextView friend;
        public ImageView donorPhoto;
        public ImageView friendPhoto;
        public TextView charity;
        public ExpandableTextView message;
        public ImageView pin;
        public TextView amount;
        public TextView tvLikesCount;
        public ImageView ivarrow;


        //like button
        public ImageButton ibEmptyHeart;

        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups
            donor = itemView.findViewById(R.id.tvDonor);
            charity = itemView.findViewById(R.id.tvCharity);
            donorPhoto= itemView.findViewById(R.id.ivDonor);
            donorPhoto.setClickable(true);
            friendPhoto = itemView.findViewById(R.id.ivFriend);
            friendPhoto.setClickable(true);
            message = itemView.findViewById(R.id.tvMessage);
            // like button
            ibEmptyHeart = itemView.findViewById(R.id.ib_empty_heart);
            pin = itemView.findViewById(R.id.ivPin);
            amount = itemView.findViewById(R.id.tvAmount);
            tvLikesCount = itemView.findViewById(R.id.tvLikesCount);
            ivarrow = itemView.findViewById(R.id.ivarrow);
            friend = itemView.findViewById(R.id.tvFriendTransaction);

        }
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