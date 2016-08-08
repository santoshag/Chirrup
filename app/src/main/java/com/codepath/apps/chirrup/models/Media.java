package com.codepath.apps.chirrup.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by santoshag on 8/7/16.
 */
public class Media {

    String mediaUrl;

    public static Media fromJSON(JSONObject jsonObject) {
        Media media = new Media();
        try {

            media.mediaUrl = jsonObject.getString("media_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return media;
    }

    public static String getMediaUrl(JSONArray jsonArray) {

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).has("media_url")) {
                    String mediaUrl = jsonArray.getJSONObject(i).get("media_url").toString();

                    if (mediaUrl.equals("")) {
                        continue;
                    } else {
                        return mediaUrl;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }


    public static ArrayList<Media> fromJSON(JSONArray jsonArray) {
        ArrayList<Media> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Media media = fromJSON(jsonArray.getJSONObject(i));
                result.add(media);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
