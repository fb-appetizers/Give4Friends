package com.example.give4friends.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.give4friends.Adapters.CharitySuggAdapter;
import com.example.give4friends.CharitySearch;
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;
import com.example.give4friends.net.CharityClient;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Charity_Search_Fragment extends Fragment {


    private EditText etCharity;
    private RecyclerView rvCharitySearch;
    private Button btnSubmit;
    private RecyclerView rvCharitySugg;

    CharityClient client;
    ArrayList<CharityAPI> acharitiesLower;
    ArrayList<CharityAPI> acharitiesUpper;
    CharitySuggAdapter charityAdapterUpper;

    ConstraintLayout constraintLayoutMain;
    ProgressBar progressBarHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_charity_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etCharity = view.findViewById(R.id.etCharity);

        rvCharitySugg = view.findViewById(R.id.rvCharitySugg);
        etCharity = view.findViewById(R.id.etCharity);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        progressBarHome = getActivity().findViewById(R.id.progressBarHome);

        constraintLayoutMain = view.findViewById(R.id.clCharitySearch);


        acharitiesLower = new ArrayList<CharityAPI>();
        acharitiesUpper = new ArrayList<CharityAPI>();

        charityAdapterUpper = new CharitySuggAdapter(acharitiesUpper);



        // attach the adapter to the RecyclerView
        rvCharitySugg.setAdapter(charityAdapterUpper);



        // Set layout manager to position the items
        rvCharitySugg.setLayoutManager(new LinearLayoutManager(getContext()));



        getResponseSuggested();




//TODO -- search up MODALS/POPUP


        //When you hit submit the recycler view updates
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getResponseSearch(etCharity.getText().toString(),false);





            }
        });



    }


    private void getResponseSearch(String search, boolean search_by_name){

        client = new CharityClient();
        showProgressBar();
        client.getCharities(search, false, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if (response.isSuccessful()){
                    final String myResponse = response.body().string();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray charityArray;
                                charityArray = new JSONArray(myResponse);


                                final ArrayList <CharityAPI> charities = CharityAPI.fromJSON(charityArray);

                                acharitiesUpper.clear();
                                for(CharityAPI charityAPI : charities){
                                    acharitiesUpper.add(charityAPI);
                                }
                                charityAdapterUpper.notifyDataSetChanged();
                                hideProgressBar();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                hideProgressBar();
                            }
                        }
                    });
                }
            }
        });

    }


    private void getResponseSuggested(){

        ParseUser mainUser = ParseUser.getCurrentUser();

        ParseQuery<ParseUser> postQuery = new ParseQuery<ParseUser>(ParseUser.class);
        postQuery.include("charityArray");
        postQuery.setLimit(1);
        postQuery.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());

        postQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                ParseUser mainUser = objects.get(0);// They'll only be one
                List <Charity> charities = mainUser.getList("charityArray");
                if (charities == null){
                    charities = new ArrayList<Charity>();
                }

                for (Charity charity : charities) {
                    acharitiesUpper.add(CharityAPI.fromParse(charity));
                }


                charityAdapterUpper.notifyDataSetChanged();


            }
        });


    }


    public void showProgressBar() {
        // Show progress item


        progressBarHome.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        // Hide progress item

        progressBarHome.setVisibility(View.INVISIBLE);
    }




}
