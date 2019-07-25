package com.example.give4friends.Adapters;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.give4friends.CharityProfile;
import com.example.give4friends.Cutom_Classes.CustomDialog;
import com.example.give4friends.Fragments.Charity_Profile_Fragment;
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;

import org.parceler.Parcels;

import java.util.List;

public class CharitySuggAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CharityAPI> mCharity;
    private Context context;

    public CharitySuggAdapter(List<CharityAPI> mCharity) {
        this.mCharity = mCharity;
    }

    private final int EFFECTIVE = 0, SUGGESTED = 1;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == EFFECTIVE) {

            View v1 = inflater.inflate(R.layout.effective_charities, parent, false);
            viewHolder = new CharitySuggAdapter.ViewHolderEffective(v1);
            return viewHolder;

        } else if (viewType == SUGGESTED) {
            View v2 = inflater.inflate(R.layout.item_charity_sugg, parent, false);
            viewHolder = new CharitySuggAdapter.ViewHolderSuggested(v2);
            return viewHolder;
        }

        return viewHolder;

    }


    public class ViewHolderEffective extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ViewHolderEffective(@NonNull View itemView) {
            super(itemView);

        }
        @Override
        public void onClick(View view) {

        }
        // fill list of effective charities here
        // in other it is charity


    }

    public class ViewHolderSuggested extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        // in other it is comments
        // the recycler view we have now

        public TextView tvCharityNameSugg;
        public TextView tvCategorySugg;
        public TextView tvCauseSugg;
        public TextView tvMoreInfo;


        public ViewHolderSuggested(@NonNull View itemView) {
            super(itemView);

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            tvCharityNameSugg = itemView.findViewById(R.id.tvCharityNameSugg);
            tvCategorySugg = itemView.findViewById(R.id.tvCategorySugg);
            tvCauseSugg = itemView.findViewById(R.id.tvCauseSugg);
            tvMoreInfo = itemView.findViewById(R.id.tvMoreInfo);


        }

        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            CharityAPI charity = mCharity.get(position);

            CustomDialog dialog = new CustomDialog(charity);
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "CustomDialog");


            return false;
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            CharityAPI charity = mCharity.get(position);


            Fragment fragment1 = new Charity_Profile_Fragment(charity);
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            fragmentManager.beginTransaction().
                    replace(R.id.flContainer, fragment1)
                    .addToBackStack(null).commit();


            // Send an intent to the Charity Profile
//            Toast.makeText(context,"This is a click",Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent(context, CharityProfile.class);
//
//            intent.putExtra("Charity", Parcels.wrap(charity));
//
//            context.startActivity(intent);

        }


    }

    @Override
    public int getItemViewType(int position) {
        //More to come

        if (position == 0) {
            return EFFECTIVE;
        } else {
            return SUGGESTED;
        }

    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == SUGGESTED) {
            CharityAPI charity = mCharity.get(position);
            final ViewHolderSuggested vh1 = (ViewHolderSuggested) holder;
            vh1.tvCharityNameSugg.setText(charity.getName());

            vh1.tvCategorySugg.setText(Html.fromHtml("<font color=\"#434040\"><b>Category:</b></font> " + charity.getCategoryName()));
            vh1.tvCauseSugg.setText(Html.fromHtml("<font color=\"#434040\"><b>Cause:</b></font> " + charity.getCauseName()));
        }
        else if (holder.getItemViewType() == EFFECTIVE){
            ViewHolderEffective vh2 = (ViewHolderEffective) holder;
        }
    }

    @Override
    public int getItemCount() {
        return mCharity.size();
    }


}
