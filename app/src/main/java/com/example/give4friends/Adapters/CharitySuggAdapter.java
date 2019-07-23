package com.example.give4friends.Adapters;

import android.app.Activity;
import android.app.FragmentManager;
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

public class CharitySuggAdapter extends RecyclerView.Adapter<CharitySuggAdapter.ViewHolder> {


    private List<CharityAPI> mCharity;
    private Context context;

    public CharitySuggAdapter(List<CharityAPI> mCharity) {
        this.mCharity = mCharity;

    }


    @Override
    public CharitySuggAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View bookView = inflater.inflate(R.layout.item_charity_sugg, parent, false);

        // Return a new holder instance
        CharitySuggAdapter.ViewHolder viewHolder = new CharitySuggAdapter.ViewHolder(bookView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull CharitySuggAdapter.ViewHolder holder, int position) {
        CharityAPI charity = mCharity.get(position);
        holder.tvCharityNameSugg.setText(charity.getName());

        holder.tvCategorySugg.setText(Html.fromHtml("<font color=\"#434040\"><b>Category:</b></font> "+charity.getCategoryName()));
        holder.tvCauseSugg.setText(Html.fromHtml("<font color=\"#434040\"><b>Cause:</b></font> "+charity.getCauseName()));
    }

    @Override
    public int getItemCount() {
        return mCharity.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        public TextView tvCharityNameSugg;
        public TextView tvCategorySugg;
        public TextView tvCauseSugg;
        public TextView tvMoreInfo;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            tvCharityNameSugg = itemView.findViewById(R.id.tvCharityNameSugg);
            tvCategorySugg = itemView.findViewById(R.id.tvCategorySugg);
            tvCauseSugg = itemView.findViewById(R.id.tvCauseSugg);
            tvMoreInfo = itemView.findViewById(R.id.tvMoreInfo);


        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            CharityAPI charity = mCharity.get(position);

            CustomDialog dialog = new CustomDialog(charity);
            dialog.show(((AppCompatActivity)context).getSupportFragmentManager(), "CustomDialog");


            return false;
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            CharityAPI charity = mCharity.get(position);


//            Fragment fragment = new Charity_Profile_Fragment(charity);
//            FragmentManager fragmentManager = ((AppCompatActivity)context).getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//            fragmentTransaction.replace(R.id.flContainer, fragment);
//
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();

            // Send an intent to the Charity Profile
            Toast.makeText(context,"This is a click",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(context, CharityProfile.class);

            intent.putExtra("Charity", Parcels.wrap(charity));

            context.startActivity(intent);

        }


    }
}
