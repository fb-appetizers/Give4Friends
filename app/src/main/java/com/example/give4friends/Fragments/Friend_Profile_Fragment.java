package com.example.give4friends.Fragments;

import android.content.Context;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.give4friends.Adapters.ProfilePagerAdapter;
import com.example.give4friends.Cutom_Classes.CustomDialogProfileImage;
import com.example.give4friends.R;
import com.google.android.material.tabs.TabLayout;
import com.parse.ParseUser;

import java.util.Date;

public class Friend_Profile_Fragment extends Fragment {



    public ImageView ivProfileImage;
    public TextView tvUserName;
    public TextView tvBio;
    public TextView tvFullName;

    TabLayout FavMileToolbarFriend;
    ViewPager viewPagerFriend;
    PagerAdapter pagerAdapter;


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

        FavMileToolbarFriend = view.findViewById(R.id.FavMileToolbarFriend);
        viewPagerFriend = view.findViewById(R.id.viewPagerFriend);


        FavMileToolbarFriend.addTab(FavMileToolbarFriend.newTab().setText("Favorites"));
        FavMileToolbarFriend.addTab(FavMileToolbarFriend.newTab().setText("Milestones"));


        FavMileToolbarFriend.setTabGravity(FavMileToolbarFriend.GRAVITY_FILL);

        pagerAdapter = new ProfilePagerAdapter(getChildFragmentManager(), FavMileToolbarFriend.getTabCount(), myUser);
        viewPagerFriend.setAdapter(pagerAdapter);
        viewPagerFriend.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(FavMileToolbarFriend));

        FavMileToolbarFriend.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerFriend.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


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

        String imageURL = myUser.getString("profileImageURL");
        if (imageURL!=null) {
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


        ivProfileImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Date imageDate = myUser.getDate("profileImageCreatedAt");
                CustomDialogProfileImage dialog = new CustomDialogProfileImage(imageURL, imageDate);
                dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "CustomDialogCharity");

                return false;
            }
        });
    }


    // A stripped down version of the toolbar
    protected void configureToolbarStripped() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTextSize(24);
        toolbarTitle.setText(myUser.getUsername());

        toolbar.setNavigationIcon(R.drawable.ic_x);

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
                Fragment fragment = new History_Fragment(myUser, true);
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                fragmentManager.beginTransaction().
                        replace(R.id.flContainer, fragment)
                        .addToBackStack(null).commit();
                return true;
            default:

        }
        return true;
    }



//    private void populateRelations() {
//        charities.add("Favorite Charities");
//        //Get relation
//        final ParseRelation<Charity> favCharities = myUser.getRelation("favCharities");
//
//        //Get all charities in relation
//        favCharities.getQuery().findInBackground(new FindCallback<Charity>() {
//            @Override
//            public void done(List<Charity> objects, ParseException e) {
//                if (e != null) {
//                    // There was an error
//                } else {
//                    if(objects.size() == 0) {
//                        Toast.makeText(context, myUser.getString("firstName") + " does not have any favorites yet", Toast.LENGTH_SHORT).show();
//                    }
//                    // results have all the charities the current user liked.
//                    // go through relation adding charities
//                    for (int i = 0; i < objects.size(); i++) {
//                        charities.add((Charity) objects.get(i));
//
//                    }
//                    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//                    rvCharities.setLayoutManager(linearLayoutManager);
//
//                    //construct the adapter from this datasource
//                    feedAdapter = new CharitySuggAdapter(charities, false, false, true);
//                    //RecyclerView setup (layout manager, use adapter)
//                    rvCharities.setAdapter(feedAdapter);
//                    rvCharities.scrollToPosition(0);
//
//                }
//            }
//        });
//
//    }

}



