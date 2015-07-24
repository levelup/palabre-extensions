package com.levelup.theoldreaderforpalabre.ui.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.levelup.theoldreaderforpalabre.MainActivity;
import com.levelup.theoldreaderforpalabre.R;
import com.levelup.theoldreaderforpalabre.ui.CategorySourceItemTouchCallback;
import com.levelup.theoldreaderforpalabre.ui.ManageSourcesAdapter;

/**
 * Created by nicolas on 18/06/15.
 */
public class ManageSourceFragment extends Fragment {

    private View mainView;
    private BroadcastReceiver catSourceRefreshedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            manageSourcesAdapter.reload();
            recyclerView.smoothScrollToPosition(manageSourcesAdapter.getItemCount());
        }
    };
    private ManageSourcesAdapter manageSourcesAdapter;
    private RecyclerView recyclerView;


    public static final ManageSourceFragment newInstance() {
        return new ManageSourceFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainView = getActivity().getLayoutInflater().inflate(R.layout.fragment_manage_sources, container, false);


        recyclerView =
                (RecyclerView) mainView.findViewById(R.id.recycler_view);

        CategorySourceItemTouchCallback categorySourceItemTouchCallback = new CategorySourceItemTouchCallback(getActivity(), ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, recyclerView);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(categorySourceItemTouchCallback);

        manageSourcesAdapter = new ManageSourcesAdapter(getActivity(), itemTouchHelper);

        categorySourceItemTouchCallback.setAdapter(manageSourcesAdapter);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(manageSourcesAdapter);

        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(itemTouchHelper);

        return mainView;
    }

    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(MainActivity.CATEGORY_SOURCE_REFRESHED);
        getActivity().registerReceiver(catSourceRefreshedReceiver, filter);
        super.onResume();
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(catSourceRefreshedReceiver);
        super.onPause();
    }
}
