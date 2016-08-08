package com.codepath.apps.chirrup.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.chirrup.R;
import com.codepath.apps.chirrup.TwitterApplication;
import com.codepath.apps.chirrup.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by santoshag on 7/31/16.
 */
public class NewTweetFragment extends BottomSheetDialogFragment{

    TwitterClient client;
    EditText etTweetText;
    Button btnPostTweet;
    //    TextView tvCharCount;
    public static int TWEET_CHAR_LIMIT = 140;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_tweet, container);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get singleton rest client
        client = TwitterApplication.getRestClient();
        etTweetText = (EditText) view.findViewById(R.id.etTweetText);
        btnPostTweet = (Button) view.findViewById(R.id.btnPost);

        btnPostTweet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                client.composeTweet(new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("DEBUG", "onsuccess" + response.toString());

                        dismiss();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                        dismiss();
                    }
                }, etTweetText.getText().toString());
            }


        });

        etTweetText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                TextView tvCharCount = (TextView) view.findViewById(R.id.tvCharCount);
                tvCharCount.setText(String.valueOf(TWEET_CHAR_LIMIT - s.length()));
                if(s.length() > 0 && s.length() <= TWEET_CHAR_LIMIT){
                    btnPostTweet.setEnabled(true);
                    btnPostTweet.setBackground(getContext().getResources().getDrawable(R.drawable.button_on));
                }else{
                    btnPostTweet.setEnabled(false);
                    btnPostTweet.setBackground(getContext().getResources().getDrawable(R.drawable.button_off));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}