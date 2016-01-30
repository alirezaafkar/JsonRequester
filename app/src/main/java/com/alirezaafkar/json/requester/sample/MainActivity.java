package com.alirezaafkar.json.requester.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.json.requester.interfaces.Methods;
import com.alirezaafkar.json.requester.interfaces.Response;
import com.alirezaafkar.json.requester.requesters.JsonArrayRequester;
import com.alirezaafkar.json.requester.requesters.JsonObjectRequester;
import com.alirezaafkar.json.requester.requesters.RequestBuilder;
import com.alirezaafkar.json.requester.sample.items.ErrorItem;
import com.alirezaafkar.json.requester.sample.items.GistItem;
import com.alirezaafkar.json.requester.sample.items.UserItem;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.alirezaafkar.json.requester.sample.items.GistItem.GistEntity;


/**
 * Created by Alireza Afkar on 12/12/15 AD.
 */
public class MainActivity extends AppCompatActivity {
    private static final int USER_REQUEST = 0;
    private static final int FOLLOWERS_REQUEST = 1;

    private JsonArrayRequester mGistsRequester;
    private JsonObjectRequester mUserRequester;
    private JsonObjectRequester mFollowersRequester;

    private TextView mTextView;
    private ProgressBar mProgressBar;

    private String mGistsAPi = "https://api.github.com/gists/public";
    private String mUserAPi = "https://api.github.com/users/alirezaafkar";
    private String mFollowersAPi = "https://api.github.com/user/followers";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        JsonObjectListener objectListener = new JsonObjectListener();

        mUserRequester = new RequestBuilder(this)
                .requestCode(USER_REQUEST)
                .showError(true)
                .buildObjectRequester(objectListener);

        mFollowersRequester = new RequestBuilder(this)
                .requestCode(FOLLOWERS_REQUEST)
                .showError(true)
                .buildObjectRequester(objectListener);

        mGistsRequester = new RequestBuilder(this)
                .showError(true)
                .buildArrayRequester(new JsonArrayListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_action:
                mUserRequester.request(Methods.GET, mUserAPi);
                break;
            case R.id.gists_action:
                mGistsRequester.request(Methods.GET, mGistsAPi);
                break;
            case R.id.followers_action:
                mFollowersRequester.request(Methods.GET, mFollowersAPi);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            mUserRequester.setCallback(null);
            mGistsRequester.setCallback(null);
            mFollowersRequester.setCallback(null);
        }
    }

    private class JsonObjectListener extends Response.SimpleObjectResponse {
        @Override
        public void onRequestStart(int requestCode) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            if (jsonObject == null) return;
            UserItem userItem = UserItem.newInstance(jsonObject);
            UserFragment.newInstance(userItem)
                    .show(getSupportFragmentManager(), null);
        }

        @Override
        public void onErrorResponse(int requestCode, VolleyError volleyError, @Nullable JSONObject errorObject) {
            if (requestCode == FOLLOWERS_REQUEST && errorObject != null) {
                ErrorItem errorItem = ErrorItem.newInstance(errorObject);
                Toast.makeText(MainActivity.this,
                        errorItem.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onRequestFinish(int requestCode) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private class JsonArrayListener extends Response.SimpleArrayResponse {
        @Override
        public void onRequestStart(int requestCode) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onResponse(int requestCode, @Nullable JSONArray jsonArray) {
            GistItem gists = new GistItem(jsonArray);
            StringBuilder stringBuilder = new StringBuilder();

            for (GistEntity feed : gists.getItems()) {
                stringBuilder.append(feed.getUrl());
                stringBuilder.append("\n");
            }

            mTextView.setText(stringBuilder.toString());
        }

        @Override
        public void onRequestFinish(int requestCode) {
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
