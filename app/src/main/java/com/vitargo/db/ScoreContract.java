package com.vitargo.db;

import android.provider.BaseColumns;

public final class ScoreContract {
    private ScoreContract() {}

    public static class WinnerRate implements BaseColumns {
        public static final String TABLE_NAME = "statistic";
        public static final String COLUMN_WINNER_NAME = "name";
        public static final String COLUMN_WINNER_SCORE = "score";
        public static final String COLUMN_DATESTAMP = "date";
    }
}
