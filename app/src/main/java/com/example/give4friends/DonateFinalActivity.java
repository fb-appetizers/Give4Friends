package com.example.give4friends;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.give4friends.models.Milestone;
import com.example.give4friends.models.Transaction;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.text.NumberFormat;
import java.util.Locale;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        charityName = findViewById(R.id.tvCommentsCharityName);

        amount = findViewById(R.id.amount);
        message = findViewById(R.id.etcommentMessage);
        charityName = findViewById(R.id.tvCommentsCharityName);
        submitDonation = findViewById(R.id.donateSubmitBtn);
        cancelBtn = findViewById(R.id.ibcancelFinal);

        String imageURL = currentFriend.getString("profileImageURL");

        if(imageURL != null){
            Date imageDate = currentFriend.getDate("profileImageCreatedAt");
            Glide.with(this)
                    .load(imageURL)
                    .apply(new RequestOptions()
                            .transforms(new CenterCrop(), new RoundedCorners(20))
                            .circleCrop()
                            .signature(new ObjectKey(imageDate)))
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


        amount.addTextChangedListener(new TextWatcher() {

            String previous;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                previous = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (count == 0){
                    amount.setText("$");
                    amount.setSelection(amount.getText().length());

                    submitDonation.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    submitDonation.setTextColor(getResources().getColor(R.color.colorPrimary));
                }

                if (count == 1){
                    amount.setSelection(amount.getText().length());
                }

                String temp = amount.getText().subSequence(1,amount.getText().length()).toString();

                if(temp.contains("$")){
                    amount.setSelection(amount.getText().length());
                    amount.setText(temp);
                }

                if(!temp.matches("^[$0-9]*+([.][0-9]{0,2})?$")) {
                    //[$0-9.]*
                    amount.setText(previous);
                    amount.setSelection(amount.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(amount.getText().subSequence(1, amount.getText().length()).length() > 0 &&
                        Integer.parseInt(amount.getText().subSequence(1, amount.getText().length()).toString()) > 0){
                    submitDonation.setBackgroundColor(getResources().getColor(R.color.colorNow));
                    submitDonation.setTextColor(getResources().getColor(R.color.colorWhite));
                }
            }
        });

        submitDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setNewTransaction();

                String code = currentCharity.getKeyCode();
                if(code == null){
                    Intent intent = new Intent(DonateFinalActivity.this, Main_Fragment_Branch.class);
                    startActivity(intent);
                    finish();
                }

                String num_string = amount.getText().toString();
                Double number;
                if(num_string.equals("")){
                    number = 0.0;
                }else if(num_string.equals("$")){
                    number = 0.0;
                }else{
                    number = Double.parseDouble(amount.getText().toString().substring(1));
                }


                // query for code from charity when done do this
                Intent intent = new Intent(DonateFinalActivity.this, PayPalActivity.class);
                intent.putExtra("code", code);
                intent.putExtra("amount", number);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                finish();
            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    private void setNewTransaction(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        Transaction newTransaction = new Transaction();
        String amountHere = amount.getText().toString();

        int amountInt = 0;
        if(amountHere.length() <= 1){
            amountHere = "$0";
        }else{
            amountInt = Integer.parseInt(amountHere.substring(1));
        }


        newTransaction.setKeyMessage(message.getText().toString());
        newTransaction.setKeyFriendId(currentFriend);
        newTransaction.setKeyDonorId(currentUser);
        newTransaction.setKeyCharityId(currentCharity);
        newTransaction.setKeyAmountDonated(amountInt);


        String amountEntered = amountHere;
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
