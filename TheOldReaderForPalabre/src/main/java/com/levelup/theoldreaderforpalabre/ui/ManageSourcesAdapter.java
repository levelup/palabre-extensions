package com.levelup.theoldreaderforpalabre.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.levelup.palabre.api.datamapping.Category;
import com.levelup.palabre.api.datamapping.Source;
import com.levelup.theoldreaderforpalabre.BuildConfig;
import com.levelup.theoldreaderforpalabre.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ludo on 09/06/15.
 */
public class ManageSourcesAdapter extends RecyclerView.Adapter<SourceViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0x01;
    private static final int VIEW_TYPE_CONTENT = 0x00;

    private static final int LINEAR = 1;
    private static final String TAG = ManageSourcesAdapter.class.getSimpleName();

    private final Context context;
    private final ItemTouchHelper touchListener;
    private ArrayList<LineItem> items;

    public void moveItem(int start, int end) {
        int max = Math.max(start, end);
        int min = Math.min(start, end);
        if (min >= 0 && max < items.size()) {
            LineItem item = items.remove(min);

            items.add(max, item);


            notifyItemMoved(min, max);
        }
    }

    public String getItemHeaderDBId(int position) {

        for (int i = position; i >= 0; i--) {
            if (items.get(i).isHeader) {
                return items.get(i).uniqueId;

            }
        }

        return null;
    }

    public String getItemDBId(int position) {
        return items.get(position).uniqueId;
    }

    public boolean isHeader(int position) {
        return items.get(position).isHeader;
    }

    public void dismissItem(RecyclerView.ViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition();
        removeItem(position);
    }

    public void removeItem(int position) {
        removeItem(position, true);
    }

    public void removeItem(int position, boolean needToRefresh) {


        if (BuildConfig.DEBUG) Log.d(TAG, "Removing: " + items.get(position).text);

        if (items.get(position).isHeader && items.size() > position) {
            if (BuildConfig.DEBUG) Log.d(TAG, "Item is header!");

            int positionSubItem = position + 1;
            while (positionSubItem < items.size() && !items.get(positionSubItem).isHeader) {
                removeItem(positionSubItem, false);
            }
        }
        items.remove(position);


        boolean headerFound = false;
        for (int i = position; i < items.size(); i++) {
            if (items.get(i).isHeader) {
                headerFound = true;
            }

            if (headerFound) {
            }
        }
//        if (needToRefresh) {
        if (BuildConfig.DEBUG) Log.d(TAG, "Removing : " + position);
//        if (position!=0) {
        notifyItemRemoved(position);

//        } else {
//            notifyDataSetChanged();
//
//        }
//            notifyDataSetChanged();
//        }
    }

    public void reload() {
        loadData();
        notifyDataSetChanged();
    }

    private static class LineItem {

        private final String uniqueId;
        public int sectionManager;

        public int sectionFirstPosition;

        public boolean isHeader;

        public String text;

        public LineItem(String text, boolean isHeader, int sectionManager,
                        int sectionFirstPosition, String uniqueId) {
            this.isHeader = isHeader;
            this.text = text;
            this.sectionManager = sectionManager;
            this.sectionFirstPosition = sectionFirstPosition;
            this.uniqueId = uniqueId;
        }
    }


    public ManageSourcesAdapter(Context context, ItemTouchHelper simpleItemTouchCallback) {
        super();
        this.context = context;
        this.touchListener = simpleItemTouchCallback;

        loadData();
    }

    private void loadData() {
        List<Category> categories = Category.getAll(context);
        List<Source> sources = Source.getAllWithCategories(context);

        items = new ArrayList<>();


        int sectionFirstPosition = 0;
        int sectionManager = -1;
        int headerCount = 0;
        int sourceCount = 0;

        // sort sources by categories
        for (Category category : categories) {


            sectionManager = (sectionManager + 1) % 2;
            sectionFirstPosition = sourceCount + headerCount;
            headerCount++;

            items.add(new LineItem(category.getTitle(), true, sectionManager, sectionFirstPosition, category.getUniqueId()));

            for (Source source : sources) {


                for (Category categoryInSource : source.getCategories()) {

                    if (categoryInSource.getUniqueId().equals(category.getUniqueId())) {

                        items.add(new LineItem(source.getTitle(), false, sectionManager, sectionFirstPosition, source.getUniqueId()));
                        sourceCount++;
                    }
                }
            }
        }


    }

    public boolean isItemHeader(int position) {
        return items.get(position).isHeader;
    }

    @Override
    public SourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_header_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.source_line_item, parent, false);
        }
        return new SourceViewHolder(view, this, touchListener);
    }

    @Override
    public void onBindViewHolder(SourceViewHolder holder, int position) {
        final LineItem item = items.get(position);


        holder.bindItem(item.text);


        if (item.isHeader) {
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).isHeader ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }
}
