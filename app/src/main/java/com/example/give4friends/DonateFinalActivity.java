package com.example.give4friends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
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
import com.example.give4friends.R;
import com.example.give4friends.models.Charity;
import com.example.give4friends.models.Transaction;
import com.example.give4friends.models.User;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import static com.example.give4friends.DonateActivity.charityName2;
import static com.example.give4friends.DonateActivity.currentCharity;
import static com.example.give4friends.DonateActivity.currentFriend;

public class DonateFinalActivity extends AppCompatActivity {
    private ParseUser ff;
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
        amountEntered = findViewById(R.id.amountEntered);
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

        Intent intent = getIntent();

        charityName.setText(charityName2);

        friendsName.setText(currentFriend.get("firstName") + " " + currentFriend.get("lastName"));
        friendsUserName.setText(currentFriend.getUsername());

        amountEntered.setVisibility(View.GONE);

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
        newTransaction.setKeyFriendId(currentFriend);
        newTransaction.setKeyDonorId(currentUser);
        newTransaction.setKeyCharityId(currentCharity);
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
