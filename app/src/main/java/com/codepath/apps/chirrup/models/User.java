package com.codepath.apps.chirrup.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by santoshag on 8/5/16.
 */
@Table(name = "Users")
public class User extends Model{
    @Column(name = "remote_id", unique = true)
    private Long remoteId;
    @Column(name = "screen_name")
    private String screenName;
    @Column(name = "name")
    private String name;
    @Column(name = "profile_image_url")
    private String profileImageUrl;


    // Make sure to have a default constructor for every ActiveAndroid model
    public User(){
        super();
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public Long getRemoteId() {
        return remoteId;
    }

    public static User findOrCreateFromJson(JSONObject json) {
        long rId = 0; // get just the remote id
        try {
            rId = json.getLong("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        User existingUser =
                new Select().from(User.class).where("remote_id = ?", rId).executeSingle();
        if (existingUser != null) {
            // found and return existing
            return existingUser;
        } else {
            // create and return new user
            User user = fromJSON(json);
            user.save();
            return user;
        }
    }

    public static User fromJSON(JSONObject jsonObject){
        User user = new User();
        try {
            user.remoteId = jsonObject.getLong("id");
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
