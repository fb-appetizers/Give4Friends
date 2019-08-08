package com.example.give4friends;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    ImageButton ibcancelSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        ibcancelSettings = findViewById(R.id.ibcancelSettings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ibcancelSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putString("username", ParseUser.getCurrentUser().getUsername());
        edit.putString("password", ParseUser.getCurrentUser().getString("password"));
        edit.putString("email", ParseUser.getCurrentUser().getString("email"));
        edit.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @Override
        public void onResume() {
            super.onResume();
            PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }

        private void updateSummary(EditTextPreference preference) {
            // set the EditTextPreference's summary value to its current text
            preference.setSummary(preference.getText());
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            switch (s){
                case "username":
                    ParseUser.getCurrentUser().setUsername(sharedPreferences.getString(s, "username"));
                    break;
                case "password":
                    ParseUser.getCurrentUser().setPassword(sharedPreferences.getString(s, "password"));
                    break;
                case "email":
                    ParseUser.getCurrentUser().setEmail(sharedPreferences.getString(s, "email"));
                    break;
                default:
                    Toast.makeText(getContext(), "No Match in Settings", Toast.LENGTH_SHORT).show();
            }

            ParseUser.getCurrentUser().saveInBackground();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }


    public static void save(String valueKey, String value, Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(valueKey, value);
        edit.commit();
    }

}
