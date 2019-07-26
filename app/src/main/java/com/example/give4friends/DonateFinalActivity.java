package com.example.give4friends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.give4friends.models.Transaction;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;

import static com.example.give4friends.DonateActivity.charityName2;
import static com.example.give4friends.DonateActivity.currentCharity;
import static com.example.give4friends.DonateActivity.currentFriend;

public class DonateFinalActivity extends AppCompatActivity {
    private EditText amount;
    private TextView amountEntered;
    private EditText message;
    private Button submitDonation;
    private ImageButton cancelBtn;

    private ImageView friendsImage;
    private TextView friendsName;
    private TextView friendsUserName;
    private TextView charityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_final);

        friendsName = findViewById(R.id.friendsName);
        friendsUserName = findViewById(R.id.friendsUserName);
        friendsImage = findViewById(R.id.friendsImage);
        charityName = findViewById(R.id.charityName);

        amount = findViewById(R.id.amount);
        //amountEntered = findViewById(R.id.amountEntered);
        message = findViewById(R.id.donationMessage);
        charityName = findViewById(R.id.charityName);
        submitDonation = findViewById(R.id.donateSubmitBtn);
        cancelBtn = findViewById(R.id.ibcancelFinal);

        ParseFile profilePic = currentFriend.getParseFile("profileImage");

        if(profilePic != null){
            Glide.with(this)
                    .load(profilePic.getUrl())
                    .apply(new RequestOptions()
                            .transforms(new CenterCrop(), new RoundedCorners(20))
                            .circleCrop())
                    .into(friendsImage);
        }

        charityName.setText(charityName2);

        friendsName.setText(currentFriend.get("firstName") + " " + currentFriend.get("lastName"));
        friendsUserName.setText("@" + currentFriend.getUsername());

        amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    amount.setText("$");
                    amount.setSelection(0);
                }
            }
        });

        submitDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewTransaction();
                Intent intent = new Intent(DonateFinalActivity.this, Main_Fragment_Branch.class);
                startActivity(intent);
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setNewTransaction(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        Transaction newTransaction = new Transaction();
        String amountHere = amount.getText().toString();

        for(int i = 0; i < amountHere.length(); i++){
            if(amountHere.charAt(i) == '$'){

            }
        }
        int amountInt = Integer.parseInt(amount.getText().toString().substring(1));

        newTransaction.setKeyMessage(message.getText().toString());
        newTransaction.setKeyFriendId(currentFriend);
        newTransaction.setKeyDonorId(currentUser);
        newTransaction.setKeyCharityId(currentCharity);
        newTransaction.setKeyAmountDonated(amountInt);


        String amountEntered = amount.getText().toString();
        newTransaction.setKeyAmountDonated(Integer.parseInt(amountEntered.substring(1)));

        newTransaction.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Log.d("DonateFinalActivity", "Created new transaction");
                }
                else{
                    Log.d("DonateFinalActivity", "Invalid transaction");

                    e.printStackTrace();
                }
            }
        });

        //update user totals
        currentUser.increment("totalDonated", amountInt);
        currentUser.saveInBackground();
        //update relation
        
        ParseRelation<ParseObject> relation = ParseUser.getCurrentUser().getRelation("friendsRecent");
        relation.add(currentFriend);
        ParseUser.getCurrentUser().saveInBackground();
    }
}
