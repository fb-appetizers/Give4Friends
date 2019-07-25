package com.example.give4friends.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.method.CharacterPickerDialog;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;
import com.example.give4friends.models.User;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

public class CharityProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private Context context;
    private List<Object> items;
    private ParseUser myUser = ParseUser.getCurrentUser();
    Charity parseCharity;

    private final int CHARITY = 0, COMMENT = 1;

    public CharityProfileAdapter(List<Object> mObjects) {
        this.items = mObjects;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {


        context = viewGroup.getContext();
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        if (viewType == CHARITY) {

            View v1 = inflater.inflate(R.layout.item_main_profile_view, viewGroup, false);
            viewHolder = new CharityProfileAdapter.ViewHolderCharity(v1);
            return viewHolder;

        } else if (viewType == COMMENT) {
            View v2 = inflater.inflate(R.layout.activity_comment_profile, viewGroup, false);
            viewHolder = new CharityProfileAdapter.ViewHolderComment(v2);
            return viewHolder;
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {


        if (viewHolder.getItemViewType() == CHARITY) {
            CharityAPI charity = (CharityAPI) items.get(position);
            final ViewHolderCharity vh1 = (ViewHolderCharity) viewHolder;

            vh1.tvCPname.setMovementMethod(LinkMovementMethod.getInstance());
            vh1.tvCPname.setMovementMethod(LinkMovementMethod.getInstance());
            vh1.tvCPname.setText(Html.fromHtml("<a href=\'"+charity.getWebsiteUrl()+"\'>"
                    +charity.getName()+ "</a>"));

            vh1.tvCPMission.setText(Html.fromHtml(charity.getMission()));
            vh1.tvCPCategory.setText(Html.fromHtml("<font color=\"#434040\"><b>Category:</b></font> "+charity.getCategoryName()));
            vh1.tvCPCause.setText(Html.fromHtml("<font color=\"#434040\"><b>Cause:</b></font> "+charity.getCauseName()));

            final boolean is_empty;
            parseCharity = convertCharity(charity);

            vh1.tvCPLikedNum.setText("Liked by " + parseCharity.getKeyNumLikes() + " users");


//check if user is in likes list
            //final List<Charity> array = myUser.getList("favCharities" );
            final List<User> array = parseCharity.getList("likesUsers");

            vh1.tvCPname.setText(Html.fromHtml("<a href=\'" + charity.getWebsiteUrl() + "\'>"
                    + charity.getName() + "</a>"));
            // get all of the users favorite charities
            final ParseRelation<ParseObject> relation = myUser.getRelation("favCharities");


// if user is in likesUsers - start yellow
            if (array == null || !(array.contains(myUser.getObjectId()))) {
                is_empty = true;
                ((ViewHolderCharity) viewHolder).ibCPLike.setImageResource(R.drawable.ic_like_icon);
                ((ViewHolderCharity) viewHolder).ibCPLike.setColorFilter(Color.BLACK);
                ((ViewHolderCharity) viewHolder).ibCPLike.setRotation(2);
            } else {
                is_empty = false;
                ((ViewHolderCharity) viewHolder).ibCPLike.setImageResource(R.drawable.ic_like_filled_con);
                ((ViewHolderCharity) viewHolder).ibCPLike.setColorFilter(Color.YELLOW);
                ((ViewHolderCharity) viewHolder).ibCPLike.setRotation(1);
            }

            ((ViewHolderCharity) viewHolder).ibCPLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean is_empty = (((ViewHolderCharity) viewHolder).ibCPLike.getRotation() == 2);

                    if (is_empty) {
                        ((ViewHolderCharity) viewHolder).ibCPLike.setImageResource(R.drawable.ic_like_filled_con);
                        ((ViewHolderCharity) viewHolder).ibCPLike.setColorFilter(Color.YELLOW);
                        ((ViewHolderCharity) viewHolder).ibCPLike.setRotation(1);
                        //update parse
                        //updateUser
                        relation.add(parseCharity);
                        myUser.saveInBackground();

                        //increment likes for charity
                        parseCharity.incrementLikes(1);
                        //add user to array
                        parseCharity.addLikesUser(myUser.getObjectId());
                        try {
                            parseCharity.save();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        vh1.tvCPLikedNum.setText("Liked by " + parseCharity.getKeyNumLikes() + " users");



                    } else {
                        ((ViewHolderCharity) viewHolder).ibCPLike.setImageResource(R.drawable.ic_like_icon);
                        ((ViewHolderCharity) viewHolder).ibCPLike.setColorFilter(Color.BLACK);
                        ((ViewHolderCharity) viewHolder).ibCPLike.setRotation(2);

                        //update parse

                        //update user
                        relation.remove(parseCharity);
                        myUser.saveInBackground();

                        //update charity
                        parseCharity.incrementLikes(-1);
                        //add user to array
                        array.remove(myUser.getObjectId());
                        parseCharity.setKeyLikesUsers(array);
                        try {
                            parseCharity.save();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        vh1.tvCPLikedNum.setText("Liked by " + parseCharity.getKeyNumLikes() + " users");

                    }


                }
            });

        } else if (viewHolder.getItemViewType() == COMMENT) {


            ViewHolderComment vh2 = (ViewHolderComment) viewHolder;


        }
    }


    @Override
    public int getItemCount() {

        return this.items.size();
    }

    @Override
    public int getItemViewType(int position) {
        //More to come

        if (position == 0) {
            return CHARITY;
        } else {
            return COMMENT;
        }

    }


    public class ViewHolderCharity extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView tvCPname;
        TextView tvCPCategory;
        TextView tvCPCause;
        TextView tvCPMission;

        TextView tvCPLikedNum;
        ImageButton ibCPLike;

        public ViewHolderCharity(@NonNull View itemView) {
            super(itemView);

            tvCPname = itemView.findViewById(R.id.tvCPname);
            tvCPCategory = itemView.findViewById(R.id.tvCPCategory);
            tvCPCause = itemView.findViewById(R.id.tvCPCause);
            tvCPMission = itemView.findViewById(R.id.tvCPMission);
            tvCPLikedNum = itemView.findViewById(R.id.tvCPLikedNum);
            ibCPLike = itemView.findViewById(R.id.ibCPLike);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public class ViewHolderComment extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ViewHolderComment(@NonNull View itemView) {
            super(itemView);


        }

        @Override
        public void onClick(View view) {

        }
    }

    public Charity convertCharity(final CharityAPI selectedCharity) {
        ParseQuery<Charity> charityParseQuery = new ParseQuery<Charity>(Charity.class);
        charityParseQuery.include(Charity.KEY_CHARITY_ID);

        charityParseQuery.whereEqualTo("charityName", selectedCharity.getEin());

        try {
            parseCharity = charityParseQuery.getFirst();
            ;
        } catch (ParseException e) {
            e.printStackTrace();
            if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                final Charity newCharity = new Charity();
                newCharity.setKeyCategoryName(selectedCharity.getCategoryName());
                newCharity.setKeyCauseName(selectedCharity.getCauseName());
                newCharity.setKeyCharityID(selectedCharity.getEin());
                newCharity.setKeyMission(selectedCharity.getMission());
                newCharity.setKeyName(selectedCharity.getName());
                newCharity.setKeyRatingURL(selectedCharity.getRatingsUrl());
                newCharity.setKeyUrl(selectedCharity.getWebsiteUrl());

                try {
                    newCharity.save();
                    parseCharity = newCharity;
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

            } else {
                Log.e("CharitySearchAdapter", "Error with query of charity");
            }
        }

        return parseCharity;
    }

    //testing push
}
