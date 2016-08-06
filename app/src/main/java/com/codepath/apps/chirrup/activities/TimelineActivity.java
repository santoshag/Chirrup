package com.codepath.apps.chirrup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.codepath.apps.chirrup.R;
import com.codepath.apps.chirrup.TwitterApplication;
import com.codepath.apps.chirrup.TwitterClient;
import com.codepath.apps.chirrup.adapters.TweetsAdapter;
import com.codepath.apps.chirrup.models.Tweet;
import com.codepath.apps.chirrup.utils.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient client;
    private TweetsAdapter tweetsAdapter;
    private RecyclerView rvTweets;
    private ArrayList<Tweet> tweetList;
    FloatingActionButton myFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        //get singleton rest client
        client = TwitterApplication.getRestClient();

        myFab = (FloatingActionButton) findViewById(R.id.fabCompose);
        rvTweets = (RecyclerView) findViewById(R.id.rvTweets);
        tweetList = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(this, tweetList);
        rvTweets.setAdapter(tweetsAdapter);
        // Setup layout manager for items with orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // Optionally customize the position you want to default scroll to
        layoutManager.scrollToPosition(0);
        rvTweets.setLayoutManager(layoutManager);

        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.i("DEBUG", "totalItemsCount: " + totalItemsCount);
                //limit totalItemsCount to void "Rate limit exceeded" error
                if(totalItemsCount < 60) {
                    populateMoreTimeline(page);
                }
            }
        });


        populateTimeline("since_id", (long)1);
    }

    public void composeNewTweet(View view){
        Intent i = new Intent(this, NewTweetActivity.class);
        startActivity(i);

    }


    private void populateMoreTimeline(int page){
        Log.i("max_id is", Tweet.lastTweetId.toString());
        if(Tweet.lastTweetId != null) {
            populateTimeline("max_id", Tweet.lastTweetId);
        }
    }

    //send API request to get tweets and add it to listview
    private void populateTimeline(String sinceOrMaxId, long count){

        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                Log.d("DEBUG", response.toString());
                tweetList.addAll(Tweet.fromJsonArray(response));
                tweetsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", "onFailure" + errorResponse.toString());

            }
        }, sinceOrMaxId, count);
    }

}
