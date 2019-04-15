package com.serzhan.practice.contentprovider.content_provider;

import android.arch.persistence.room.Room;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.serzhan.practice.contentprovider.database.NoteDatabase;
import com.serzhan.practice.contentprovider.utils.ConvertUtils;

public class MyContentProvider extends ContentProvider {

    private static final String DB_NAME = "note_db";
    private static final String AUTHORITY = "com.serzhan.practice.contentprovider.content_provider.MyContentProvider";
    private static final String NOTES_TABLE = "notes";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + NOTES_TABLE);

    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final int NOTES = 1;
    public static final int NOTE_ID = 2;

    static {
        sUriMatcher.addURI(AUTHORITY, NOTES_TABLE, NOTES);
        sUriMatcher.addURI(AUTHORITY, NOTES_TABLE + "/#", NOTE_ID);
    }

    private NoteDao mNoteDao;
    private NoteDatabase mDatabase;

    @Override
    public boolean onCreate() {
        mDatabase = Room.databaseBuilder(getContext(), NoteDatabase.class, DB_NAME).build();
        mNoteDao = new NoteDao(mDatabase);
        return mDatabase != null;
    }

    @Override
    public Cursor query(Uri uri,  String[] projection,  String selection,  String[] selectionArgs,  String sortOrder) {
        int uriType = sUriMatcher.match(uri);
        final Cursor cursor;
        switch (uriType) {
            case NOTES:
                cursor = mNoteDao.selectAll();
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    
    @Override
    public String getType(Uri uri) {
        return null;
    }

    
    @Override
    public Uri insert( Uri uri,  ContentValues values) {
        int uriType = sUriMatcher.match(uri);
        long id;
        switch (uriType) {
            case NOTES:
                if (values != null)
                    id = mNoteDao.addNote(ConvertUtils.convertContentValuesToNote(values));
                else
                    throw new IllegalArgumentException("Content Values can't be null");
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse("Note/" + id);
    }

    @Override
    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update( Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (uriType) {
            case NOTES:
                if (values != null)
                    rowsUpdated = mNoteDao.updateNote(ConvertUtils.convertContentValuesToNote(values));
                else
                    throw new IllegalArgumentException("Content Values can't be null");
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
