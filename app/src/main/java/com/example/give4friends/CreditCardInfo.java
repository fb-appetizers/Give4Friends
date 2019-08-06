package com.example.give4friends;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import static com.parse.ParseUser.getCurrentUser;

public class CreditCardInfo extends AppCompatActivity {
    private CardForm cardForm;
    private Button submit;
    private Button skip;
    AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_info);

        cardForm = findViewById(R.id.card_form);
        submit = findViewById(R.id.submitBtn);
        skip = findViewById(R.id.skipBtn);

        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .setup(CreditCardInfo.this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardForm.isValid()) {
                    final String cardNum = cardForm.getCardNumber();
                    final String expDate = cardForm.getExpirationDateEditText().getText().toString();
                    final String cvv = cardForm.getCvv();
                    final String zip = cardForm.getPostalCode();
                    final String phoneNum = cardForm.getMobileNumber();
                    alertBuilder = new AlertDialog.Builder(CreditCardInfo.this);
                    alertBuilder.setTitle("Confirm before submitting");
                    alertBuilder.setMessage("Card number: " + cardNum + "\n" +
                            "Card expiry date: " + expDate + "\n" +
                            "Card CVV: " + cvv + "\n" +
                            "Postal code: " + zip + "\n" +
                            "Phone number: " + phoneNum);
                    alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            creditCardInfo(cardNum, expDate, cvv, zip, phoneNum);
                            dialogInterface.dismiss();
                            Toast.makeText(CreditCardInfo.this, "Saved", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(CreditCardInfo.this, Main_Fragment_Branch.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                        }
                    });
                    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();

                }else {
                    Toast.makeText(CreditCardInfo.this, "Please complete the form", Toast.LENGTH_LONG).show();
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreditCardInfo.this, Main_Fragment_Branch.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    public void creditCardInfo(String cardNum, String expDate, String cvv, String zip, String phoneNum){
        final ParseObject parseObject = new ParseObject("financialInfo");

        parseObject.put("credit_card_num", Integer.parseInt(cardNum));
        parseObject.put("expDate", Integer.parseInt(expDate));
        parseObject.put("cvc", Integer.parseInt(cvv));
        parseObject.put("zipCode", Integer.parseInt(zip));
        parseObject.put("email", getCurrentUser().getEmail());
        parseObject.put("phoneNumber", Integer.parseInt(phoneNum));

        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    getCurrentUser().put("financialInfo", parseObject);
                    getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Log.d("CreditCardInfo", "Credit Card Info Updated");
                        }
                    });
                }
                else{
                    Log.d("CreditCardInfo", "Unable to update Credit Card Info");
                    e.printStackTrace();
                }
            }
        });
    }
}
