package com.codepath.apps.chirrup.models;

import android.text.format.DateUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by santoshag on 8/5/16.
 */

@Table(name = "Tweets")
public class Tweet extends Model{

    @Column(name = "remote_id", unique = true)
    private long remoteId;
    @Column(name = "body")
    private String body;
    @Column(name = "user")//, onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;
    @Column(name = "created_at")
    private String createdAt;
    @Column(name = "relative_date")
    private String relativeDate;


    // Make sure to have a default constructor for every ActiveAndroid model
    public Tweet(){
        super();
    }


    public String getRelativeDate() {
        return relativeDate;
    }
    public User getUser() {
        return user;
    }

    //for getting tweets in batches for endless scrolling
    public static Long lastTweetId;

    public long getRemoteId() {
        return remoteId;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public static Tweet fromJson(JSONObject jsonObject){
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.remoteId = jsonObject.getLong("id");
            User user = User.findOrCreateFromJson(jsonObject.getJSONObject("user"));
            tweet.user = user;
            user.save();
            tweet.relativeDate = getRelativeTimeAgo(jsonObject.getString("created_at"));
            tweet.save();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray){

        ArrayList<Tweet> result = new ArrayList<>();
        for(int i=0; i< jsonArray.length(); i++){
            try {
                Tweet tweet = fromJson(jsonArray.getJSONObject(i));
                result.add(tweet);
                lastTweetId = tweet.getRemoteId() - 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
