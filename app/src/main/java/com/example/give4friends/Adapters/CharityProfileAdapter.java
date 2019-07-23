package com.example.give4friends.Adapters;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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

import java.util.List;

public class CharityProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private Context context;
    private List<Object> items;

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

        if(viewType == CHARITY) {

              View v1 = inflater.inflate(R.layout.item_main_profile_view, viewGroup, false);
              viewHolder = new CharityProfileAdapter.ViewHolderCharity(v1);
              return viewHolder;

        }
        else if(viewType == COMMENT) {
              View v2 = inflater.inflate(R.layout.activity_comment_profile, viewGroup, false);
              viewHolder = new CharityProfileAdapter.ViewHolderComment(v2);
              return viewHolder;
        }


        return viewHolder;


    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {


        if (viewHolder.getItemViewType() == CHARITY) {
            CharityAPI charity = (CharityAPI) items.get(position);
            ViewHolderCharity vh1 = (ViewHolderCharity) viewHolder;


            vh1.tvCPname.setText("Hello");
//                vh1.tvCPname.setMovementMethod(LinkMovementMethod.getInstance());
//
//
//                vh1.tvCPname.setText(Html.fromHtml("<a href=\'"+charity.getWebsiteUrl()+"\'>"
//                        +charity.getName()+ "</a>"));
//
//                vh1.tvCPMission.setText(Html.fromHtml(charity.getMission()));
//                vh1.tvCPCategory.setText(Html.fromHtml("<font color=\"#434040\"><b>Category:</b></font> "+charity.getCategoryName()));
//                vh1.tvCPCause.setText(Html.fromHtml("<font color=\"#434040\"><b>Cause:</b></font> "+charity.getCauseName()));
        }else if (viewHolder.getItemViewType() == COMMENT){


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
        } else  {
            return COMMENT;
        }

    }


    public class ViewHolderCharity extends RecyclerView.ViewHolder implements View.OnClickListener{


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

    public class ViewHolderComment extends RecyclerView.ViewHolder implements View.OnClickListener{


        public ViewHolderComment(@NonNull View itemView) {
            super(itemView);


        }

        @Override
        public void onClick(View view) {

        }
    }
}
