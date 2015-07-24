package com.levelup.palabre.flickrforpalabre;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.levelup.palabre.flickrforpalabre.flickr.utils.SharedPreferenceKeys;
import com.squareup.picasso.Picasso;

/**
 * Simple view holder for a single text view.
 */
public class HeaderViewHolder extends RecyclerView.ViewHolder {

    private final View parent;
    private final CategorySourceAdapter.OnLoginClickListener listener;
    private final TextView realnameView;
    private TextView textView;
    private ImageView imageView;

    HeaderViewHolder(View view, final CategorySourceAdapter.OnLoginClickListener listener) {
        super(view);

        textView = (TextView) view.findViewById(R.id.login);
        realnameView = (TextView) view.findViewById(R.id.realname);
        imageView = (ImageView) view.findViewById(R.id.user_avatar);
        parent = view;
        this.listener = listener;




    }

    public void bindItem(Context context) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String login = sharedPreferences.getString(SharedPreferenceKeys.LOGIN_USERNAME, "");
        if (!TextUtils.isEmpty(login)) {
            textView.setText(login);
        } else {
            textView.setText(context.getString(R.string.login));
        }

        String realName = sharedPreferences.getString(SharedPreferenceKeys.LOGIN_REAL_NAME, "");
        if (!TextUtils.isEmpty(realName)) {
            realnameView.setText(realName);
            realnameView.setVisibility(View.VISIBLE);
        } else {
            realnameView.setVisibility(View.GONE);
        }

        String avatar = sharedPreferences.getString(SharedPreferenceKeys.LOGIN_AVATAR, "");

        if (!TextUtils.isEmpty(avatar)) {

            Picasso.with(context).load(avatar).into(imageView);
        } else {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_account));
        }

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!= null) {
                    listener.onLoginClicked();
                }
            }
        });


    }

    @Override
    public String toString() {
        return textView.getText().toString();
    }

}
