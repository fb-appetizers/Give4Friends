package com.example.give4friends.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.give4friends.Adapters.CharityProfileAdapter;
import com.example.give4friends.R;
import com.example.give4friends.models.CharityAPI;

import org.parceler.Parcels;

import java.util.ArrayList;

public class Charity_Profile_Fragment extends Fragment{

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

        rvCPProfile = view.findViewById(R.id.rvCPProfile);

        items = new ArrayList<Object>();


        itemsAdapter = new CharityProfileAdapter(items);

        // attach the adapter to the RecyclerView
        rvCPProfile.setAdapter(itemsAdapter);

        // Set layout manager to position the items
        rvCPProfile.setLayoutManager(new LinearLayoutManager(getContext()));

        populateProfile();

    }



    private void populateProfile(){
        items.add(charity);
        itemsAdapter.notifyItemInserted(items.size() - 1);

    }

}
