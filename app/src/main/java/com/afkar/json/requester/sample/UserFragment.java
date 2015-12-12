package com.afkar.json.requester.sample;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.afkar.json.requester.sample.items.UserItem;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

/**
 * Created by Alireza Afkar on 12/12/15 AD.
 */
public class UserFragment extends DialogFragment implements View.OnClickListener {
    private static final String USER = "USER";

    private UserItem mUser;

    public static UserFragment newInstance(UserItem user) {
        Bundle args = new Bundle();
        args.putString(USER, new Gson().toJson(user));
        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog == null) return;
        Window window = dialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        param.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        window.setAttributes(param);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setWindowAnimations(R.style.DialogAnimation);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        String user = getArguments().getString(USER);
        mUser = new Gson().fromJson(user, UserItem.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user,
                container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView image = (ImageView) view.findViewById(R.id.image);
        ((TextView) view.findViewById(R.id.name)).setText(mUser.getName());
        view.findViewById(R.id.view).setOnClickListener(this);

        Glide.with(this)
                .load(mUser.getAvatar_url())
                .into(image);
    }

    @Override
    public void onClick(View view) {
        if (mUser.getHtml_url() == null) return;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mUser.getHtml_url()));
            startActivity(intent);
        } catch (ActivityNotFoundException ignored) {

        }
    }
}
