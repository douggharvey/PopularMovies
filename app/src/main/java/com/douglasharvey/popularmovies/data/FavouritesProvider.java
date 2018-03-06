package com.douglasharvey.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.douglasharvey.popularmovies.data.FavouritesContract.FavouritesEntry;


public class FavouritesProvider extends ContentProvider {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = FavouritesProvider.class.getSimpleName();

    private FavouritesDBHelper favouritesDBHelper;

    // UriMatcher codes
    private static final int FAVOURITES = 100;
    private static final int FAVOURITES_WITH_ID = 200;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavouritesContract.CONTENT_AUTHORITY;

        // add a code for each type of URI you want
        uriMatcher.addURI(authority, FavouritesContract.PATH_FAVOURITES, FAVOURITES);

        uriMatcher.addURI(authority, FavouritesContract.PATH_FAVOURITES + "/#", FAVOURITES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        favouritesDBHelper = new FavouritesDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = favouritesDBHelper.getReadableDatabase();
        int match = uriMatcher.match(uri);

        Cursor retCursor;

        switch (match) {
            case FAVOURITES:
                retCursor = db.query(FavouritesEntry.TABLE_FAVOURITES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case FAVOURITES_WITH_ID:

                String id = uri.getPathSegments().get(1);

                String selectionId = "_id=?";
                String[] selectionArgsID = new String[]{id};
                retCursor = db.query(FavouritesEntry.TABLE_FAVOURITES,
                        projection,
                        selectionId,
                        selectionArgsID,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);

        switch (match){
            case FAVOURITES:{
                return FavouritesEntry.CONTENT_DIR_TYPE;
            }
            case FAVOURITES_WITH_ID:{
                return FavouritesEntry.CONTENT_ITEM_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = favouritesDBHelper.getWritableDatabase();
        Uri returnUri;

        int match = uriMatcher.match(uri);

        switch (match) {
            case FAVOURITES: {
                long id = db.insert(FavouritesEntry.TABLE_FAVOURITES, null, contentValues);
                if (id > 0) {
                    returnUri = FavouritesEntry.buildFavouritesUri(id);
                } else {
                    throw new SQLException("Failed to insert row into: " + uri);
                }

                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = favouritesDBHelper.getReadableDatabase();

        int match = uriMatcher.match(uri);

        int rowsDeleted;
        switch (match) {
            case FAVOURITES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                rowsDeleted = db.delete(FavouritesEntry.TABLE_FAVOURITES, "_id=?", new String[]{id});
                break;
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;

    }

    //Coded for completeness however not executed from the app
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = favouritesDBHelper.getWritableDatabase();
        int numUpdated;

        if (contentValues == null){
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch(uriMatcher.match(uri)){
            case FAVOURITES:{
                numUpdated = db.update(FavouritesEntry.TABLE_FAVOURITES,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case FAVOURITES_WITH_ID: {
                numUpdated = db.update(FavouritesEntry.TABLE_FAVOURITES,
                        contentValues,
                        FavouritesEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }

}
