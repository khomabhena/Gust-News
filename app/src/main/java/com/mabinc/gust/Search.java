package com.mabinc.gust;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.ETC1;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    DBOperations dbOperations;
    SQLiteDatabase db;

    Context context;
    XAlgorithms xAlgorithms;
    EditText etSearch;
    TextView tvResults, tvFound;
    CardView cardResults;
    RecyclerView recyclerViewAccount, recyclerViewSource, recyclerViewMonth, recyclerViewCategory;

    List listAccount, listSource, listMonth, listCategory, listKeySourcesSetter, listAvailableArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        xAlgorithms = new XAlgorithms(context);
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
        listAvailableArticles = new ArrayList();
        listAccount = new ArrayList(); listSource = new ArrayList(); listMonth = new ArrayList(); listCategory = new ArrayList();
        etSearch = findViewById(R.id.etSearch);
        tvResults = findViewById(R.id.tvResults);
        tvFound = findViewById(R.id.tvFound);
        cardResults = findViewById(R.id.cardResults);
        recyclerViewAccount = findViewById(R.id.recyclerViewAccount);
        recyclerViewSource = findViewById(R.id.recyclerViewSource);
        recyclerViewMonth = findViewById(R.id.recyclerViewMonth);
        recyclerViewCategory = findViewById(R.id.recyclerViewCategory);

        xAlgorithms.initializeSingleRecyclerviewLayouts(recyclerViewAccount, LinearLayoutManager.HORIZONTAL);
        xAlgorithms.initializeSingleRecyclerviewLayouts(recyclerViewSource, LinearLayoutManager.HORIZONTAL);
        xAlgorithms.initializeSingleRecyclerviewLayouts(recyclerViewMonth, LinearLayoutManager.HORIZONTAL);
        xAlgorithms.initializeSingleRecyclerviewLayouts(recyclerViewCategory, LinearLayoutManager.HORIZONTAL);


    }

    private class BackTask extends AsyncTask<Void, SetterArticles, Integer> {


        @Override
        protected Integer doInBackground(Void... params) {
            Cursor cursor;
            listAvailableArticles = new ArrayList();
            listKeySourcesSetter = dbOperations.getSourceKeysSetter(db);
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
            listAvailableArticles.add(values[0]);
        }

        @Override
        protected void onPostExecute(Integer count) {
            loadSearchContents(listAvailableArticles);
        }
    }

    private void loadSearchContents(List listAvailableArticles) {
        List list = xAlgorithms.loadAccounts(listAvailableArticles, listAccount);
        xAlgorithms.loadSources(listAvailableArticles, listSource);
        xAlgorithms.loadMonths(listAvailableArticles, listMonth);
    }

    class AdapterAccount extends RecyclerView.Adapter<AdapterAccount.Holder> {

        private List listAdapter;

        public AdapterAccount(List listAdapter) {
            this.listAdapter = listAdapter;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            int layoutIdForListItem = R.layout.row_tags;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
            Holder holder = new Holder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.bind((SetterAccount) listAdapter.get(position));
        }

        @Override
        public int getItemCount() {
            return listAdapter.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView tvName;

            public Holder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvName);
            }

            void bind(final SetterAccount setter) {
                tvName.setText(setter.getName());
            }
        }

    }

}
