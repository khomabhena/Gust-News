package com.mabinc.gust;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class XAlgorithms {

    private Context context;
    DBOperations dbOperations;
    SQLiteDatabase db;

    public XAlgorithms(Context context) {
        this.context = context;
        dbOperations = new DBOperations(context);
        db = dbOperations.getWritableDatabase();
    }

    /**For single recylerview*/
    LinearLayoutManager initializeSingleRecyclerviewLayouts(RecyclerView recyclerViews, int layout) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, layout, false);
        recyclerViews.setLayoutManager(layoutManager);
        recyclerViews.setHasFixedSize(true);

        return layoutManager;
    }

    void initializeRecyclerviewLayouts(RecyclerView[] recyclerViews, int layout) {
        for (int x = 0; x < recyclerViews.length; x++) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, layout, false);
            recyclerViews[x].setLayoutManager(layoutManager);
            recyclerViews[x].setHasFixedSize(true);
        }
    }

    List loadAccounts(List listAvailableArticles, List listAccount) {
        for (int x = 0; x < listAvailableArticles.size(); x++) {
            SetterArticles setterArticles = (SetterArticles) listAvailableArticles.get(x);

            boolean isAvailable = false;
            for (int y = 0; y < listAccount.size(); y++) {
                SetterAccount setterAccount = (SetterAccount) listAccount.get(y);

                if (setterAccount.getKey().equals(setterArticles.getAccountKey()))
                    isAvailable = true;
            }

            if (!isAvailable) {
                SetterAccount setterAccount = dbOperations.getAccountName(db, setterArticles.getAccountKey());
                listAccount.add(setterAccount);
            }
        }

        return listAccount;
    }

    List loadSources(List listAvailableArticles, List listSource) {
        for (int x = 0; x < listAvailableArticles.size(); x++) {
            SetterArticles setterArticles = (SetterArticles) listAvailableArticles.get(x);

            boolean isAvailable = false;
            for (int y = 0; y < listSource.size(); y++) {
                SetterSources setterSources = (SetterSources) listSource.get(y);

                if (setterSources.getKey().equals(setterArticles.getSourceKey()))
                    isAvailable = true;
            }

            if (!isAvailable) {
                SetterSources setterSources = dbOperations.getSourceName(db, setterArticles.getSourceKey());
                listSource.add(setterSources);
            }
        }

        return listSource;
    }

    List loadMonths(List listAvailableArticles, List listMonth) {
        for (int x = 0; x < listAvailableArticles.size(); x++) {
            SetterArticles setterArticles = (SetterArticles) listAvailableArticles.get(x);

            boolean isAvailable = false;
            for (int y = 0; y < listMonth.size(); y++) {
                SetterMonth setterMonth = (SetterMonth) listMonth.get(y);

                if (getMonthYear(setterMonth.getTimestamp()).equals(getMonthYear(setterArticles.getTimestamp())))
                    isAvailable = true;
            }

            if (!isAvailable) {
                SetterMonth setterMonth = new SetterMonth(getMonth(setterArticles.getTimestamp()),
                        getYear(setterArticles.getTimestamp()), setterArticles.getTimestamp());
                listSource.add(setterSources);
            }
        }

        return listMonth;
    }

    private String getMonthYear(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        String day = getTheValue(calendar.get(Calendar.DAY_OF_MONTH));
        String month = getTheValue(calendar.get(Calendar.MONTH) + 1);
        String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4);
        String hour = getTheValue(calendar.get(Calendar.HOUR_OF_DAY));
        String min = getTheValue(calendar.get(Calendar.MINUTE));

        return month + "-" + year;
    }

    private String getMonth(long timestamp) {
        String[] monthsSmall = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        String day = getTheValue(calendar.get(Calendar.DAY_OF_MONTH));
        int month = calendar.get(Calendar.MONTH) + 1;
        String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4);
        String hour = getTheValue(calendar.get(Calendar.HOUR_OF_DAY));
        String min = getTheValue(calendar.get(Calendar.MINUTE));

        return monthsSmall[month];
    }

    private String getYear(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        String day = getTheValue(calendar.get(Calendar.DAY_OF_MONTH));
        String month = getTheValue(calendar.get(Calendar.MONTH) + 1);
        String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4);
        String hour = getTheValue(calendar.get(Calendar.HOUR_OF_DAY));
        String min = getTheValue(calendar.get(Calendar.MINUTE));

        return "" + year;
    }

    private String getTheValue(int value){
        String theValue = String.valueOf(value);
        if (theValue.length() == 1){
            return "0"+theValue;
        } else {
            return theValue;
        }
    }
}
