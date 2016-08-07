package com.codepath.apps.chirrup.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by santoshag on 8/5/16.
 */
public class User {
    private Long id;
    private String screenName;
    private String name;
    private String profileImageUrl;

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public Long getId() {
        return id;
    }

    public static User fromJson(JSONObject jsonObject){
        User user = new User();
        try {
            user.id = jsonObject.getLong("id");
            user.name = jsonObject.getString("name");
            user.screenName = "@" + jsonObject.getString("screen_name");
            user.profileImageUrl = getOriginalImage(jsonObject.getString("profile_image_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static String getOriginalImage(String imageUrl){
        return imageUrl.replace("_normal", "");
    }
}
