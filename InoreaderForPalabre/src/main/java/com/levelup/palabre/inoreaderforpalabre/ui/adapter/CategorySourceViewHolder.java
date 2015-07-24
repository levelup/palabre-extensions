package com.levelup.palabre.inoreaderforpalabre.ui.adapter;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.levelup.palabre.inoreaderforpalabre.R;
import com.squareup.picasso.Picasso;

/**
 * Simple view holder for a single text view.
 */
public class CategorySourceViewHolder extends RecyclerView.ViewHolder {

    private final View parent;
    private final RecyclerView.Adapter adapter;
    private final ItemTouchHelper touchListener;
    private TextView textView;
    private ImageView imageView;
    private ImageView dragView;

    CategorySourceViewHolder(View view, RecyclerView.Adapter adapter, ItemTouchHelper touchListener) {
        super(view);

        textView = (TextView) view.findViewById(R.id.text);
        imageView = (ImageView) view.findViewById(R.id.image);
        dragView= (ImageView) view.findViewById(R.id.drag_view);
        parent = view;
        this.adapter = adapter;
        this.touchListener = touchListener;
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

        if (dragView!= null) {
            dragView.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        touchListener.startDrag(CategorySourceViewHolder.this);
                    }
                    return false;
                }
            });
        }

    }

    @Override
    public String toString() {
        return textView.getText().toString();
    }

}
