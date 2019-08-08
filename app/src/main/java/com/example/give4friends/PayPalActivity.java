package com.example.give4friends;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.give4friends.models.Milestone;

public class PayPalActivity extends AppCompatActivity {
    WebView webView;
    String charityCode;
    ImageButton cancel;
    Double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);

        cancel = findViewById(R.id.ibcancelFinal2);
        amount = getIntent().getDoubleExtra("amount", 0);
        charityCode = getIntent().getStringExtra("code");
        String url = "https://www.paypal.com/fundraiser/charity/" + charityCode;

        webView = (WebView) findViewById(R.id.wvPayPal);

        webView.getSettings().setDomStorageEnabled(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webSettings.setUserAgentString("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");

        //TODO might actually check user but unsure if it has been updated
        Milestone.addMilestone("Donated $50", PayPalActivity.this);
        Milestone.addMilestone("Donated $20", PayPalActivity.this);
        Milestone.addMilestone("First Donation", PayPalActivity.this );

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:(function() { document.getElementById('nemo_inputAmount').value = '"+amount.toString()+"';})()");
            }
        });

        webView.canGoBack();
        webView.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP
                        && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
                return false;
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PayPalActivity.this, Main_Fragment_Branch.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

            }
        });

    }

}


