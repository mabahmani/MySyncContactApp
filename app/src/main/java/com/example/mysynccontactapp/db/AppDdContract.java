package com.example.mysynccontactapp.db;

import android.provider.BaseColumns;

public class AppDdContract {
    public AppDdContract() {
    }

    public static class FriendEntry implements BaseColumns {
        public static final String TABLE_NAME = "friend";
        public static final String COLUMN_NAME_PHONE = "phone";
    }
}
