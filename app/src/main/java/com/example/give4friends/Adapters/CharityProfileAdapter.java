package com.example.give4friends.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.give4friends.Cutom_Classes.ExpandableTextView;
import com.example.give4friends.DonateActivity;
import com.example.give4friends.Fragments.Create_Comments_Fragment;
import com.example.give4friends.Fragments.Friend_Profile_Fragment;
import com.example.give4friends.Fragments.User_Profile_Fragment;
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;
import com.example.give4friends.models.FavoriteCharities;
import com.example.give4friends.models.Comments;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

import static com.example.give4friends.DonateActivity.charityName2;
import static com.example.give4friends.DonateActivity.currentCharity;

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

            View v1 = inflater.inflate(R.layout.item_charity_profile_page, viewGroup, false);
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
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position){

        if (viewHolder.getItemViewType() == CHARITY) {
            CharityAPI charity = (CharityAPI) items.get(position);
            final ViewHolderCharity vh1 = (ViewHolderCharity) viewHolder;

            vh1.tvCPname.setMovementMethod(LinkMovementMethod.getInstance());
            vh1.tvCPname.setMovementMethod(LinkMovementMethod.getInstance());
            vh1.tvCPname.setText(Html.fromHtml("<a href=\'" + charity.getWebsiteUrl() + "\'>"
                    + charity.getName() + "</a>"));

            vh1.tvCPMission.setText(Html.fromHtml(charity.getMission()));
            vh1.tvCPCategory.setText(Html.fromHtml("<font color=\"#434040\"><b>Category:</b></font> " + charity.getCategoryName()));
            vh1.tvCPCause.setText(Html.fromHtml("<font color=\"#434040\"><b>Cause:</b></font> " + charity.getCauseName()));

            final boolean is_empty;
            parseCharity = convertCharity(charity);

            vh1.tvCPLikedNum.setText(((Integer) parseCharity.getKeyNumLikes()).toString());
            vh1.tvCommentsNum.setText(((Integer) parseCharity.getInt("CommentsNum")).toString());


            List<ParseUser> array = parseCharity.getList("likesUsers");

            vh1.tvCPname.setText(Html.fromHtml("<a href=\'" + charity.getWebsiteUrl() + "\'>"
                    + charity.getName() + "</a>"));


            ParseQuery<Charity> charityParseQuery = new ParseQuery<Charity>(Charity.class);
            charityParseQuery.include(Charity.KEY_CHARITY_ID);
            charityParseQuery.whereEqualTo("charityName", charity.getEin());

            charityParseQuery.getFirstInBackground(new GetCallback<Charity>() {
                @Override
                public void done(Charity object, ParseException e) {
                    if (e != null) {
                        if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                            Glide.with(context)
                                    .load("https://png.pngtree.com/svg/20170801/c502e4e69e.png")
                                    .apply(new RequestOptions()
                                            .transforms(new CenterCrop(), new RoundedCorners(20))
                                            .circleCropTransform())
                                    .into(vh1.ivLogo);

                        } else {
                            Log.e("CharityProfileAdapter", "Error with query of charity");
                        }
                    } else {
                        String logo = object.getKeyLogo();
                        if (logo != null) {
                            Glide.with(context)
                                    .load(logo)
                                    .apply(new RequestOptions()
                                            .transforms(new CenterCrop(), new RoundedCorners(20))
                                            .circleCropTransform()
                                            .fitCenter()
                                    )
                                    .into(vh1.ivLogo);
                        } else {
                            Glide.with(context)
                                    .load("https://png.pngtree.com/svg/20170801/c502e4e69e.png")
                                    .apply(new RequestOptions()
                                            .transforms(new CenterCrop(), new RoundedCorners(20))
                                            .circleCrop()
                                            .circleCropTransform()
                                            .fitCenter())
                                    .into(vh1.ivLogo);
                        }

                    }
                }
            });

            FavoriteCharities.setUpFavorites(parseCharity, myUser, vh1.ibCPLike, vh1.tvCPLikedNum);

        } else if (viewHolder.getItemViewType() == COMMENT) {
            ViewHolderComment vh2 = (ViewHolderComment) viewHolder;
            Comments comments = (Comments) items.get(position);
            vh2.tvComment.setText(comments.getMessage());
            vh2.tvCommentUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){


                    if(comments.getUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId()) ){
                        // Create a new fragment instead of an activity
                        Fragment fragment = new User_Profile_Fragment(ParseUser.getCurrentUser(), true);
                        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                        fragmentManager.beginTransaction().
                                replace(R.id.flContainer, fragment)
                                .addToBackStack(null).commit();
                    }
                    else{

//                    // Create a new fragment instead of an activity
                        Fragment fragment = new Friend_Profile_Fragment(comments.getUser());
                        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                        fragmentManager.beginTransaction().
                                replace(R.id.flContainer, fragment)
                                .addToBackStack(null).commit();
                    }
                }
            });
            comments.getUser().fetchIfNeededInBackground(new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser myUser, ParseException e) {
                    if (myUser != null) {
                        vh2.tvCommentUsername.setText(myUser.getUsername());

                        String imageURL = myUser.getString("profileImageURL");

                        if (imageURL != null) {
                            Date imageDate = myUser.getDate("profileImageCreatedAt");
                            Glide.with(context)
                                    .load(imageURL)
                                    .apply(new RequestOptions()
                                            .transforms(new CenterCrop(), new RoundedCorners(20))
                                            .circleCropTransform()
                                            .placeholder(R.drawable.user_outline_24)
                                            .error(R.drawable.user_outline_24)
                                            .signature(new ObjectKey(imageDate))
                                    )
                                    .into(vh2.ivCommentProfile);
                        } else {
                            Glide.with(context)
                                    .load(R.drawable.user_outline_24)
                                    .apply(new RequestOptions()
                                            .transforms(new CenterCrop(), new RoundedCorners(20))
                                            .circleCropTransform()
                                            .placeholder(R.drawable.user_outline_24)
                                            .error(R.drawable.user_outline_24))
                                    .into(vh2.ivCommentProfile);
                        }

                        // Makes the comment clickable
                        vh2.ivCommentProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view){

                                if(myUser.getObjectId().equals(ParseUser.getCurrentUser().getObjectId()) ){
                                    // Create a new fragment instead of an activity
                                    Fragment fragment = new User_Profile_Fragment(ParseUser.getCurrentUser(), true);
                                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                                    fragmentManager.beginTransaction().
                                            replace(R.id.flContainer, fragment)
                                            .addToBackStack(null).commit();
                                }
                                else{

                                    Fragment fragment = new Friend_Profile_Fragment(myUser);
                                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                                    fragmentManager.beginTransaction().
                                            replace(R.id.flContainer, fragment)
                                            .addToBackStack(null).commit();
                                }
                            }
                        });



                    }
                }
            });
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

    public class ViewHolderCharity extends RecyclerView.ViewHolder{
        TextView tvCPname;
        TextView tvCPCategory;
        TextView tvCPCause;
        TextView tvCPMission;
        TextView tvCPLikedNum;
        ImageButton ibCPLike;
        ImageButton tvDonateNow;
        ImageButton ibComments;
        TextView tvCommentsNum;
        ImageView ivcheckmarkprofile;
        ImageView ivLogo;


        public ViewHolderCharity(@NonNull View itemView) {
            super(itemView);
            tvCPname = itemView.findViewById(R.id.tvCPname);
            tvCPCategory = itemView.findViewById(R.id.tvCPCategory);
            tvCPCause = itemView.findViewById(R.id.tvCPCause);
            tvCPMission = itemView.findViewById(R.id.tvCPMission);
            tvCPLikedNum = itemView.findViewById(R.id.tvCPLikedNum);
            ibCPLike = itemView.findViewById(R.id.ibCPLike);
            tvDonateNow = itemView.findViewById(R.id.tvDonateNowProfile);
            ibComments = itemView.findViewById(R.id.ibComments);
            tvCommentsNum = itemView.findViewById(R.id.tvCommentsNum);
            //ivcheckmarkprofile = itemView.findViewById(R.id.ivcheckmarkprofile);
            ivLogo = itemView.findViewById(R.id.ivLogo);




            ibComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition(); // gets item position
                    if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                        currentCharity = parseCharity;
                        charityName2 = currentCharity.getKeyName();
                    }
                    // Create a new fragment instead of an activity
                    Fragment fragment = new Create_Comments_Fragment(charityName2,myUser,currentCharity);
                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                    fragmentManager.beginTransaction().
                            replace(R.id.flContainer, fragment)
                            .addToBackStack(null).commit();
                }
            });

            tvDonateNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition(); // gets item position
                    if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                        currentCharity = parseCharity;
                        charityName2 = currentCharity.getKeyName();
                    }

                    Intent intent = new Intent(view.getContext(), DonateActivity.class);
                    intent.putExtra("donateNow", true);
                    view.getContext().startActivity(intent);
                }
            });
        }


    }

    public class ViewHolderComment extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivCommentProfile;
        TextView tvCommentUsername;
        ExpandableTextView tvComment;
        public ViewHolderComment(@NonNull View itemView) {

            super(itemView);
            ivCommentProfile = itemView.findViewById(R.id.ivLogo);
            tvCommentUsername = itemView.findViewById(R.id.tvCommentUsername);
            tvComment = itemView.findViewById(R.id.tvComment);
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
}
