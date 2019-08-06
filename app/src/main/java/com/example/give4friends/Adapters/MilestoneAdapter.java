package com.example.give4friends.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.give4friends.R;
import com.parse.ParseUser;

import java.util.ArrayList;

public class MilestoneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<Object> items;
    ArrayList<Object> completedMilestones;
    ParseUser myUser;
    Context context;


    public MilestoneAdapter(ArrayList<Object> items, ParseUser myUser, ArrayList<Object> completedMilestones) {
        this.items = items;
        this.myUser = myUser;
        this.completedMilestones = completedMilestones;
    }

    private final int TEXT = 0, MILESTONE = 1;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == MILESTONE) {
                View v1 = inflater.inflate(R.layout.item_milestone, parent, false);
                viewHolder = new MilestoneAdapter.ViewHolderMilestones(v1);
            return viewHolder;
        } else if (viewType == TEXT) {
            View v2 = inflater.inflate(R.layout.item_header, parent, false);
            viewHolder = new MilestoneAdapter.ViewHolderText(v2);
            return viewHolder;
        }
        return viewHolder;
    }
    //first check done

    public class ViewHolderText extends RecyclerView.ViewHolder{
        public TextView tvHeader;
        public ViewHolderText(@NonNull View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tvHeader);
        }
    }
    //first check done

    public class ViewHolderMilestones extends RecyclerView.ViewHolder{

        ImageView ivMilestone;
        TextView tvMilestoneName;

        public ViewHolderMilestones(View itemView) {
            super(itemView);

            tvMilestoneName = (TextView) itemView.findViewById(R.id.tvMilestoneName);
            ivMilestone = itemView.findViewById(R.id.ivMilestone);



        }
    }

    @Override
    public int getItemViewType(int position) {
        //More to come
        if (items.get(position) instanceof String) {
            return TEXT;
        }else  {
            return MILESTONE;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == MILESTONE) {
            // get data according to position.
            final ViewHolderMilestones vh3 = (ViewHolderMilestones) holder;
            Pair<String, Integer> milestone = (Pair<String, Integer>) items.get(position);

            ((ViewHolderMilestones) holder).tvMilestoneName.setText(milestone.first);
            ((ViewHolderMilestones) holder).ivMilestone.setImageResource(milestone.second);

            if(completedMilestones != null && completedMilestones.contains(milestone.first)){
                //acheived!
                ((ViewHolderMilestones) holder).ivMilestone.setColorFilter(Color.parseColor("#CFB7E6"), PorterDuff.Mode.SRC_ATOP);
            }else{
                ((ViewHolderMilestones) holder).ivMilestone.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            }

        } else if (holder.getItemViewType() == TEXT) {
            String header = (String) items.get(position);
            MilestoneAdapter.ViewHolderText vh2 = (MilestoneAdapter.ViewHolderText) holder;
            vh2.tvHeader.setText(header);
        }
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







