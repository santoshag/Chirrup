package com.codepath.apps.chirrup.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.chirrup.R;
import com.codepath.apps.chirrup.TwitterApplication;
import com.codepath.apps.chirrup.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class NewTweetActivity extends AppCompatActivity {
    TwitterClient client;
    EditText etTweetText;
//    TextView tvCharCount;
    public static int TWEET_CHAR_LIMIT = 140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_tweet);
        //get singleton rest client
        client = TwitterApplication.getRestClient();
        etTweetText = (EditText) findViewById(R.id.etTweetText);
        etTweetText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                TextView tvCharCount = (TextView) findViewById(R.id.tvCharCount);

                tvCharCount.setText(String.valueOf(TWEET_CHAR_LIMIT - s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void postTweet(View view) {
        client.composeTweet(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", "onsuccess" + response.toString());
             /*   Intent i = new Intent(getApplicationContext(), TimelineActivity.class);
                i.putExtra("newTweet", Boolean.TRUE);
                startActivity(i);*/
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", responseString);
            }
        }, etTweetText.getText().toString());
    }
}


