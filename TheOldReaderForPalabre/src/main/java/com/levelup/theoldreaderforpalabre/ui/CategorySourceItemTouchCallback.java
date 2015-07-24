package com.levelup.theoldreaderforpalabre.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.levelup.palabre.api.datamapping.Category;
import com.levelup.palabre.api.datamapping.Source;
import com.levelup.theoldreaderforpalabre.BuildConfig;
import com.levelup.theoldreaderforpalabre.R;
import com.levelup.theoldreaderforpalabre.SharedPreferenceKeys;
import com.levelup.theoldreaderforpalabre.TheOldReaderExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by nicolas on 11/06/15.
 */
public class CategorySourceItemTouchCallback extends ItemTouchHelper.SimpleCallback {
    private static final String TAG = CategorySourceItemTouchCallback.class.getSimpleName();
    private final String authKey;
    private ManageSourcesAdapter adapter;
    private final Context context;
    private final RecyclerView recyclerView;
    public String initialSource;
    public int lastPosition;
    public String initialCategory;
    public boolean dragInitiated;
    private Handler actionHandler = new Handler(Looper.getMainLooper());
    private String lastItemDelete;
    private boolean lastItemIsCategory;
    private String lastCategoryId;
    private int initialPosition;

    public CategorySourceItemTouchCallback(Context context, int dragDirs, int swipeDirs, RecyclerView recyclerView) {
        super(dragDirs, swipeDirs);
        this.context = context;
        this.recyclerView = recyclerView;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        authKey = sharedPref.getString(SharedPreferenceKeys.AUTH, null);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        final int fromPos = viewHolder.getAdapterPosition();
        final int toPos = target.getAdapterPosition();
//        if (!adapter.isHeader(toPos)) {
//            return false;
//        }
        adapter.moveItem(fromPos, toPos);

        return true;
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {

        if (BuildConfig.DEBUG) Log.d(TAG, "onMoved: " + fromPos + " - " + toPos);
        lastPosition = toPos;
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onSelectedChanged: " + actionState);
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            dragInitiated = true;
            initialCategory = adapter.getItemHeaderDBId(viewHolder.getAdapterPosition());
            initialSource = adapter.getItemDBId(viewHolder.getAdapterPosition());
            initialPosition = viewHolder.getAdapterPosition();
            if (BuildConfig.DEBUG) Log.d(TAG, "initial cat: " + initialCategory);

        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            if (dragInitiated) {
                dragInitiated = false;

                final String newCategory = adapter.getItemHeaderDBId(lastPosition);
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "Drag ended for source: " + initialSource + " from " + initialCategory + " to " + newCategory);


                if (!initialCategory.equals(newCategory)) {
                    Snackbar.make(recyclerView, R.string.source_moved, Snackbar.LENGTH_LONG).setAction(R.string.undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            undoAction();
                        }
                    }).show();


                    actionHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            Ion.with(context).load("POST", "https://theoldreader.com/reader/api/0/subscription/edit")
                                    .setHeader("Authorization: GoogleLogin auth", authKey)
                                    .addQuery("output", "json")
                                    .addQuery("ac", "edit")
                                    .addQuery("s", initialSource)
                                    .addQuery("a", newCategory)
                                    .asString()
                                    .setCallback(new FutureCallback<String>() {
                                        @Override
                                        public void onCompleted(Exception e, String result) {

                                            if (BuildConfig.DEBUG)
                                                Log.d(TAG, "Result: " + result, e);

                                            if (e != null) {
                                                Snackbar.make(recyclerView, R.string.unable_to_perform, Snackbar.LENGTH_LONG).show();
                                                undoAction();
                                            }
                                        }
                                    });


                        }
                    }, 2850);
                } else {
                    adapter.moveItem(lastPosition, initialPosition);
                }

            }
        }
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
        lastItemDelete = adapter.getItemDBId(adapterPosition);

        if (lastItemDelete.equals(TheOldReaderExtension.TOR_CAT_SUBSCRIPTION)) {
            Snackbar.make(recyclerView, R.string.removing_uncat_forbidden, Snackbar.LENGTH_LONG).show();
            adapter.notifyDataSetChanged();
            return;
        }

        if (adapter.isHeader(adapterPosition)) {

            lastItemIsCategory = true;

            Snackbar.make(recyclerView, R.string.category_removed, Snackbar.LENGTH_LONG).setAction(R.string.undo, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    undoAction();
                }
            }).show();
        } else {
            lastCategoryId = adapter.getItemHeaderDBId(adapterPosition);

            lastItemIsCategory = false;

            Snackbar.make(recyclerView, R.string.source_removed, Snackbar.LENGTH_LONG).setAction(R.string.undo, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    undoAction();
                }
            }).show();
        }

        actionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                performDismissAction();
            }
        }, 2850);


        adapter.dismissItem(viewHolder);
        //Remove swiped item from list and notify the RecyclerView
    }

    private void undoAction() {
        lastItemDelete = "";
        adapter.reload();
        actionHandler.removeCallbacksAndMessages(null);
    }

    private void performDismissAction() {
        if (lastItemIsCategory) {
            final Category category = Category.getByUniqueId(context, lastItemDelete);


            Ion.with(context).load("POST", "https://theoldreader.com/reader/api/0/disable-tag")
                    .setHeader("Authorization: GoogleLogin auth", authKey)
                    .addQuery("output", "json")
                    .addQuery("s", lastItemDelete)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {

                            if (BuildConfig.DEBUG) Log.d(TAG, "Result: " + result, e);

                            if (e != null || !result.equals("OK")) {
                                Snackbar.make(recyclerView, R.string.unable_to_perform, Snackbar.LENGTH_LONG).show();
                                undoAction();
                            } else {
                                category.delete(context);
                            }
                        }
                    });


        } else {
            //remove source

            final Source source = Source.getByUniqueId(context, lastItemDelete);


            Map<String, List<String>> params = new HashMap<>();

            List<String> value = new ArrayList<>();
            value.add("unsubscribe");
            params.put("ac", value);

            value = new ArrayList<>();
            value.add(lastItemDelete);
            params.put("s", value);


            Ion.with(context).load("POST", "https://theoldreader.com/reader/api/0/subscription/edit")
                    .setHeader("Authorization: GoogleLogin auth", authKey)
                    .setBodyParameters(params)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {

                            if (BuildConfig.DEBUG) Log.d(TAG, "Result: " + result, e);

                            if (e != null || !result.equals("OK")) {
                                Snackbar.make(recyclerView, R.string.unable_to_perform, Snackbar.LENGTH_LONG).show();
                                undoAction();
                            } else {
                                source.delete(context);
                            }
                        }
                    });


        }
    }


    public void setAdapter(ManageSourcesAdapter adapter) {
        this.adapter = adapter;
    }
}
