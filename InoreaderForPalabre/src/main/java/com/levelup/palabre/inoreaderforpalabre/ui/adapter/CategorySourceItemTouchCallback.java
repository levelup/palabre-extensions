package com.levelup.palabre.inoreaderforpalabre.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.levelup.palabre.api.datamapping.Category;
import com.levelup.palabre.api.datamapping.Source;
import com.levelup.palabre.inoreaderforpalabre.BuildConfig;
import com.levelup.palabre.inoreaderforpalabre.InoreaderExtension;
import com.levelup.palabre.inoreaderforpalabre.R;
import com.levelup.palabre.inoreaderforpalabre.inoreader.InoreaderService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by nicolas on 11/06/15.
 */
public class CategorySourceItemTouchCallback extends ItemTouchHelper.SimpleCallback {
    private static final String TAG = CategorySourceItemTouchCallback.class.getSimpleName();
    private CategorySourceAdapter adapter;
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
        if (BuildConfig.DEBUG) Log.d(TAG, "onSelectedChanged: "+actionState);
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            dragInitiated = true;
            initialCategory = adapter.getItemHeaderDBId(viewHolder.getAdapterPosition());
            initialSource = adapter.getItemDBId(viewHolder.getAdapterPosition());
            initialPosition = viewHolder.getAdapterPosition();
            if (BuildConfig.DEBUG) Log.d(TAG, "initial cat: "+initialCategory);

        } else if(actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            if (dragInitiated) {
                dragInitiated = false;

                final String newCategory = adapter.getItemHeaderDBId(lastPosition);
                if (BuildConfig.DEBUG) Log.d(TAG, "Drag ended for source: "+initialSource+" from "+initialCategory+" to "+ newCategory);


                if (newCategory != null && initialCategory != null && !initialCategory.equals(newCategory)) {
                    Snackbar.make(recyclerView, R.string.source_moved, Snackbar.LENGTH_LONG).setAction(R.string.undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            undoAction();
                        }
                    }).show();


                    actionHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Call<String> call = InoreaderService.getInstance(context).moveSource(initialSource, initialCategory, newCategory);
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (!response.isSuccessful()) {
                                        try {
                                            onFailure(call, new IOException(response.errorBody().string()));
                                        } catch (IOException e) {
                                            onFailure(call, new IOException());
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Snackbar.make(recyclerView, R.string.unable_to_perform, Snackbar.LENGTH_LONG).show();
                                    undoAction();
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

        if (lastItemDelete.equals(InoreaderExtension.UNCATEGORIZED_ID)) {
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
            Call<String> call = InoreaderService.getInstance(context).deleteCategory(lastItemDelete);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (!response.isSuccessful()) {
                        try {
                            onFailure(call, new IOException(response.errorBody().string()));
                        } catch (IOException e) {
                            onFailure(call, new IOException());
                        }
                    } else {

                        category.delete(context);
                    }


                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Snackbar.make(recyclerView, R.string.unable_to_perform, Snackbar.LENGTH_LONG).show();
                    undoAction();
                }
            });


        } else {
            //remove source

            final Source source = Source.getByUniqueId(context, lastItemDelete);
            if (source.getCategories() != null && source.getCategories().size() == 1) {
                // removed from last cat
                if (BuildConfig.DEBUG) Log.d(TAG, "Last one: unsubscribing");
                Call<String> call = InoreaderService.getInstance(context).unsubscribeSource(lastItemDelete);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (!response.isSuccessful()) {
                            try {
                                onFailure(call, new IOException(response.errorBody().string()));
                            } catch (IOException e) {
                                onFailure(call, new IOException());
                            }
                        } else {
                            source.delete(context);
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Snackbar.make(recyclerView, R.string.unable_to_perform, Snackbar.LENGTH_LONG).show();
                        undoAction();
                    }
                });



            } else {

                if (BuildConfig.DEBUG) Log.d(TAG, "Not last one: removing from cat");

                Call<String> call = InoreaderService.getInstance(context).removeSourceFromCat(lastItemDelete, lastCategoryId);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (!response.isSuccessful()) {
                            try {
                                onFailure(call, new IOException(response.errorBody().string()));
                            } catch (IOException e) {
                                onFailure(call, new IOException());
                            }
                        } else {
                            Set<Category> newCategories = new HashSet<Category>();
                            for (Category category : source.getCategories()) {
                                if (!category.getUniqueId().equals(lastCategoryId)) {
                                    newCategories.add(category);
                                }

                            }

                            source.setCategories(newCategories);
                            source.save(context);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Snackbar.make(recyclerView, R.string.unable_to_perform, Snackbar.LENGTH_LONG).show();
                        undoAction();
                    }
                });
                
            }


        }
    }


    public void setAdapter(CategorySourceAdapter adapter) {
        this.adapter = adapter;
    }
}
