package com.mabinc.gust;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Context context;
    ConstraintSet constraintSetMain = new ConstraintSet();
    ConstraintSet constraintSetMenu = new ConstraintSet();
    ConstraintSet constraintSetSearch = new ConstraintSet();
    ConstraintLayout constMain;
    CardView cardMenuFab;
    ImageView ivSearch;
    RecyclerView recyclerView;

    boolean isMenuOpen = false, isSearchOpen = false;
    XConversions xConversions;

    static List list, listKeySourcesSetter;
    List<String> listKeys, listKeySources;
    DBOperations dbOperations;
    SQLiteDatabase db;
    Adapter adapter;
    String databaseRoot = "articles";
    final String sources = "sources";
    XAlgorithms xAlgorithms;

    //ToDo if source is not available do not add related article
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextAppearance(context, R.style.mainToolbarText);
        setSupportActionBar(toolbar);

        xAlgorithms = new XAlgorithms(context);
        xConversions = new XConversions(context);
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
        list = new ArrayList();
        listKeys = dbOperations.getArticleKeys(db);
        listKeySources = dbOperations.getSourceKeys(db);
        listKeySourcesSetter = dbOperations.getSourceKeysSetter(db);

        cardMenuFab = findViewById(R.id.cardMenuFab);
        constMain = findViewById(R.id.constMain);
        ivSearch = findViewById(R.id.ivSearch);
        recyclerView = findViewById(R.id.recyclerView);

        constraintSetMain.clone(constMain);
        constraintSetMenu.clone(context, R.layout.content_main_menu);
        constraintSetSearch.clone(context, R.layout.content_main_search);
        xAlgorithms.initializeSingleRecyclerviewLayouts(recyclerView, LinearLayoutManager.HORIZONTAL);

        cardMenuFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu();
            }
        });
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openSearch();
                startActivity(new Intent(context, Search.class));
            }
        });

        getSources();
        //getAccount();
        new BackTask().execute();
    }

    private void getSources() {
        FirebaseDatabase.getInstance().getReference()
                .child(sources)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                            return;

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            SetterSources setter = snapshot.getValue(SetterSources.class);

                            new InsertSources(context, listKeySources).execute(setter);
                        }
                        listKeySources = dbOperations.getSourceKeys(db);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void openSearch() {
        if (isSearchOpen) {
            xConversions.swipeViews(constMain, constraintSetMain);
            isSearchOpen = false;
        } else {
            xConversions.swipeViews(constMain, constraintSetSearch);
            isSearchOpen = true;
        }
    }

    void openMenu() {
        if (isMenuOpen) {
            xConversions.swipeViews(constMain, constraintSetMain);
            isMenuOpen = false;
        } else {
            xConversions.swipeViews(constMain, constraintSetMenu);
            isMenuOpen = true;
        }
    }




    private class BackTask extends AsyncTask<Void, SetterArticles, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            Cursor cursor;
            cursor = dbOperations.getArticles(db);
            int count = cursor.getCount();

            String key, accountKey, sourceKey, writerName, headline, story, tags, imageLink, sourceLink;
            long timestamp, originalTimestamp;
            boolean isAvailable;

            while (cursor.moveToNext()) {
                key = cursor.getString(cursor.getColumnIndex(DBContract.Articles.KEY));
                accountKey = cursor.getString(cursor.getColumnIndex(DBContract.Articles.ACCOUNT_KEY));
                sourceKey = cursor.getString(cursor.getColumnIndex(DBContract.Articles.SOURCE_KEY));
                writerName = cursor.getString(cursor.getColumnIndex(DBContract.Articles.WRITER_NAME));
                headline = cursor.getString(cursor.getColumnIndex(DBContract.Articles.HEADLINE));
                story = cursor.getString(cursor.getColumnIndex(DBContract.Articles.STORY));
                tags = cursor.getString(cursor.getColumnIndex(DBContract.Articles.TAGS));
                imageLink = cursor.getString(cursor.getColumnIndex(DBContract.Articles.IMAGE_LINK));
                sourceLink = cursor.getString(cursor.getColumnIndex(DBContract.Articles.SOURCE_LINK));
                timestamp = Long.parseLong(cursor.getString(cursor.getColumnIndex(DBContract.Articles.TIMESTAMP)));
                originalTimestamp = Long.parseLong(cursor.getString(cursor.getColumnIndex(DBContract.Articles.ORIGINAL_TIMESTAMP)));
                isAvailable = cursor.getString(cursor.getColumnIndex(DBContract.Articles.IS_AVAILABLE)).equals("yes");

                SetterArticles setter = new SetterArticles(key, accountKey, sourceKey, writerName, headline, story,
                        tags, imageLink, sourceLink, timestamp, originalTimestamp, isAvailable);

                for (int x = 0; x < listKeySourcesSetter.size(); x++) {
                    SetterSources setterSources = (SetterSources) listKeySourcesSetter.get(x);
                    if (setterSources.getKey().equals(sourceKey) && setterSources.isAvailable()) {
                        if (isAvailable)
                            publishProgress(setter);
                    }
                }
            }

            return count;
        }

        @Override
        protected void onProgressUpdate(SetterArticles... values) {
            list.add(values[0]);
        }

        @Override
        protected void onPostExecute(Integer count) {
            if (count != 0) {
                adapter = new Adapter(list);
                recyclerView.setAdapter(adapter);
            }
            loadInbox();
        }
    }

    private void loadInbox() {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(databaseRoot)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists())
                            return;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            SetterArticles setter = snapshot.getValue(SetterArticles.class);

                            new InsertArticles(context, listKeys).execute(setter);
                        }

                        new BackTask().execute();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //
                    }
                });
    }

    class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private List listAdapter;

        public Adapter(List listAdapter) {
            this.listAdapter = listAdapter;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            int layoutIdForListItem = R.layout.row_article;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
            Holder holder = new Holder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.bind((SetterArticles) listAdapter.get(position));
        }

        @Override
        public int getItemCount() {
            return listAdapter.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            ImageView ivImage;
            TextView tvHeadline, tvIntroduction, tvHeadlineH, tvIntroductionH, tvHeadlineI;
            ConstraintLayout constImage, constHeadline, constIntroduction;

            public Holder(View itemView) {
                super(itemView);
                ivImage = itemView.findViewById(R.id.ivImage);
                tvHeadline = itemView.findViewById(R.id.tvHeadline);
                tvIntroduction = itemView.findViewById(R.id.tvIntroduction);
                constImage = itemView.findViewById(R.id.constImage);
                constHeadline = itemView.findViewById(R.id.constHeadline);
                tvHeadlineH = itemView.findViewById(R.id.tvHeadlineH);
                tvIntroductionH = itemView.findViewById(R.id.tvIntroductionH);
                constIntroduction = itemView.findViewById(R.id.constIntroduction);
                tvHeadlineI = itemView.findViewById(R.id.tvHeadlineI);
            }

            void bind(final SetterArticles setter) {
                int position = getAdapterPosition();
                if (position == 0)
                    constVisibility(setter, true, false, false);
                else if (position > 0 && position < 5)
                    constVisibility(setter, false, true, false);
                else
                    constVisibility(setter, false, false, true);
            }

            private void constVisibility(SetterArticles setter, boolean b, boolean b1, boolean b2) {
                if (b) {
                    tvHeadline.setText(setter.getHeadline());
                    tvIntroduction.setText(setter.getStory().substring(0, 200));
                    constIntroduction.setVisibility(View.VISIBLE); constHeadline.setVisibility(View.GONE); constIntroduction.setVisibility(View.GONE);
                } else if (b1) {
                    tvHeadlineH.setText(setter.getHeadline());
                    tvIntroductionH.setText(setter.getStory().substring(0, 200));
                    constImage.setVisibility(View.GONE); constHeadline.setVisibility(View.VISIBLE); constIntroduction.setVisibility(View.GONE);
                } else {
                    tvHeadlineI.setText(setter.getHeadline());
                    constImage.setVisibility(View.GONE); constHeadline.setVisibility(View.GONE); constIntroduction.setVisibility(View.VISIBLE);
                }
            }
        }

    }

}
