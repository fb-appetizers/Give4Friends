package com.example.give4friends.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.give4friends.DonateSearchCharity;
import com.example.give4friends.R;

import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

import static com.example.give4friends.DonateActivity.friend;


public class DonateAdapter extends RecyclerView.Adapter<DonateAdapter.ViewHolder>{
    Context context;
    private List<ParseUser> friends;

    public DonateAdapter(List<ParseUser> friends){ this.friends = friends; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_donate, parent, false);
        return new DonateAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParseUser user = friends.get(position);

        holder.friendsName.setText(user.get("firstName") + " " + user.get("lastName"));
        holder.friendsUserName.setText("@" + user.getUsername());

        ParseFile image = user.getParseFile("profileImage");

        if(image != null){
            Glide.with(context)
                    .load(image.getUrl())
//                .bitmapTransform(new CropCircleTransformation())
//                .placeholder(R.drawable.ic_account)
                    .into(holder.friendImage);
        }
    }

    @Override
    public int getItemCount() {
        return this.friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView friendsName;
        public TextView friendsUserName;
        public ImageView friendImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            friendsName = itemView.findViewById(R.id.friendsName);
            friendsUserName = itemView.findViewById(R.id.friendsUserName);
            friendImage = itemView.findViewById(R.id.friendImage);

            itemView.setOnClickListener(this);
        }

        // Handles the row being clicked
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                friend = friends.get(position);
                Toast.makeText(context, "Friend: " + friend.getUsername(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context, DonateSearchCharity.class);
                intent.putExtra("friend", friend.getUsername());
                context.startActivity(intent);
            }
        }
    }
}
