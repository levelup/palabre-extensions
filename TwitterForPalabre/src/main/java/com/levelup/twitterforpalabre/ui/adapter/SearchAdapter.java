package com.levelup.twitterforpalabre.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.levelup.palabre.api.datamapping.Category;
import com.levelup.twitterforpalabre.BuildConfig;
import com.levelup.twitterforpalabre.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolas on 15/09/15.
 */
public class SearchAdapter extends RecyclerView.Adapter {

    private static final String TAG = SearchAdapter.class.getSimpleName();
    private List<Category> searches = new ArrayList<>();

    public SearchAdapter(Context context) {
        initData(context);
    }

    private void initData(Context context) {
        searches = new ArrayList<>();
        for (Category category : Category.getAll(context)) {
            if (category.getUniqueId().startsWith("s/")) {

                searches.add(category);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item, parent, false);
        return new SearchesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Category item = searches.get(position);

        ((SearchesViewHolder) holder).bindItem(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return searches.size();
    }

    public void reloadData(Context context) {
        initData(context);
        notifyDataSetChanged();
    }

    public String getItem(int position) {
        return searches.get(position).getUniqueId();
    }

    public void dismissItem(RecyclerView.ViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition();
        removeItem(position);
    }

    public void removeItem(int position) {
        removeItem(position, true);
    }
    public void removeItem(int position, boolean needToRefresh) {


        if (BuildConfig.DEBUG) Log.d(TAG, "Removing: " + searches.get(position).getTitle());

        searches.remove(position);


        notifyItemRemoved(position);

    }

    private class SearchesViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        SearchesViewHolder(View view) {
            super(view);

            textView = (TextView) view.findViewById(R.id.title);
        }

        public void bindItem(String text) {
            textView.setText(text);


        }

        @Override
        public String toString() {
            return textView.getText().toString();
        }

    }
}
