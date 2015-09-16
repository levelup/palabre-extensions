package com.levelup.twitterforpalabre;

import android.animation.Animator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.levelup.ViewUtils;
import com.levelup.palabre.api.datamapping.Category;

public class ManageSearchesActivity extends AppCompatActivity {

    private EmptyRecyclerView searchList;
    private SearchAdapter adapter;
    private View searchAddContainer;
    private View searchAddCard;
    private FloatingActionButton fab;
    private View searchAddBackground;
    private TextInputLayout searchAddInput;
    private View searchAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_searches);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        searchList = (EmptyRecyclerView) findViewById(R.id.search_list);

        searchAddContainer = findViewById(R.id.search_add_container);
        searchAddBackground = findViewById(R.id.search_add_background);
        searchAddCard = findViewById(R.id.search_add_card);
        searchAddInput = (TextInputLayout) findViewById(R.id.search_add_input);
        searchAddButton = findViewById(R.id.search_add_button);

        searchAddBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAddSearch();
            }
        });

        searchAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        searchAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = String.valueOf(searchAddInput.getEditText().getText());
                if (!TextUtils.isEmpty(text)) {
                    hideAddSearch();
                    new Category().title(text).uniqueId("s/" + text).save(ManageSearchesActivity.this);
                    adapter.reloadData(ManageSearchesActivity.this);
                }
            }
        });

        SearchItemTouchCallback searchItemTouchCallback = new SearchItemTouchCallback(this, 0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, searchList);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(searchItemTouchCallback);
        adapter = new SearchAdapter(this);

        searchItemTouchCallback.setAdapter(adapter);

        searchList.setLayoutManager(new LinearLayoutManager(this));

        searchList.setAdapter(adapter);

        searchList.setEmptyView(findViewById(R.id.empty_view));


        itemTouchHelper.attachToRecyclerView(searchList);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchAddBackground.setAlpha(0);
                searchAddContainer.setVisibility(View.VISIBLE);
                final int dp200 = ViewUtils.dipToPixel(ManageSearchesActivity.this, 200);
                searchAddCard.setTranslationY(dp200);
                fab.animate().translationY(dp200);
                searchAddCard.animate().translationY(0).setListener(null);
                searchAddBackground.animate().alpha(1);

            }
        });

        searchAddButton.setEnabled(false);
        searchAddInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchAddButton.setEnabled(!TextUtils.isEmpty(s) && s.length() >= 3);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void hideAddSearch() {
        searchAddBackground.animate().alpha(0);
        fab.animate().translationY(0);
        searchAddCard.animate().translationY(ViewUtils.dipToPixel(ManageSearchesActivity.this, 200)).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                searchAddContainer.setVisibility(View.GONE);
                searchAddInput.getEditText().setText("");

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

}
