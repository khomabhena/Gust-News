package com.mabinc.gust;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DBOperations extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1; //8
    private static final String DB_NAME = "gust_news.db";
    //private String localUid;

    Context context;

    DBOperations(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_12);
        db.execSQL(QUERY_13);
        db.execSQL(QUERY_KEYWORDS);
        db.execSQL(QUERY_QUEST);
        db.execSQL(QUERY_RESPONSES);
        db.execSQL(QUERY_ACTIONS);
        db.execSQL(QUERY_ORDERED_MEALS);
        db.execSQL(QUERY_ACTIONS_ONE);
        db.execSQL(QUERY_ACTIONS_TWO);
        db.execSQL(QUERY_ACTIONS_THREE);
        db.execSQL(QUERY_ACTIONS_FOUR);
        db.execSQL(QUERY_ACTIONS_FIVE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Articles.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Sources.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Keywords.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Orders.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.DeliveryTime.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Categories.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Account.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Actions1.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Actions2.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Actions3.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Actions4.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Actions5.TABLE_NAME);

        onCreate(db);
    }

    Cursor getArticles(SQLiteDatabase db) {
        Cursor cursor;

        String[] projections = {
                DBContract.Articles.ID,
                DBContract.Articles.ACCOUNT_KEY,
                DBContract.Articles.SOURCE_KEY,
                DBContract.Articles.WRITER_NAME,
                DBContract.Articles.SOURCE_NAME,
                DBContract.Articles.HEADLINE,
                DBContract.Articles.STORY,
                DBContract.Articles.TAGS,
                DBContract.Articles.IMAGE_LINK,
                DBContract.Articles.SOURCE_LINK,
                DBContract.Articles.TIMESTAMP,
                DBContract.Articles.ORIGINAL_TIMESTAMP,
                DBContract.Articles.IS_FAVORITE,
                DBContract.Articles.IS_AVAILABLE,
        };
        cursor = db.query(
                true,
                DBContract.Articles.TABLE_NAME,
                projections,
                null,
                null,
                DBContract.Articles.KEY,
                null,
                DBContract.Articles.TIMESTAMP + " DESC",
                null
        );

        return cursor;
    }

    List<String> getArticleKeys(SQLiteDatabase db) {
        Cursor cursor;

        String[] arrayMax = new String[]{""};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        String[] projections = {DBContract.Articles.KEY};
        cursor = db.query(true, DBContract.Articles.TABLE_NAME, projections, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            listKeys.add(cursor.getString(cursor.getColumnIndex(DBContract.Articles.KEY)));
        }
        cursor.close();

        return listKeys;
    }

    SetterAccount getAccountName(SQLiteDatabase db, String accountKey) {
        Cursor cursor;

        String name = "";
        String[] projections = {DBContract.Account.NAME};
        cursor = db.query(true, DBContract.Account.TABLE_NAME, projections,
                DBContract.Account.KEY + "='" + accountKey + "'",
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(DBContract.Account.NAME));
        }
        SetterAccount setterAccount = new SetterAccount(accountKey, name, false);

        cursor.close();

        return setterAccount;
    }

    SetterSources getSourceName(SQLiteDatabase db, String sourceKey) {
        Cursor cursor;

        String name = "";
        String[] projections = {DBContract.Sources.NAME};
        cursor = db.query(true, DBContract.Sources.TABLE_NAME, projections,
                DBContract.Account.KEY + "='" + sourceKey + "'",
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(DBContract.Sources.NAME));
        }
        SetterSources setterSources = new SetterSources(sourceKey, name, false);

        cursor.close();

        return setterSources;
    }

    List<String> getSourceKeys(SQLiteDatabase db) {
        Cursor cursor;

        String[] arrayMax = new String[]{""};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        String[] projections = {DBContract.Sources.KEY};
        cursor = db.query(true, DBContract.Sources.TABLE_NAME, projections, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            listKeys.add(cursor.getString(cursor.getColumnIndex(DBContract.Sources.KEY)));
        }
        cursor.close();

        return listKeys;
    }

    List getSourceKeysSetter(SQLiteDatabase db) {
        Cursor cursor;

        List listKeys = new ArrayList();
        String[] projections = {DBContract.Sources.KEY, DBContract.Sources.IS_AVAILABLE};
        cursor = db.query(true, DBContract.Sources.TABLE_NAME, projections, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String key = cursor.getString(cursor.getColumnIndex(DBContract.Sources.KEY));
            boolean isAvailable = cursor.getString(cursor.getColumnIndex(DBContract.Sources.IS_AVAILABLE)).equals("yes");
            SetterSources setterSources = new SetterSources(key, null, isAvailable);
            listKeys.add(setterSources);
        }
        cursor.close();

        return listKeys;
    }




    Cursor getCategory(SQLiteDatabase db) {
        Cursor cursor;

        String[] projections = {
                DBContract.Categories.ID,
                DBContract.Categories.KEY,
                DBContract.Categories.NAME,
                DBContract.Categories.POSITION,
                DBContract.Categories.IS_AVAILABLE
        };

// + DBContract.Event.SOURCE_LINK + "='" + localUid + "'"
        cursor = db.query(
                true,
                DBContract.Categories.TABLE_NAME, projections,
                null,
                null,
                DBContract.Categories.KEY,
                null,
                DBContract.Categories.POSITION + " ASC",
                null
        );

        return cursor;
    }

    List<String> getCategoryKeys(SQLiteDatabase db) {
        Cursor cursor;

        String[] arrayMax = new String[]{""};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        String[] projections = {DBContract.Categories.KEY};
        cursor = db.query(true, DBContract.Categories.TABLE_NAME, projections, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            listKeys.add(cursor.getString(cursor.getColumnIndex(DBContract.Categories.KEY)));
        }
        cursor.close();

        return listKeys;
    }


    Cursor getDeliveryArea(SQLiteDatabase db) {
        Cursor cursor;

        String[] projections = {
                DBContract.Articles.ID,
                DBContract.Articles.KEY,
                DBContract.Articles.ACCOUNT_LINK,
                DBContract.Articles.SOURCE_LINK,
                DBContract.Articles.HEADLINE,
                DBContract.Articles.STORY,
                DBContract.Articles.TAGS,
                DBContract.Articles.IMAGE_LINK
        };

// + DBContract.Event.SOURCE_LINK + "='" + localUid + "'"
        cursor = db.query(
                true,
                DBContract.Articles.TABLE_NAME, projections,
                null,
                null,
                DBContract.Articles.KEY,
                null,
                DBContract.Articles.IMAGE_LINK + " ASC",
                null
        );

        return cursor;
    }

    List<String> getDeliveryAreaKeys(SQLiteDatabase db) {
        Cursor cursor;

        String[] arrayMax = new String[]{""};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        String[] projections = {DBContract.Articles.KEY};
        cursor = db.query(true, DBContract.Articles.TABLE_NAME, projections, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            listKeys.add(cursor.getString(cursor.getColumnIndex(DBContract.Articles.KEY)));
        }
        cursor.close();

        return listKeys;
    }



    Cursor getOrders(SQLiteDatabase db, String uid) {
        Cursor cursor;

        String[] projections = {
                DBContract.Orders.ID,
                DBContract.Orders.UID,
                DBContract.Orders.ORDER_CODE,
                DBContract.Orders.KEY,
                DBContract.Orders.NOTES,
                DBContract.Orders.NAME,
                DBContract.Orders.SURNAME,
                DBContract.Orders.CONFIRMATION_CODE,
                DBContract.Orders.ADDRESS,
                DBContract.Orders.ORDER_CHARGE,
                DBContract.Orders.DELIVERY_CHARGE,
                DBContract.Orders.PROCESSING_CHARGE,
                DBContract.Orders.SERVICE_CHARGE,
                DBContract.Orders.TIMESTAMP,
                DBContract.Orders.DELIVERY_START,
                DBContract.Orders.DELIVER_BEFORE,
                DBContract.Orders.DELIVERED_ON,
                DBContract.Orders.IS_PAID,
                DBContract.Orders.IS_PREPARING,
                DBContract.Orders.IS_CLUB,
                DBContract.Orders.IS_DONE,
                DBContract.Orders.IS_DELIVERED,
                DBContract.Orders.IS_TAKEAWAY,
                DBContract.Orders.TAKEAWAY_CHARGE,
                DBContract.Orders.DELIVERY_AREA_KEY
        };

        cursor = db.query(
                true,
                DBContract.Orders.TABLE_NAME, projections,
                DBContract.Orders.UID + "='" + uid + "'",
                null,
                DBContract.Orders.KEY,
                null,
                DBContract.Orders.TIMESTAMP + " DESC",
                null
        );

        return cursor;
    }

    List<String> getOrderKeys(SQLiteDatabase db) {
        Cursor cursor;

        String[] arrayMax = new String[]{""};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        String[] projections = {DBContract.Orders.KEY};
        cursor = db.query(true, DBContract.Orders.TABLE_NAME, projections, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            listKeys.add(cursor.getString(cursor.getColumnIndex(DBContract.Orders.KEY)));
        }
        cursor.close();

        return listKeys;
    }



    Cursor getMeals(SQLiteDatabase db, String type) {
        Cursor cursor;

        String[] projections = {
                DBContract.Sources.ID,
                DBContract.Sources.CATEGORY_KEY,
                DBContract.Sources.KEY,
                DBContract.Sources.NAME,
                DBContract.Sources.SIZE,
                DBContract.Sources.LIMIT,
                DBContract.Sources.LINK,
                DBContract.Sources.PRICE,
                DBContract.Sources.DESCRIPTION,
                DBContract.Sources.IS_AVAILABLE,
                DBContract.Sources.IS_HOME_DELIVERY,
                DBContract.Sources.TIMESTAMP,
                DBContract.Sources.TAKEAWAY_CHARGE
        };

        cursor = db.query(
                true,
                DBContract.Sources.TABLE_NAME, projections,
                DBContract.Sources.CATEGORY_KEY + "='" + type + "'",
                null,
                DBContract.Sources.KEY,
                null,
                DBContract.Sources.TIMESTAMP + " ASC",
                null
        );

        return cursor;
    }

    String getOrderedMeals(SQLiteDatabase db, String key) {
        Cursor cursor;

        String[] projections = {
                DBContract.Sources.ID
        };

        cursor = db.query(
                true,
                DBContract.Sources.TABLE_NAME, projections,
                DBContract.Account.ORDER_KEY + "='" + key + "'",
                null,
                DBContract.Sources.KEY,
                null,
                DBContract.Sources.TIMESTAMP + " ASC",
                null
        );

        return String.valueOf(cursor.getCount());
    }

    List<String> getMealKeys(SQLiteDatabase db) {
        Cursor cursor;

        String[] arrayMax = new String[]{""};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        String[] projections = {DBContract.Sources.KEY};
        cursor = db.query(true, DBContract.Sources.TABLE_NAME, projections, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            listKeys.add(cursor.getString(cursor.getColumnIndex(DBContract.Sources.KEY)));
        }
        cursor.close();

        return listKeys;
    }

    String getFirstCategory(SQLiteDatabase db) {
        Cursor cursor;
        String key = "";

        String[] projections = {DBContract.Categories.KEY};
        cursor = db.query(true, DBContract.Categories.TABLE_NAME, projections,
                DBContract.Categories.IS_AVAILABLE + "='yes' ",
                null,
                null, null, DBContract.Categories.POSITION + " DESC", null);

        while (cursor.moveToNext()) {
            key = cursor.getString(cursor.getColumnIndex(DBContract.Categories.KEY));
        }
        cursor.close();

        return key;
    }



    Cursor getDeliveryTime(SQLiteDatabase db) {
        Cursor cursor;

        String[] projections = {
                DBContract.DeliveryTime.ID,
                DBContract.DeliveryTime.KEY,
                DBContract.DeliveryTime.NAME,
                DBContract.DeliveryTime.START_HOUR,
                DBContract.DeliveryTime.END_HOUR,
                DBContract.DeliveryTime.MINUTE,
                DBContract.DeliveryTime.EXTRA_CHARGE,
                DBContract.DeliveryTime.POSITION,
                DBContract.DeliveryTime.IS_AVAILABLE
        };

        cursor = db.query(
                true,
                DBContract.DeliveryTime.TABLE_NAME, projections,
                null,
                null,
                DBContract.DeliveryTime.KEY,
                null,
                DBContract.DeliveryTime.POSITION + " ASC",
                null
        );

        return cursor;
    }

    List<String> getDeliveryTimeKeys(SQLiteDatabase db) {
        Cursor cursor;

        String[] arrayMax = new String[]{""};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        String[] projections = {DBContract.DeliveryTime.KEY};
        cursor = db.query(true, DBContract.DeliveryTime.TABLE_NAME, projections, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            listKeys.add(cursor.getString(cursor.getColumnIndex(DBContract.DeliveryTime.KEY)));
        }
        cursor.close();

        return listKeys;
    }



    Cursor getKeywords(SQLiteDatabase db, String language, String type) {
        Cursor cursor;
        String[] projections = {
                DBContract.Keywords.ID,
                DBContract.Keywords.KEY,
                DBContract.Keywords.NAME,
                DBContract.Keywords.TYPE,
                DBContract.Keywords.ENABLED
        };

        cursor = db.query(
                true,
                DBContract.Keywords.TABLE_NAME, projections,
                DBContract.Keywords.LANGUAGE + "='" + language + "' AND " +
                        DBContract.Keywords.TYPE + "='" + type + "' ",
                null,
                DBContract.Keywords.KEY,
                null,
                DBContract.Keywords.ID + " ASC",
                null
        );

        return cursor;
    }

    List<String> getKeywordsKeys(SQLiteDatabase db) {
        Cursor cursor;

        String[] arrayMax = new String[]{""};
        List<String> listKeys = new LinkedList<>(Arrays.asList(arrayMax));
        String[] projections = {DBContract.Keywords.KEY};
        cursor = db.query(true, DBContract.Keywords.TABLE_NAME, projections, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            listKeys.add(cursor.getString(cursor.getColumnIndex(DBContract.Keywords.KEY)));
        }
        cursor.close();

        return listKeys;
    }



    Cursor getChats(SQLiteDatabase db, String language) {
        Cursor cursor;
        String[] projections = {
                DBContract.Sources.ID,
                DBContract.Sources.KEY,
                DBContract.Sources.NAME,
                DBContract.Sources.LINK,
                DBContract.Sources.PRICE,
                DBContract.Sources.IS_AVAILABLE,
                DBContract.Sources.DESCRIPTION
        };
// + DBContract.Event.SOURCE_LINK + "='" + localUid + "'"
        cursor = db.query(
                true,
                DBContract.Sources.TABLE_NAME, projections,
                DBContract.Sources.TIMESTAMP + "='" + language + "'",
                null,
                DBContract.Sources.ID,
                null,
                DBContract.Sources.DESCRIPTION + " ASC",
                null
        );

        return cursor;
    }



    private static final String QUERY_12 = "CREATE TABLE "+ DBContract.Articles.TABLE_NAME +"("+
            DBContract.Articles.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Articles.TIMESTAMP + " TEXT, " +
            DBContract.Articles.IS_SEEN + " TEXT, " +
            DBContract.Articles.IS_MALE + " TEXT, " +
            DBContract.Articles.KEY + " TEXT, " +
            DBContract.Articles.ACCOUNT_LINK + " TEXT, " +
            DBContract.Articles.SOURCE_LINK + " TEXT, " +
            DBContract.Articles.HEADLINE + " TEXT, " +
            DBContract.Articles.STORY + " TEXT, " +
            DBContract.Articles.TAGS + " TEXT, " +
            DBContract.Articles.IMAGE_LINK + " TEXT, " +
            DBContract.Articles.ORIGINAL_TIMESTAMP + " TEXT, " +
            DBContract.Articles.ACCOUNT_KEY + " TEXT, " +
            DBContract.Articles.SOURCE_KEY + " TEXT, " +
            DBContract.Articles.WRITER_NAME + " TEXT, " +
            DBContract.Articles.SOURCE_NAME + " TEXT, " +
            DBContract.Articles.IS_FAVORITE + " TEXT, " +
            DBContract.Articles.SPECIMEN_TYPE + " TEXT, " +
            DBContract.Articles.MEDICAL_LINK + " TEXT, " +
            DBContract.Articles.FORM_LINK + " TEXT, " +
            DBContract.Articles.PATIENT_DOB +" TEXT);";

    private static final String QUERY_13 = "CREATE TABLE "+ DBContract.Sources.TABLE_NAME +"("+
            DBContract.Sources.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Sources.TYPE + " TEXT, " +
            DBContract.Sources.KEY + " TEXT, " +
            DBContract.Sources.NAME + " TEXT, " +
            DBContract.Sources.LINK + " TEXT, " +
            DBContract.Sources.PRICE + " TEXT, " +
            DBContract.Sources.IS_AVAILABLE + " TEXT, " +
            DBContract.Sources.DESCRIPTION + " TEXT, " +
            DBContract.Sources.TIMESTAMP + " TEXT, " +
            DBContract.Sources.TAKEAWAY_CHARGE + " TEXT, " +
            DBContract.Sources.LIMIT + " TEXT, " +
            DBContract.Sources.SIZE + " TEXT, " +
            DBContract.Sources.IS_HOME_DELIVERY + " TEXT, " +
            DBContract.Sources.CATEGORY_KEY + " TEXT, " +
            DBContract.Sources.COl_13 + " TEXT, " +
            DBContract.Sources.COl_14 + " TEXT, " +
            DBContract.Sources.COl_15 + " TEXT, " +
            DBContract.Sources.COl_16 + " TEXT, " +
            DBContract.Sources.COl_17 + " TEXT, " +
            DBContract.Sources.COl_18 + " TEXT, " +
            DBContract.Sources.COl_19 + " TEXT, " +
            DBContract.Sources.COl_20 +" TEXT);";

    private static final String QUERY_ORDERED_MEALS = "CREATE TABLE "+ DBContract.Account.TABLE_NAME +"("+
            DBContract.Account.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Account.TYPE + " TEXT, " +
            DBContract.Account.KEY + " TEXT, " +
            DBContract.Account.NAME + " TEXT, " +
            DBContract.Account.LINK + " TEXT, " +
            DBContract.Account.PRICE + " TEXT, " +
            DBContract.Account.IS_AVAILABLE + " TEXT, " +
            DBContract.Account.DISCOUNT + " TEXT, " +
            DBContract.Account.TIMESTAMP + " TEXT, " +
            DBContract.Account.TAKEAWAY_CHARGE + " TEXT, " +
            DBContract.Account.ORDER_KEY + " TEXT, " +
            DBContract.Account.LIMIT + " TEXT, " +
            DBContract.Account.SIZE + " TEXT, " +
            DBContract.Account.DESCRIPTION + " TEXT, " +
            DBContract.Account.IS_HOME_DELIVERY + " TEXT, " +
            DBContract.Account.CATEGORY_KEY + " TEXT, " +
            DBContract.Account.COl_15 + " TEXT, " +
            DBContract.Account.COl_16 + " TEXT, " +
            DBContract.Account.COl_17 + " TEXT, " +
            DBContract.Account.COl_18 + " TEXT, " +
            DBContract.Account.COl_19 + " TEXT, " +
            DBContract.Account.COl_20 +" TEXT);";

    private static final String QUERY_QUEST = "CREATE TABLE "+ DBContract.Orders.TABLE_NAME +"("+
            DBContract.Orders.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Orders.UID + " TEXT, " +
            DBContract.Orders.ORDER_CODE + " TEXT, " +
            DBContract.Orders.KEY + " TEXT, " +
            DBContract.Orders.NOTES + " TEXT, " +
            DBContract.Orders.DELIVERY_AREA_KEY + " TEXT, " +
            DBContract.Orders.NAME + " TEXT, " +
            DBContract.Orders.SURNAME + " TEXT, " +
            DBContract.Orders.CONFIRMATION_CODE + " TEXT, " +
            DBContract.Orders.ADDRESS + " TEXT, " +
            DBContract.Orders.ORDER_CHARGE + " TEXT, " +
            DBContract.Orders.DELIVERY_CHARGE + " TEXT, " +
            DBContract.Orders.PROCESSING_CHARGE + " TEXT, " +
            DBContract.Orders.SERVICE_CHARGE + " TEXT, " +
            DBContract.Orders.TIMESTAMP + " TEXT, " +
            DBContract.Orders.DELIVER_BEFORE + " TEXT, " +
            DBContract.Orders.DELIVERED_ON + " TEXT, " +
            DBContract.Orders.IS_PAID + " TEXT, " +
            DBContract.Orders.IS_PREPARING + " TEXT, " +
            DBContract.Orders.IS_CLUB + " TEXT, " +
            DBContract.Orders.IS_DONE + " TEXT, " +
            DBContract.Orders.IS_DELIVERED + " TEXT, " +
            DBContract.Orders.IS_TAKEAWAY + " TEXT, " +
            DBContract.Orders.TAKEAWAY_CHARGE + " TEXT, " +
            DBContract.Orders.DELIVERY_START +" TEXT);";

    private static final String QUERY_RESPONSES = "CREATE TABLE "+ DBContract.DeliveryTime.TABLE_NAME +"("+
            DBContract.DeliveryTime.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.DeliveryTime.LOCAL_UID + " TEXT, " +
            DBContract.DeliveryTime.KEY + " TEXT, " +
            DBContract.DeliveryTime.START_HOUR + " TEXT, " +
            DBContract.DeliveryTime.END_HOUR + " TEXT, " +
            DBContract.DeliveryTime.MINUTE + " TEXT, " +
            DBContract.DeliveryTime.EXTRA_CHARGE + " TEXT, " +
            DBContract.DeliveryTime.IS_AVAILABLE + " TEXT, " +
            DBContract.DeliveryTime.NAME + " TEXT, " +
            DBContract.DeliveryTime.POSITION + " TEXT, " +
            DBContract.DeliveryTime.COl_9 + " TEXT, " +
            DBContract.DeliveryTime.COl_10 + " TEXT, " +
            DBContract.DeliveryTime.COl_11 + " TEXT, " +
            DBContract.DeliveryTime.COl_12 + " TEXT, " +
            DBContract.DeliveryTime.COl_13 + " TEXT, " +
            DBContract.DeliveryTime.COl_14 + " TEXT, " +
            DBContract.DeliveryTime.COl_15 + " TEXT, " +
            DBContract.DeliveryTime.COl_16 + " TEXT, " +
            DBContract.Orders.IS_DELIVERED + " TEXT, " +
            DBContract.DeliveryTime.COl_18 + " TEXT, " +
            DBContract.DeliveryTime.COl_19 + " TEXT, " +
            DBContract.DeliveryTime.COl_20 +" TEXT);";

    private static final String QUERY_ACTIONS = "CREATE TABLE "+ DBContract.Categories.TABLE_NAME +"("+
            DBContract.Categories.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Categories.LOCAL_UID + " TEXT, " +
            DBContract.Categories.KEY + " TEXT, " +
            DBContract.Categories.PARENT_KEY + " TEXT, " +
            DBContract.Categories.NAME + " TEXT, " +
            DBContract.Categories.POSITION + " TEXT, " +
            DBContract.Categories.ENABLED + " TEXT, " +
            DBContract.Categories.TIMESTAMP + " TEXT, " +
            DBContract.Categories.IS_AVAILABLE + " TEXT, " +
            DBContract.Categories.COl_8 + " TEXT, " +
            DBContract.Categories.COl_9 + " TEXT, " +
            DBContract.Categories.COl_10 + " TEXT, " +
            DBContract.Categories.COl_11 + " TEXT, " +
            DBContract.Categories.COl_12 + " TEXT, " +
            DBContract.Categories.COl_13 + " TEXT, " +
            DBContract.Categories.COl_14 + " TEXT, " +
            DBContract.Categories.COl_15 + " TEXT, " +
            DBContract.Categories.COl_16 + " TEXT, " +
            DBContract.Categories.COl_17 + " TEXT, " +
            DBContract.Categories.COl_18 + " TEXT, " +
            DBContract.Categories.COl_19 + " TEXT, " +
            DBContract.Categories.COl_20 +" TEXT);";

    private static final String QUERY_KEYWORDS = "CREATE TABLE "+ DBContract.Keywords.TABLE_NAME +"("+
            DBContract.Keywords.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Keywords.LOCAL_UID + " TEXT, " +
            DBContract.Keywords.KEY + " TEXT, " +
            DBContract.Keywords.NAME + " TEXT, " +
            DBContract.Keywords.TYPE + " TEXT, " +
            DBContract.Keywords.ENABLED + " TEXT, " +
            DBContract.Keywords.QUESTION + " TEXT, " +
            DBContract.Keywords.TIMESTAMP + " TEXT, " +
            DBContract.Keywords.LANGUAGE + " TEXT, " +
            DBContract.Keywords.COl_8 + " TEXT, " +
            DBContract.Keywords.COl_9 + " TEXT, " +
            DBContract.Keywords.COl_10 + " TEXT, " +
            DBContract.Keywords.COl_11 + " TEXT, " +
            DBContract.Keywords.COl_12 + " TEXT, " +
            DBContract.Keywords.COl_13 + " TEXT, " +
            DBContract.Keywords.COl_14 + " TEXT, " +
            DBContract.Keywords.COl_15 + " TEXT, " +
            DBContract.Keywords.COl_16 + " TEXT, " +
            DBContract.Keywords.COl_17 + " TEXT, " +
            DBContract.Keywords.COl_18 + " TEXT, " +
            DBContract.Keywords.COl_19 + " TEXT, " +
            DBContract.Keywords.COl_20 +" TEXT);";

    private static final String QUERY_ACTIONS_ONE = "CREATE TABLE "+ DBContract.Actions1.TABLE_NAME +"("+
            DBContract.Categories.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Categories.LOCAL_UID + " TEXT, " +
            DBContract.Categories.KEY + " TEXT, " +
            DBContract.Categories.PARENT_KEY + " TEXT, " +
            DBContract.Categories.NAME + " TEXT, " +
            DBContract.Categories.POSITION + " TEXT, " +
            DBContract.Categories.ENABLED + " TEXT, " +
            DBContract.Categories.TIMESTAMP + " TEXT, " +
            DBContract.Categories.IS_AVAILABLE + " TEXT, " +
            DBContract.Categories.COl_8 + " TEXT, " +
            DBContract.Categories.COl_9 + " TEXT, " +
            DBContract.Categories.COl_10 + " TEXT, " +
            DBContract.Categories.COl_11 + " TEXT, " +
            DBContract.Categories.COl_12 + " TEXT, " +
            DBContract.Categories.COl_13 + " TEXT, " +
            DBContract.Categories.COl_14 + " TEXT, " +
            DBContract.Categories.COl_15 + " TEXT, " +
            DBContract.Categories.COl_16 + " TEXT, " +
            DBContract.Categories.COl_17 + " TEXT, " +
            DBContract.Categories.COl_18 + " TEXT, " +
            DBContract.Categories.COl_19 + " TEXT, " +
            DBContract.Categories.COl_20 +" TEXT);";

    private static final String QUERY_ACTIONS_TWO = "CREATE TABLE "+ DBContract.Actions2.TABLE_NAME +"("+
            DBContract.Categories.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Categories.LOCAL_UID + " TEXT, " +
            DBContract.Categories.KEY + " TEXT, " +
            DBContract.Categories.PARENT_KEY + " TEXT, " +
            DBContract.Categories.NAME + " TEXT, " +
            DBContract.Categories.POSITION + " TEXT, " +
            DBContract.Categories.ENABLED + " TEXT, " +
            DBContract.Categories.TIMESTAMP + " TEXT, " +
            DBContract.Categories.IS_AVAILABLE + " TEXT, " +
            DBContract.Categories.COl_8 + " TEXT, " +
            DBContract.Categories.COl_9 + " TEXT, " +
            DBContract.Categories.COl_10 + " TEXT, " +
            DBContract.Categories.COl_11 + " TEXT, " +
            DBContract.Categories.COl_12 + " TEXT, " +
            DBContract.Categories.COl_13 + " TEXT, " +
            DBContract.Categories.COl_14 + " TEXT, " +
            DBContract.Categories.COl_15 + " TEXT, " +
            DBContract.Categories.COl_16 + " TEXT, " +
            DBContract.Categories.COl_17 + " TEXT, " +
            DBContract.Categories.COl_18 + " TEXT, " +
            DBContract.Categories.COl_19 + " TEXT, " +
            DBContract.Categories.COl_20 +" TEXT);";

    private static final String QUERY_ACTIONS_THREE = "CREATE TABLE "+ DBContract.Actions3.TABLE_NAME +"("+
            DBContract.Categories.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Categories.LOCAL_UID + " TEXT, " +
            DBContract.Categories.KEY + " TEXT, " +
            DBContract.Categories.PARENT_KEY + " TEXT, " +
            DBContract.Categories.NAME + " TEXT, " +
            DBContract.Categories.POSITION + " TEXT, " +
            DBContract.Categories.ENABLED + " TEXT, " +
            DBContract.Categories.TIMESTAMP + " TEXT, " +
            DBContract.Categories.IS_AVAILABLE + " TEXT, " +
            DBContract.Categories.COl_8 + " TEXT, " +
            DBContract.Categories.COl_9 + " TEXT, " +
            DBContract.Categories.COl_10 + " TEXT, " +
            DBContract.Categories.COl_11 + " TEXT, " +
            DBContract.Categories.COl_12 + " TEXT, " +
            DBContract.Categories.COl_13 + " TEXT, " +
            DBContract.Categories.COl_14 + " TEXT, " +
            DBContract.Categories.COl_15 + " TEXT, " +
            DBContract.Categories.COl_16 + " TEXT, " +
            DBContract.Categories.COl_17 + " TEXT, " +
            DBContract.Categories.COl_18 + " TEXT, " +
            DBContract.Categories.COl_19 + " TEXT, " +
            DBContract.Categories.COl_20 +" TEXT);";

    private static final String QUERY_ACTIONS_FOUR = "CREATE TABLE "+ DBContract.Actions4.TABLE_NAME +"("+
            DBContract.Categories.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Categories.LOCAL_UID + " TEXT, " +
            DBContract.Categories.KEY + " TEXT, " +
            DBContract.Categories.PARENT_KEY + " TEXT, " +
            DBContract.Categories.NAME + " TEXT, " +
            DBContract.Categories.POSITION + " TEXT, " +
            DBContract.Categories.ENABLED + " TEXT, " +
            DBContract.Categories.TIMESTAMP + " TEXT, " +
            DBContract.Categories.IS_AVAILABLE + " TEXT, " +
            DBContract.Categories.COl_8 + " TEXT, " +
            DBContract.Categories.COl_9 + " TEXT, " +
            DBContract.Categories.COl_10 + " TEXT, " +
            DBContract.Categories.COl_11 + " TEXT, " +
            DBContract.Categories.COl_12 + " TEXT, " +
            DBContract.Categories.COl_13 + " TEXT, " +
            DBContract.Categories.COl_14 + " TEXT, " +
            DBContract.Categories.COl_15 + " TEXT, " +
            DBContract.Categories.COl_16 + " TEXT, " +
            DBContract.Categories.COl_17 + " TEXT, " +
            DBContract.Categories.COl_18 + " TEXT, " +
            DBContract.Categories.COl_19 + " TEXT, " +
            DBContract.Categories.COl_20 +" TEXT);";

    private static final String QUERY_ACTIONS_FIVE = "CREATE TABLE "+ DBContract.Actions5.TABLE_NAME +"("+
            DBContract.Categories.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DBContract.Categories.LOCAL_UID + " TEXT, " +
            DBContract.Categories.KEY + " TEXT, " +
            DBContract.Categories.PARENT_KEY + " TEXT, " +
            DBContract.Categories.NAME + " TEXT, " +
            DBContract.Categories.POSITION + " TEXT, " +
            DBContract.Categories.ENABLED + " TEXT, " +
            DBContract.Categories.TIMESTAMP + " TEXT, " +
            DBContract.Categories.IS_AVAILABLE + " TEXT, " +
            DBContract.Categories.COl_8 + " TEXT, " +
            DBContract.Categories.COl_9 + " TEXT, " +
            DBContract.Categories.COl_10 + " TEXT, " +
            DBContract.Categories.COl_11 + " TEXT, " +
            DBContract.Categories.COl_12 + " TEXT, " +
            DBContract.Categories.COl_13 + " TEXT, " +
            DBContract.Categories.COl_14 + " TEXT, " +
            DBContract.Categories.COl_15 + " TEXT, " +
            DBContract.Categories.COl_16 + " TEXT, " +
            DBContract.Categories.COl_17 + " TEXT, " +
            DBContract.Categories.COl_18 + " TEXT, " +
            DBContract.Categories.COl_19 + " TEXT, " +
            DBContract.Categories.COl_20 +" TEXT);";


}