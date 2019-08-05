package com.example.give4friends.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.give4friends.DonateActivity;
import com.example.give4friends.Fragments.Charity_Profile_Fragment;
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;

import java.util.ArrayList;
import java.util.List;

import static com.example.give4friends.DonateActivity.charityName2;
import static com.example.give4friends.DonateActivity.currentCharity;


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
        holder.name.setText(charity.getKeyName());
        //holder.name.setMovementMethod(LinkMovementMethod.getInstance());
        //holder.name.setText(Html.fromHtml("<a href=\'"+charity.getKeyWebsiteURL()+"\'>"
               // +charity.getKeyName() + " ("
               // + charity.getKeyCategoryName() + ")"+ "</a>"));
        holder.causeName.setText(Html.fromHtml("<font color=\"#434040\"><b>Cause:</b></font> "+charity.getKeyCauseName()));
        }

    @Override
    public int getItemCount() {
        return charities.size();
    }


    // create ViewHolder Class
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView causeName;
        public TextView tvDonateNow;
        public TextView tvMoreInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            // perform findViewById lookups
            name = (TextView) itemView.findViewById(R.id.tvCharityName);
            causeName = (TextView) itemView.findViewById(R.id.tvCause);
            tvDonateNow = itemView.findViewById(R.id.ibDonateNow);
            tvMoreInfo = itemView.findViewById(R.id.ibMoreInfo);

            tvDonateNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition(); // gets item position
                    if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                        currentCharity = charities.get(position);
                        charityName2 = currentCharity.getKeyName();
                    }
                    Intent intent = new Intent(view.getContext(), DonateActivity.class);
                    intent.putExtra("donateNow", true);
                    view.getContext().startActivity(intent);
                }
            });


            tvMoreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    CharityAPI charity = CharityAPI.fromParse(charities.get(position));

                    Fragment fragment = new Charity_Profile_Fragment(charity);
                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                    fragmentManager.beginTransaction().
                            replace(R.id.flContainer, fragment)
                            .addToBackStack(null).commit();
                }
            });
        }

    }
}


