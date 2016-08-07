package com.codepath.apps.chirrup.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.codepath.apps.chirrup.R;
import com.codepath.apps.chirrup.TwitterApplication;
import com.codepath.apps.chirrup.TwitterClient;
import com.codepath.apps.chirrup.adapters.TweetsAdapter;
import com.codepath.apps.chirrup.decorators.ItemClickSupport;
import com.codepath.apps.chirrup.models.Tweet;
import com.codepath.apps.chirrup.models.User;
import com.codepath.apps.chirrup.utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.chirrup.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient client;
    private TweetsAdapter tweetsAdapter;
    private RecyclerView rvTweets;
    private ArrayList<Tweet> tweetList;
    FloatingActionButton myFab;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        //get singleton rest client
        client = TwitterApplication.getRestClient();
// Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        myFab = (FloatingActionButton) findViewById(R.id.fabCompose);
        rvTweets = (RecyclerView) findViewById(R.id.rvTweets);
        tweetList = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(this, tweetList);
        rvTweets.setAdapter(tweetsAdapter);
        // Setup layout manager for items with orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // Optionally customize the position you want to default scroll to
        layoutManager.scrollToPosition(0);

        ItemClickSupport.addTo(rvTweets).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Tweet tweet = tweetList.get(position);
                        Intent intent = new Intent(TimelineActivity.this, DetailActivity.class);
                        //intent.putExtra("article", Parcels.wrap(article));
                        startActivity(intent);
                    }
                }
        );
        rvTweets.setLayoutManager(layoutManager);

        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.i("DEBUG", "totalItemsCount: " + totalItemsCount);
                //limit totalItemsCount to void "Rate limit exceeded" error
                if(totalItemsCount < 60) {
                    populateMoreTimeline();
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if(!Utils.checkForInternet()){
            Toast.makeText(this, "Internet is not connected", Toast.LENGTH_SHORT).show();
// Query ActiveAndroid for list of todo items currenty sorted by priority

            swipeContainer.setEnabled(false);
            List<Tweet> queryResults = new Select().from(Tweet.class)
                    .orderBy("remote_id DESC").execute();
            // Load the result into the adapter using `addAll`
            Log.i("sql", "loading data from offline: " + queryResults.size() + " " + queryResults.get(1).getUser().getProfileImageUrl());
            tweetList.clear();
            tweetList.addAll(queryResults);
            tweetsAdapter.notifyDataSetChanged();

        }else{
            //get timeline here
            populateTimeline("since_id", (long)1);
            //setup swipe to refresh
            setupSwipeToRefreshView();
        }
    }
    // pass context to Calligraphy
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }

    private void setupSwipeToRefreshView() {
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimeline("since_id", (long)1);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void composeNewTweet(View view){
        Intent i = new Intent(this, NewTweetActivity.class);
        startActivity(i);

    }


    private void populateMoreTimeline(){
        Log.i("max_id is", Tweet.lastTweetId.toString());
        if(Tweet.lastTweetId != null) {
            populateTimeline("max_id", Tweet.lastTweetId);
        }
    }

    //send API request to get tweets and add it to listview
    private void populateTimeline(final String sinceOrMaxId, long count){

        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                Boolean clearOfflineTweets = Boolean.FALSE;
                if(sinceOrMaxId.equals("since_id")){
                    tweetList.clear();
                    new Delete().from(Tweet.class).execute(); // all records
                    new Delete().from(User.class).execute(); // all records
                }
                tweetList.addAll(Tweet.fromJsonArray(response));
                tweetsAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", "onFailure" + errorResponse.toString());
                swipeContainer.setRefreshing(false);
            }
        }, sinceOrMaxId, count);
    }
    

}
