package com.example.give4friends.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.give4friends.Adapters.CharityProfileAdapter;
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;
import com.example.give4friends.models.Comments;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static com.example.give4friends.DonateActivity.currentCharity;

public class Charity_Profile_Fragment extends Fragment {

    RecyclerView rvCPProfile;
    ArrayList<Object> items;
    CharityAPI charity;
    CharityProfileAdapter itemsAdapter;

    public Charity_Profile_Fragment(CharityAPI charity) {
        this.charity = charity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_charity_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        configureToolbar();
        rvCPProfile = view.findViewById(R.id.rvCPProfile);

        items = new ArrayList<Object>();


        itemsAdapter = new CharityProfileAdapter(items);

        // attach the adapter to the RecyclerView
        rvCPProfile.setAdapter(itemsAdapter);

        // Set layout manager to position the items
        rvCPProfile.setLayoutManager(new LinearLayoutManager(getContext()));


        populateProfile();


        populateComments();

    }


    private void populateProfile(){
        items.add(charity);
        itemsAdapter.notifyItemInserted(items.size() - 1);
    }


    private void populateComments(){
        //First find the charity with the ID in parse

        ParseQuery<Charity> charityParseQuery = new ParseQuery<Charity>(Charity.class);
        charityParseQuery.include(Charity.KEY_CHARITY_ID);

        charityParseQuery.whereEqualTo("charityName", charity.getEin());

        charityParseQuery.getFirstInBackground(new GetCallback<Charity>() {
            @Override
            public void done(Charity charity, ParseException e) {
                if (e != null) {
                    if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {

                        return;
                    } else {
                        Log.e("CharitySearchAdapter", "Error with query of charity");
                    }
                } else {
                    ParseRelation<Comments>  comments= charity.getRelation("UserComments");

                    comments.getQuery().findInBackground(new FindCallback<Comments>() {
                        @Override
                        public void done(List<Comments> comment_obj, ParseException e) {

                            if (e != null) {
                                // There was an error
                            } else {
                                // results have all the charities the current user liked.
                                // go through relation adding charities
                                for (int i = 0; i < comment_obj.size(); i++) {
                                    items.add(comment_obj.get(i));
                                }
                                itemsAdapter.notifyDataSetChanged();
                            }

                        }


                    });






                }
            }
        });

    }




    protected void configureToolbar() {

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(charity.getName());
        toolbarTitle.setTextSize(17);

        toolbar.setNavigationIcon(R.drawable.ic_x);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();

            }
        });

    }




}
