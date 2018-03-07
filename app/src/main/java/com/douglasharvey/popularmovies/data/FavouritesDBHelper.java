package com.douglasharvey.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.douglasharvey.popularmovies.data.FavouritesContract.FavouritesEntry;

class FavouritesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favourites.db";

    private static final int DATABASE_VERSION = 1;

    FavouritesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVOURITES_TABLE = "CREATE TABLE " +
                FavouritesEntry.TABLE_FAVOURITES + " (" +
                FavouritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavouritesEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_NAME_SYNOPSIS + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_NAME_VOTE_AVERAGE + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_NAME_RELEASE_DATE + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_NAME_POSTER + " TEXT, " +
                FavouritesEntry.COLUMN_NAME_BACKDROP + " TEXT " +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // currently not implemented
        // if an upgrade is required, then follow principles according to https://thebhwgroup.com/blog/how-android-sqlite-onupgrade
    }
}
