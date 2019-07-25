package com.example.give4friends.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.give4friends.Adapters.FavCharitiesAdapter;
import com.example.give4friends.HistoryActivity;
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Friend_Profile_Fragment extends Fragment {

    com.example.give4friends.Adapters.FavCharitiesAdapter feedAdapter;
    ArrayList<Charity> charities;
    RecyclerView rvCharities;
    private SwipeRefreshLayout swipeContainer;

    public ImageView ivProfileImage;
    public TextView tvUserName;
    public TextView tvBio;
    public TextView tvFullName;


    ParseUser myUser;
    Context context;

    public Friend_Profile_Fragment(ParseUser myUser) {
        this.myUser = myUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.activity_friend_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();


        configureToolbarStripped();
        setHasOptionsMenu(true);

        //Below for recycler view of charities
        rvCharities = (RecyclerView) view.findViewById(R.id.rvFavCharities);
        // initialize the array list of charities
        charities = new ArrayList<Charity>();
        populateRelations();
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                feedAdapter.clear();
                feedAdapter.addAll(charities);
                populateRelations();
                swipeContainer.setRefreshing(false);
            }

        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Below for static elements of profile
        ivProfileImage = (ImageView) view.findViewById((R.id.ivProfileImage));
        tvUserName = (TextView) view.findViewById(R.id.tvName);
        tvBio = (TextView) view.findViewById(R.id.tvBio);
        tvFullName = (TextView) view.findViewById(R.id.tvFullName);

        tvUserName.setText("@" + myUser.getUsername());
        if(myUser.getString("bio") == null){
            tvBio.setText(" ");
        }
        else{
            tvBio.setText(myUser.getString("bio"));
        }
        tvBio.setEnabled(false);
        tvFullName.setText(myUser.getString("firstName") + " " + myUser.getString("lastName"));

        //Handles images
        ParseFile file = myUser.getParseFile("profileImage");

        if (file!=null) {
            Glide.with(context)
                    .load(file.getUrl())
                    .apply(new RequestOptions()
                            .transforms(new CenterCrop(), new RoundedCorners(20))
                            .circleCropTransform()
                            .placeholder(R.drawable.user_outline_24)
                            .error(R.drawable.user_outline_24))
                    .into(ivProfileImage);
        }
        else{
            Glide.with(context)
                    .load(R.drawable.user_outline_24)
                    .apply(new RequestOptions()
                            .transforms(new CenterCrop(), new RoundedCorners(20))
                            .circleCropTransform()
                            .placeholder(R.drawable.user_outline_24)
                            .error(R.drawable.user_outline_24))
                    .into(ivProfileImage);
        }
    }


    // A stripped down version of the toolbar
    protected void configureToolbarStripped() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTextSize(24);
        toolbarTitle.setText(myUser.getUsername());

        toolbar.setNavigationIcon(R.drawable.ic_cancel_2);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pops back the fragment if you cancel
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater main_activity_inflater = getActivity().getMenuInflater();


        main_activity_inflater.inflate(R.menu.friend_user_menu, menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.reportUser:
                Toast.makeText(getContext(), "Report User Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.TransactionHistory:
                Toast.makeText(context, "Transaction History selected", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, HistoryActivity.class);
                intent.putExtra("user", myUser);
                startActivity(intent);
                return true;
            default:
//                Log.e()
        }
        return true;
    }



    private void populateRelations() {
        //Get relation
        final ParseRelation<Charity> favCharities = myUser.getRelation("favCharities");
        //Get all charities in relation
        favCharities.getQuery().findInBackground(new FindCallback<Charity>() {
            @Override
            public void done(List<Charity> objects, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    // results have all the charities the current user liked.
                    // go through relation adding charities
                    for (int i = 0; i < objects.size(); i++) {
                        charities.add((Charity) objects.get(i));

                    }
                    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    rvCharities.setLayoutManager(linearLayoutManager);

                    //construct the adapter from this datasource
                    feedAdapter = new FavCharitiesAdapter(charities);
                    //RecyclerView setup (layout manager, use adapter)
                    rvCharities.setAdapter(feedAdapter);
                    rvCharities.scrollToPosition(0);

                }
            }
        });

    }

}



