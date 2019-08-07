package com.example.give4friends.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
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

import com.example.give4friends.Cutom_Classes.CustomDialogCharity;
import com.example.give4friends.Cutom_Classes.InfoDialog;
import com.example.give4friends.DonateActivity;
import com.example.give4friends.DonateFinalActivity;
import com.example.give4friends.Fragments.Charity_Profile_Fragment;
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;
import com.example.give4friends.models.FavoriteCharities;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import static com.example.give4friends.DonateActivity.charityName2;
import static com.example.give4friends.DonateActivity.charityLogo2;
import static com.example.give4friends.DonateActivity.currentCharity;

public class CharitySearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> items;
    private Context context;
    private boolean skip;
    private boolean from_charity_search;
    private boolean favorites;
    private Charity newCharity;


    public CharitySearchAdapter(ArrayList<Object> items, boolean skip, boolean from_charity_search, boolean favorites) {
        this.items = items;
        this.skip = skip;
        // If the class is called from charity search then disable the buttons on the dialog fragment that pops up
        this.from_charity_search = from_charity_search;
        this.favorites = favorites;
    }

    private final int TEXT = 0, CHARITY = 1;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View v1;
        if (viewType == CHARITY) {
            if(favorites){
                 v1 = inflater.inflate(R.layout.item_charity_favorites, parent, false);
                viewHolder = new CharitySearchAdapter.ViewHolderFavorites(v1);
            }
            else {
                 v1 = inflater.inflate(R.layout.item_charity_sugg, parent, false);
                viewHolder = new CharitySearchAdapter.ViewHolderSuggested(v1);
            }
            return viewHolder;

        } else if (viewType == TEXT) {
            //much change
            View v2 = inflater.inflate(R.layout.item_header, parent, false);
            viewHolder = new CharitySearchAdapter.ViewHolderText(v2);
            return viewHolder;
    }

        return viewHolder;
    }

    public class ViewHolderText extends RecyclerView.ViewHolder{
        public TextView tvHeader;
        public ImageButton ibQuestion;

        public ViewHolderText(@NonNull View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tvHeader);
            ibQuestion = itemView.findViewById(R.id.ibQuestion);

        }
    }

    public class ViewHolderFavorites extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView causeName;
        public ImageButton tvDonateNow;
        public ImageButton tvMoreInfo;
        public ImageButton ibCPLike;
        public TextView tvCPLikedNum;
        public ImageView ivcheckmarkfav;

        public ViewHolderFavorites(View itemView) {
            super(itemView);
            // perform findViewById lookups
            name = (TextView) itemView.findViewById(R.id.tvCharityName);
            causeName = (TextView) itemView.findViewById(R.id.tvCause);
            tvDonateNow = itemView.findViewById(R.id.ibDonateNow);
            tvMoreInfo = itemView.findViewById(R.id.ibMoreInfo);
            ibCPLike = itemView.findViewById(R.id.ibCPLike);
            tvCPLikedNum = itemView.findViewById(R.id.tvCPLikedNum);
            ivcheckmarkfav = itemView.findViewById(R.id.ivcheckmarkfav);
            tvDonateNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition(); // gets item position
                    if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                        currentCharity = (Charity) items.get(position);
                        charityName2 = currentCharity.getKeyName();
                        charityLogo2 = currentCharity.getKeyLogo();
                    }
                    Intent intent = new Intent(view.getContext(), DonateActivity.class);
                    intent.putExtra("donateNow", true);
                    view.getContext().startActivity(intent);
                    ((Activity)view.getContext()).overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            });

            tvMoreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    CharityAPI charity = CharityAPI.fromParse((Charity) (items.get(position)));

                    Fragment fragment = new Charity_Profile_Fragment(charity);
                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                    fragmentManager.beginTransaction().
                            replace(R.id.flContainer, fragment)
                            .addToBackStack(null).commit();
                }
            });
        }

    }

    public class ViewHolderSuggested extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView tvCharityNameSugg;
        public TextView tvCategorySugg;
        public TextView tvCauseSugg;
        public TextView tvMoreInfo;
        public ImageButton ibCPLike;
        public TextView tvCPLikedNum;
        public ImageView  ivcheckmarksugg;
        public TextView tvMission;

        public ViewHolderSuggested(@NonNull View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            tvCharityNameSugg = itemView.findViewById(R.id.tvCharityNameSugg);
            tvCategorySugg = itemView.findViewById(R.id.tvCategorySugg);
            tvCauseSugg = itemView.findViewById(R.id.tvCauseSugg);
            tvMoreInfo = itemView.findViewById(R.id.ibMoreInfo);
            ibCPLike = itemView.findViewById(R.id.ibCPLike);
            tvCPLikedNum = itemView.findViewById(R.id.tvCPLikedNum);
            ivcheckmarksugg = itemView.findViewById(R.id.ivpaypal);
            tvMission = itemView.findViewById(R.id.tvMission);
        }

        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            CharityAPI charity = (CharityAPI) items.get(position);
            CustomDialogCharity dialog = new CustomDialogCharity(charity, from_charity_search);
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "CustomDialogCharity");
            return false;
        }

        @Override
        public void onClick(View view) {
            if (skip) {
                int position = getAdapterPosition(); // gets item position
                if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                    final CharityAPI selectedCharity = (CharityAPI) items.get(position);

                    ParseQuery<Charity> charityParseQuery = new ParseQuery<Charity>(Charity.class);
                    charityParseQuery.include(Charity.KEY_CHARITY_ID);
                    charityParseQuery.whereEqualTo("charityName", selectedCharity.getEin());

                    charityParseQuery.getFirstInBackground(new GetCallback<Charity>() {
                        @Override
                        public void done(Charity object, ParseException e) {
                            if (e != null) {
                                if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                                    addNewCharity(selectedCharity);
                                    return;
                                } else {
                                    Log.e("CharitySearchAdapter", "Error with query of charity");
                                }
                            } else {
                                currentCharity = object;
                                charityLogo2 = object.getKeyLogo();
                            }

                            charityName2 = selectedCharity.getName();


                            Intent intent = new Intent(view.getContext(), DonateFinalActivity.class);
                            view.getContext().startActivity(intent);
                            ((Activity)view.getContext()).overridePendingTransition(R.anim.enter, R.anim.exit);
                        }

                    });

                }
            } else {

                int position = getAdapterPosition();
                CharityAPI charity = (CharityAPI) items.get(position);

                Fragment fragment1 = new Charity_Profile_Fragment(charity);
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction().
                        replace(R.id.flContainer, fragment1)
                        .addToBackStack(null)
                        .commit();

            }
        }
    }




    @Override
    public int getItemViewType(int position) {
        //More to come
        if (items.get(position) instanceof String) {
            return TEXT;
        }else if ((items.get(position) instanceof CharityAPI)|| items.get(position) instanceof Charity) {
            return CHARITY;
        }
        return -1;

    }

    private void addNewCharity(CharityAPI selectedCharity) {
        newCharity = new Charity();
        newCharity.setKeyCategoryName(selectedCharity.getCategoryName());
        newCharity.setKeyCauseName(selectedCharity.getCauseName());
        newCharity.setKeyCharityID(selectedCharity.getEin());
        newCharity.setKeyMission(selectedCharity.getMission());
        newCharity.setKeyName(selectedCharity.getName());
        newCharity.setKeyRatingURL(selectedCharity.getRatingsUrl());
        newCharity.setKeyUrl(selectedCharity.getWebsiteUrl());

        newCharity.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("CharitySearchAdapter", "Created new charity");
                    currentCharity = newCharity;
                    charityLogo2 = newCharity.getKeyLogo();
                } else {
                    Log.d("CharitySearchAdapter", "Invalid charity");
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == CHARITY) {


            if (favorites) {
                // get data according to position.
                final ViewHolderFavorites vh3 = (ViewHolderFavorites) holder;
                Charity charity = (Charity) items.get(position);
                vh3.name.setText(charity.getKeyName());

                vh3.causeName.setText(Html.fromHtml("<font color=\"#434040\"><b>Cause:</b></font> "+charity.getKeyCauseName()));
                vh3.tvCPLikedNum.setText(((Integer)charity.getKeyNumLikes()).toString());

                String payPalnum = charity.getString("payPal");
                if(payPalnum == null || payPalnum.equals("")){
                    vh3.ivcheckmarkfav.setVisibility(View.INVISIBLE);
                }else{
                    vh3.ivcheckmarkfav.setVisibility(View.VISIBLE);
                }
                FavoriteCharities.setUpFavorites(charity, ParseUser.getCurrentUser(), vh3.ibCPLike, vh3.tvCPLikedNum);


            }else {
                CharityAPI charity = (CharityAPI) items.get(position);
                final ViewHolderSuggested vh1 = (ViewHolderSuggested) holder;

                // Initially set the checkmark to be invisible
                vh1.ivcheckmarksugg.setVisibility(View.INVISIBLE);

                // Initialize the values before you begin the thread process
                vh1.ibCPLike.setImageResource(R.drawable.ic_like_icon);
                vh1.ibCPLike.setColorFilter(Color.BLACK);
                vh1.tvCPLikedNum.setText("0");
                vh1.tvCharityNameSugg.setText(charity.getName());
                vh1.tvCategorySugg.setText(Html.fromHtml("<font color=\"#434040\"><b>Category:</b></font> " + charity.getCategoryName()));
                vh1.tvCauseSugg.setText(Html.fromHtml("<font color=\"#434040\"><b>Cause:</b></font> " + charity.getCauseName()));
                vh1.tvMission.setText(charity.getMission());

                ParseQuery<Charity> charityParseQuery = new ParseQuery<Charity>(Charity.class);
                charityParseQuery.include(Charity.KEY_CHARITY_ID);
                charityParseQuery.whereEqualTo("charityName", charity.getEin());

                charityParseQuery.getFirstInBackground(new GetCallback<Charity>() {
                    @Override
                    public void done(Charity object, ParseException e) {
                        if (e != null) {
                            if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {

                                // TODO this might occur after you setup the favorites
                                addNewCharity(charity);


                            } else {
                                Log.e("CharitySearchAdapter", "Error with query of charity");
                            }
                        } else {
                            currentCharity = object;
                        }
                        if(currentCharity != null){

                            String payPalnum = currentCharity.getString("payPal");

                            if(payPalnum == null || payPalnum.equals("")){
                                vh1.ivcheckmarksugg.setVisibility(View.INVISIBLE);
                            }else{

                                vh1.ivcheckmarksugg.setVisibility(View.VISIBLE);
                            }

                            vh1.tvCPLikedNum.setText(((Integer)currentCharity.getKeyNumLikes()).toString());
                            FavoriteCharities.setUpFavorites(currentCharity, ParseUser.getCurrentUser(), vh1.ibCPLike, vh1.tvCPLikedNum);
                    }
                    }

                });
            }
        }
        else if (holder.getItemViewType() == TEXT){
            String header = (String) items.get(position);
            ViewHolderText vh2 = (ViewHolderText) holder;
            vh2.tvHeader.setText(header);
            if(header != "Recommended Effective Charities"){
                vh2.ibQuestion.setVisibility(View.INVISIBLE);
                vh2.ibQuestion.setClickable(false);
            }
            else{
                vh2.ibQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO change to dialog
                        InfoDialog infoDialog = new InfoDialog();
                        infoDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "InfoDialog");

                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    // Clean all elements of the recycler
    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(ArrayList<Object> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }




}
