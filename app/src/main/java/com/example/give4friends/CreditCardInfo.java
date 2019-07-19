package com.example.give4friends;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.parse.ParseUser.getCurrentUser;

public class CreditCardInfo extends AppCompatActivity {
    private CardForm cardForm;
    private Button submit;
    AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_info);

        cardForm = findViewById(R.id.card_form);
        submit = findViewById(R.id.submitBtn);
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

                            Intent intent = new Intent(CreditCardInfo.this, MainActivity.class);
                            startActivity(intent);
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
    }

    public void creditCardInfo(String cardNum, String expDate, String cvv, String zip, String phoneNum){
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        Date date = null;
//        try {
//            date = format.parse(expDate);
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        FinancialInfo newUserCC = new FinancialInfo();
//
//        newUserCC.setKeyCreditCardNum(Integer.parseInt(cardNum));
//        newUserCC.setKeyCvc(Integer.parseInt(cvv));
//        newUserCC.setKeyEmail(getCurrentUser().getEmail());
//        newUserCC.setKeyExpDate(date);
//        newUserCC.setKeyZipcode(Integer.parseInt(zip));
//        newUserCC.setKeyPhoneNum(Integer.parseInt(phoneNum));

        ParseObject parseObject = ParseObject.create("financialInfo");
        parseObject.add("credit_card_num", cardNum);
        parseObject.add("expiration_date", expDate);
        parseObject.add("cvc", Integer.parseInt(cvv));
        parseObject.add("zipCode", Integer.parseInt(zip));
//        parseObject.add("email", getCurrentUser().getEmail());
        parseObject.add("phoneNumber", phoneNum);

        parseObject.saveInBackground();
//
//        getCurrentUser().add("financialInfo", parseObject);
    }
}
