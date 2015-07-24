package com.levelup.theoldreaderforpalabre.ui;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.levelup.theoldreaderforpalabre.R;

/**
 * Created by ludo on 09/06/15.
 */
public class SourceViewHolder  extends RecyclerView.ViewHolder {

    private final RecyclerView.Adapter adapter;
    private final ItemTouchHelper touchListener;
    private final View mDragView;
    private TextView mTextView;

    public SourceViewHolder(View itemView, RecyclerView.Adapter adapter, ItemTouchHelper touchListener) {
        super(itemView);

        mTextView = (TextView) itemView.findViewById(R.id.text);
        mDragView = itemView.findViewById(R.id.drag_view);

        this.adapter = adapter;
        this.touchListener = touchListener;

    }

    public void bindItem(String text) {
        mTextView.setText(text);

        if (mDragView!= null) {
            mDragView.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        touchListener.startDrag(SourceViewHolder.this);
                    }
                    return false;
                }
            });
        }
    }
}
