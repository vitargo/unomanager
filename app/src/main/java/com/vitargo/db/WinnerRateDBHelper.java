package com.vitargo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class WinnerRateDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WinnerRate.db";

    private static final String SQL_CREATE_STATISTIC =
            "CREATE TABLE " + ScoreContract.WinnerRate.TABLE_NAME + " (" +
                    ScoreContract.WinnerRate._ID + " INTEGER PRIMARY KEY," +
                    ScoreContract.WinnerRate.COLUMN_WINNER_NAME + " TEXT," +
                    ScoreContract.WinnerRate.COLUMN_WINNER_SCORE + " INTEGER," +
                    ScoreContract.WinnerRate.COLUMN_DATESTAMP + " TEXT)";

    private static final String SQL_DELETE_STATISTIC =
            "DROP TABLE IF EXISTS " + ScoreContract.WinnerRate.TABLE_NAME;

    public WinnerRateDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_STATISTIC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_STATISTIC);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public List<Winner> getAllWinners() {
        List<Winner> winnerList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + ScoreContract.WinnerRate.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Winner winner = new Winner();
                winner.setId(Integer.parseInt(cursor.getString(0)));
                winner.setName(cursor.getString(1));
                winner.setScore(cursor.getString(2));
                winner.setDate(cursor.getString(3));
                winnerList.add(winner);
            } while (cursor.moveToNext());
        }
        return winnerList;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ScoreContract.WinnerRate.TABLE_NAME);
    }
}