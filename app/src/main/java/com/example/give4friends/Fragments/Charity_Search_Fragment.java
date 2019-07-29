package com.example.give4friends.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.give4friends.Adapters.CharitySuggAdapter;
import com.example.give4friends.CharitySearch;
import com.example.give4friends.MainActivity;
import com.example.give4friends.Main_Fragment_Branch;
import com.example.give4friends.ParseApplication;
import com.example.give4friends.R;
import com.example.give4friends.SettingsActivity;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.CharityAPI;
import com.example.give4friends.net.CharityClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseSession;
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

    private TextInputEditText etCharity;
    private SearchView sbCharity;
    private Button btnCancel;
    private RecyclerView rvCharitySugg;

    public static Integer NUMBER_OF_SUGGESTIONS = 10;

    CharityClient client;
    CharitySuggAdapter charityAdapterUpper;
    private ArrayList<Object> items;

    ConstraintLayout constraintLayoutMain;
    ProgressBar progressBarHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_charity_search, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        etCharity = view.findViewById(R.id.etCharity);
        sbCharity = view.findViewById(R.id.sbCharity);
        rvCharitySugg = view.findViewById(R.id.rvCharitySugg);
        etCharity = view.findViewById(R.id.etCharity);
        btnCancel = view.findViewById(R.id.btnCancel);

        progressBarHome = getActivity().findViewById(R.id.progressBarHome);

        configureToolbar();

        sbCharity.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }


            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")){
                    if(client!=null) {
                        client.getClient().dispatcher().cancelAll();
                    }

                    getEffective();
                }
                else{
                    getResponseSearch(s.toString(),false);
                }


                return false;
            }
        });


        constraintLayoutMain = view.findViewById(R.id.clCharitySearch);

        items = new ArrayList<>();

        charityAdapterUpper = new CharitySuggAdapter(items, false, false);
        // attach the adapter to the RecyclerView
        rvCharitySugg.setAdapter(charityAdapterUpper);

        // Set layout manager to position the items
        rvCharitySugg.setLayoutManager(new LinearLayoutManager(getContext()));


        getEffective();
//        Toast.makeText(getContext(), "Created", Toast.LENGTH_SHORT).show();
    }



    protected void configureToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTextSize(30);
        toolbarTitle.setText("Charity Search");

        toolbar.setNavigationIcon(R.drawable.ic_settings);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

    }

    // TODO -- shut down a thread when another one calls it

    private void getResponseSearch(String search, boolean search_by_name){

        if(client!=null){
            //Clears all of the previous client calls
            client.getClient().dispatcher().cancelAll();
        }

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

                                items.clear();
                                for(CharityAPI charityAPI : charities){
                                    items.add(charityAPI);
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


    // from the link above
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(getContext(), "keyboard visible", Toast.LENGTH_LONG).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(getContext(), "keyboard hidden", Toast.LENGTH_LONG).show();
        }
    }


    private void getResponseSuggested(Integer size_of_recommended){

        // You check if the size of the already inputted recommended list matches up with what
        // you have in the items. This is to prevent a duplicate call from the parallel threads
        if(items.size() <= size_of_recommended) {
            ParseQuery<ParseUser> postQuery = new ParseQuery<ParseUser>(ParseUser.class);
            postQuery.include("charityArray");
            postQuery.setLimit(1);

            postQuery.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());


            postQuery.getFirstInBackground(new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser object, ParseException e) {
                    List<Charity> charities = object.getList("charityArray");
                    if (charities == null) {
                        charities = new ArrayList<Charity>();
                    }

                    if (charities.size() > 0) {
                        items.add("Charities Suggested For You");
                    }
                    // The suggestedNum now goes in decending order
                    Integer suggestedNum = Integer.max((Integer) (charities.size() - 1 - NUMBER_OF_SUGGESTIONS), 0);

                    for (int i = (charities.size() - 1); i > suggestedNum; i--) {
                        items.add(CharityAPI.fromParse(charities.get(i)));
                    }

                    charityAdapterUpper.notifyDataSetChanged();
                    hideProgressBar();
                }
            });

        }
    }


    public void showProgressBar() {
        // Show progress item


        progressBarHome.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        // Hide progress item

        progressBarHome.setVisibility(View.INVISIBLE);
    }

    public void getEffective(){

        items.clear();
        items.add("Recommended Effective Charities");

        ParseQuery<Charity> postQuery = new ParseQuery<Charity>(Charity.class)
                .whereEqualTo("highlyEffective", true);


        postQuery.findInBackground(new FindCallback<Charity>() {
            //iterate through query
            @Override
            public void done(List<Charity> objects, ParseException e) {

                // DO this to prevent duplication during parallel threads
                if (items.size() <= 1) {
                    if (e == null) {
                        for (int i = 0; i < objects.size(); ++i) {
                            items.add(CharityAPI.fromParse(objects.get(i)));
                        }
                    } else {
                        Log.e("MainActivity", "Can't get transaction");
                        e.printStackTrace();
                    }
                    getResponseSuggested(objects.size() + 1);
                }
            }
        });


    }




}
