package com.douglasharvey.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public final class FavouritesContract {

    static final String CONTENT_AUTHORITY = "com.douglasharvey.popularmovies";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String PATH_FAVOURITES = "favourites";

    private FavouritesContract() {}

    public static class FavouritesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITES).build();
        // table name
        static final String TABLE_FAVOURITES = "favourites";

        // columns
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_TITLE="title";
        public static final String COLUMN_NAME_SYNOPSIS="synopsis";
        public static final String COLUMN_NAME_VOTE_AVERAGE="voteAverage";
        public static final String COLUMN_NAME_RELEASE_DATE="releaseDate";
        public static final String COLUMN_NAME_POSTER="poster";
        public static final String COLUMN_NAME_BACKDROP="backdrop";

           // create cursor of base type directory for multiple entries
        static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVOURITES;
        // create cursor of base type item for single entry
        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_FAVOURITES;

        // for building URIs on insertion
        static Uri buildFavouritesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
