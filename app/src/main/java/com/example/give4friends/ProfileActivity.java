package com.example.give4friends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    FavCharitiesAdapter feedAdapter;
    ArrayList<String> charities;
    RecyclerView rvCharities;
    private SwipeRefreshLayout swipeContainer;
    private Object FavCharitiesAdapter;

    public ImageView ivProfileImage;
    public TextView tvUserName;
    public TextView tvBio;
    public TextView tvTotalRaised;
    public TextView tvTotalDonated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //Below for recycler view of charities

        //find the RecyclerView
        rvCharities = (RecyclerView) findViewById(R.id.rvFavCharities);

        // initialize the array list (starting with just array of strings)
        charities = new ArrayList<String>();
        // Temporarily add charities
        // Initialize an ArrayList with add()
        charities.add("A");
        charities.add("B");
        charities.add("C");

        //construct the adapter from this datasource
        feedAdapter = new FavCharitiesAdapter(charities);

        //RecyclerView setup (layout manager, use adapter)

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setReverseLayout(true);
        rvCharities.setLayoutManager(linearLayoutManager);
        rvCharities.setAdapter(feedAdapter);
        rvCharities.scrollToPosition(0);


        // Below for static elements of profile


        ivProfileImage = (ImageView) findViewById((R.id.ivProfileImage));
        tvUserName = (TextView) findViewById(R.id.tvName);
        tvBio= (TextView) findViewById(R.id.tvBio);
        tvTotalRaised= (TextView) findViewById((R.id.tvTotalRaised));
        tvTotalDonated = (TextView) findViewById((R.id.tvTotalDonated));




        // Actually query and fill





        //get query -- get user info
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
        postQuery.whereGreaterThan(Post.KEY_CREATED_AT, posts.get(page).getKeyCreatedAt());
        postQuery.addDescendingOrder(Post.KEY_CREATED_AT);
        postQuery.findInBackground(new FindCallback<Post>() {
            //iterate through query
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null){
                    for (int i = 0; i < objects.size(); ++i){
                        posts.add(objects.get(i));
                        feedAdapter.notifyItemInserted(posts.size() - 1);

                        // from old vid example of getting things
                        Log.d("FeedActivity", "Post[" + i + "] = " + objects.get(i).getDescription() + "\nusername = " + objects.get(i).getUser().getUsername());
                    }
                }else {
                    Log.e("FeedActivity", "Can't get post");
                    e.printStackTrace();
                }
            }
        });




    }
}
