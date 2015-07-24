package com.levelup.palabre.flickrforpalabre;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Simple view holder for a single text view.
 */
public class CategorySourceViewHolder extends RecyclerView.ViewHolder {

    private final View parent;
    private final RecyclerView.Adapter adapter;
    private TextView textView;
    private ImageView imageView;

    CategorySourceViewHolder(View view, RecyclerView.Adapter adapter) {
        super(view);

        textView = (TextView) view.findViewById(R.id.text);
        imageView = (ImageView) view.findViewById(R.id.image);
        parent = view;
        this.adapter = adapter;
    }

    public void bindItem(Context context, String text, String image) {
        textView.setText(text);
        if (!TextUtils.isEmpty(image)) {
            Picasso
                    .with(context)
                    .load(image)
//                    .centerCrop()
                    .into(imageView);
        }


    }

    @Override
    public String toString() {
        return textView.getText().toString();
    }

}
