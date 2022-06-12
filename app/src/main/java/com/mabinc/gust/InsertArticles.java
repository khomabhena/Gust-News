package com.mabinc.gust;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.List;

public class InsertArticles extends AsyncTask<SetterArticles, Void, Void> {

    private SQLiteDatabase db;
    DBOperations dbOperations;
    List listKeys;

    public InsertArticles(Context context, List listKeys) {
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
        this.listKeys = listKeys;
    }

    @Override
    protected Void doInBackground(SetterArticles... params) {
        SetterArticles setter = params[0];

        ContentValues values = new ContentValues();
        values.put(DBContract.Articles.KEY, setter.getKey());
        values.put(DBContract.Articles.ACCOUNT_KEY, setter.getAccountKey());
        values.put(DBContract.Articles.SOURCE_KEY, setter.getSourceKey());
        values.put(DBContract.Articles.WRITER_NAME, setter.getWriterName());
        //values.put(DBContract.Articles.SOURCE_NAME, setter.getSourceName());
        values.put(DBContract.Articles.HEADLINE, setter.getHeadline());
        values.put(DBContract.Articles.STORY, setter.getStory());
        values.put(DBContract.Articles.TAGS, setter.getTags());
        values.put(DBContract.Articles.IMAGE_LINK, setter.getImageLink());
        values.put(DBContract.Articles.SOURCE_LINK, setter.getSourceLink());
        values.put(DBContract.Articles.TIMESTAMP, setter.getTimestamp());
        values.put(DBContract.Articles.ORIGINAL_TIMESTAMP, setter.getOriginalTimestamp());
        //values.put(DBContract.Articles.IS_FAVORITE, setter.isFavorite() ? "yes": "no");
        values.put(DBContract.Articles.IS_AVAILABLE, setter.isAvailable() ? "yes": "no");

        if (!listKeys.contains(setter.getKey()))
            db.insert(DBContract.Articles.TABLE_NAME, null, values);
        else
            db.update(DBContract.Articles.TABLE_NAME, values, DBContract.Articles.KEY + "='" + setter.getKey() + "'", null);

        listKeys = dbOperations.getArticleKeys(db);

        return null;
    }

}
