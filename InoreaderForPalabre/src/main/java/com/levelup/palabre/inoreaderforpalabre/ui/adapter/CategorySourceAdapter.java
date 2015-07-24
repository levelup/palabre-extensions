package com.levelup.palabre.inoreaderforpalabre.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.levelup.palabre.api.datamapping.Category;
import com.levelup.palabre.api.datamapping.Source;
import com.levelup.palabre.inoreaderforpalabre.BuildConfig;
import com.levelup.palabre.inoreaderforpalabre.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolas on 09/06/15.
 */
public class CategorySourceAdapter extends RecyclerView.Adapter<CategorySourceViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0x01;

    private static final int VIEW_TYPE_CONTENT = 0x00;
    private static final String TAG = CategorySourceAdapter.class.getSimpleName();
    private final Context context;
    private final List<LineItem> items = new ArrayList<>();
    private final ItemTouchHelper touchListener;

    public CategorySourceAdapter(Context context, ItemTouchHelper simpleItemTouchCallback) {
        this.context = context;
        this.touchListener = simpleItemTouchCallback;


        initData();
    }

    public void initData() {
        items.clear();
        List<Category> categories = Category.getAll(context);
        List<Source> sources = Source.getAllWithCategories(context);

        long id = 0L;
        for (Category category : categories) {



            items.add(new LineItem(category.getUniqueId(), category.getTitle(), "", true,  id));
            id++;

            for (Source source : sources) {


                for (Category categoryInSource : source.getCategories()) {

                    if (categoryInSource.getUniqueId().equals(category.getUniqueId())) {

                        items.add(new LineItem(source.getUniqueId(), source.getTitle(), source.getIconUrl(), false,  id));
                        id++;
                    }
                }
            }
        }
    }


    @Override
    public CategorySourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.text_line_item, parent, false);
        }
        return new CategorySourceViewHolder(view, this, touchListener);
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).isHeader ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    @Override
    public void onBindViewHolder(CategorySourceViewHolder holder, int position) {
        final LineItem item = items.get(position);
        final View itemView = holder.itemView;

        holder.bindItem(context, item.text, item.image);


    }


    @Override
    public long getItemId(int position) {
        return items.get(position).id;
    }

    public void moveItem(int start, int end) {
        int max = Math.max(start, end);
        int min = Math.min(start, end);
        if (min >= 0 && max < items.size()) {
            LineItem item = items.remove(min);

            items.add(max, item);


            notifyItemMoved(min, max);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void dismissItem(RecyclerView.ViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition();
        removeItem(position);
    }

    public void removeItem(int position) {
        removeItem(position, true);
    }
    public void removeItem(int position, boolean needToRefresh) {


        if (BuildConfig.DEBUG) Log.d(TAG, "Removing: "+items.get(position).text);

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
        if (BuildConfig.DEBUG) Log.d(TAG, "Removing : "+position);
//        if (position!=0) {
            notifyItemRemoved(position);

//        } else {
//            notifyDataSetChanged();
//
//        }
//            notifyDataSetChanged();
//        }
    }

    public boolean isHeader(int position) {
        return items.get(position).isHeader;
    }

    public String getItemDBId(int position) {
        return items.get(position).uniqueId;
    }

    public String getItemHeaderDBId(int position) {

        for (int i = position; i >= 0; i--) {
            if (items.get(i).isHeader) {
                return items.get(i).uniqueId;

            }
        }

        return null;
    }

    public void reload() {
        initData();
        notifyDataSetChanged();
    }

    public int getPositionForUniqueId(String uniqueId) {
        for (LineItem item : items) {
            if (item.uniqueId.equals(uniqueId)) {
                return items.indexOf(item);
            }
        }
        return 0;
    }

    private static class LineItem {


        private final String image;
        public final long id;
        public String text;
        public String uniqueId;

        public boolean isHeader;

        public LineItem(String uniqueId, String text, String image, boolean isHeader, long id) {
            this.isHeader = isHeader;
            this.text = text;
            this.image = image;
            this.uniqueId = uniqueId;
            this.id = id;
        }


    }

}
