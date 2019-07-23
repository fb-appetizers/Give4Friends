package com.example.give4friends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.Transaction;
import com.example.give4friends.models.User;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import static com.example.give4friends.DonateActivity.charity;
import static com.example.give4friends.DonateActivity.friend;

public class DonateFinalActivity extends AppCompatActivity {
    private ParseUser ff;
    private TextView friendd;
    private EditText amount;
    private TextView amountEntered;
    private EditText message;
    private TextView charityName;
    private Button submitDonation;
    private ImageButton cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_final);

        friendd = findViewById(R.id.friendPost);
        amount = findViewById(R.id.amount);
        amountEntered = findViewById(R.id.amountEntered);
        message = findViewById(R.id.donationMessage);
        charityName = findViewById(R.id.charityName);
        submitDonation = findViewById(R.id.donateSubmitBtn);
        cancelBtn = findViewById(R.id.ibcancelFinal);

        amountEntered.setVisibility(View.GONE);

        friendd.setText(friend.getUsername());
//        charityName.setText(charity.getKeyName());

        amount.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent == null || !keyEvent.isShiftPressed()) {
                    // the user is done typing.
                    amountEntered.setVisibility(View.VISIBLE);
                    amountEntered.setText("$" + amount.getText().toString());
                    amount.setVisibility(View.GONE);

                    return true; // consume.
                }
                return false;
            }
        });

        submitDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewTransaction();
                Intent intent = new Intent(DonateFinalActivity.this, MainActivity.class);
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

        newTransaction.setKeyMessage(message.getText().toString());
        newTransaction.setKeyFriendId(friend);
        newTransaction.setKeyDonorId(currentUser);
        newTransaction.setKeyCharityId(charity);
        newTransaction.setKeyAmountDonated(Integer.parseInt(amount.getText().toString()));

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
    }
}
