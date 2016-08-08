package com.codepath.apps.chirrup.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by santoshag on 8/7/16.
 */
@Table(name = "Entities")
public class Entity extends Model {

    public String getMediaUrl() {
        return media_url;
    }

    @Column(name = "media")
    private String media_url = "";

    // Make sure to have a default constructor for every ActiveAndroid model
    public Entity(){
        super();
    }

    public static Entity fromJSON(JSONObject jsonObject){
        Entity entity = new Entity();
        try {
            entity.media_url = Media.getMediaUrl(jsonObject.getJSONArray("media"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return entity;
    }
}
