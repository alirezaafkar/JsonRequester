package com.afkar.json.requester.sample.items;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Alireza Afkar on 12/11/15 AD.
 */
public class GistItem {
    ArrayList<GistEntity> mItems = new ArrayList<>();

    public GistItem(JSONArray jsonArray) {
        GistEntity[] feeds = new Gson().fromJson(jsonArray.toString(),
                GistEntity[].class);
        Collections.addAll(mItems, feeds);
    }

    public ArrayList<GistEntity> getItems() {
        return mItems;
    }

    public void setItems(ArrayList<GistEntity> items) {
        mItems = items;
    }

    public static class GistEntity {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
