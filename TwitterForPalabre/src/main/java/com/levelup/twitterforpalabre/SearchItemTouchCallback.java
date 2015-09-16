package com.levelup.twitterforpalabre;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.levelup.palabre.api.datamapping.Category;


/**
 * Created by nicolas on 11/06/15.
 */
public class SearchItemTouchCallback extends ItemTouchHelper.SimpleCallback {
    private static final String TAG = SearchItemTouchCallback.class.getSimpleName();
    private SearchAdapter adapter;
    private final Context context;
    private final RecyclerView recyclerView;
    private Handler actionHandler = new Handler(Looper.getMainLooper());
    private String lastCategoryId;
    private Snackbar snack;

    final View.OnAttachStateChangeListener listener = new View.OnAttachStateChangeListener() {
        @Override
        public void onViewAttachedToWindow(View v) {
        }

        @Override
        public void onViewDetachedFromWindow(View v) {

            performDismissAction();
            snack.getView().removeOnAttachStateChangeListener(this);
        }
    };

    public SearchItemTouchCallback(Context context, int dragDirs, int swipeDirs, RecyclerView recyclerView) {
        super(dragDirs, swipeDirs);
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

        return false;
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {

        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

//                pendingViewHolder = new viewHolder;


        final int adapterPosition = viewHolder.getAdapterPosition();


        if (!TextUtils.isEmpty(lastCategoryId)) {
            performDismissAction();
            snack.getView().removeOnAttachStateChangeListener(listener);
        }

        lastCategoryId = adapter.getItem(adapterPosition);


        snack = Snackbar.make(recyclerView, R.string.search_removed, Snackbar.LENGTH_LONG).setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoAction();
            }
        });
                snack.show();


        snack.getView().addOnAttachStateChangeListener(listener);



        adapter.dismissItem(viewHolder);
        //Remove swiped item from list and notify the RecyclerView
    }

    private void undoAction() {
        lastCategoryId = "";
        adapter.reloadData(context);
        actionHandler.removeCallbacksAndMessages(null);
    }

    private void performDismissAction() {
        if (!TextUtils.isEmpty(lastCategoryId)) {
            if (BuildConfig.DEBUG) Log.d(TAG, "Deleting: "+lastCategoryId);
            final Category category = Category.getByUniqueId(context, lastCategoryId);

            category.delete(context);
            lastCategoryId = "";
        }


    }


    public void setAdapter(SearchAdapter adapter) {
        this.adapter = adapter;
    }
}
