package com.mabinc.gust;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.List;

public class InsertSources extends AsyncTask<SetterSources, Void, Void> {

    private SQLiteDatabase db;
    DBOperations dbOperations;
    List listKeys;

    public InsertSources(Context context, List listKeys) {
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
        this.listKeys = listKeys;
    }

    @Override
    protected Void doInBackground(SetterSources... params) {
        SetterSources setter = params[0];

        ContentValues values = new ContentValues();
        values.put(DBContract.Sources.KEY, setter.getKey());
        values.put(DBContract.Sources.NAME, setter.getName());
        values.put(DBContract.Sources.IS_AVAILABLE, setter.isAvailable() ? "yes": "no");

        if (!listKeys.contains(setter.getKey()))
            db.insert(DBContract.Sources.TABLE_NAME, null, values);
        else
            db.update(DBContract.Sources.TABLE_NAME, values, DBContract.Sources.KEY + "='" + setter.getKey() + "'", null);

        listKeys = dbOperations.getArticleKeys(db);

        return null;
    }

}
