package com.nanodegree.android.watchthemall.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manages a local database for shows data.
 */
public class WtaDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 14;
    private static final String DATABASE_NAME = "watch.them.all.db";

    public WtaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_SHOW_TABLE = "CREATE TABLE " +
                WtaContract.ShowEntry.TABLE_NAME + " (" +
                WtaContract.ShowEntry._ID + " INTEGER PRIMARY KEY," +
                WtaContract.ShowEntry.COLUMN_TITLE + " TEXT, " +
                WtaContract.ShowEntry.COLUMN_OVERVIEW + " TEXT, " +
                WtaContract.ShowEntry.COLUMN_POSTER_PATH + " TEXT, " +
                WtaContract.ShowEntry.COLUMN_BANNER_PATH + " TEXT, " +
                WtaContract.ShowEntry.COLUMN_THUMB_PATH + " TEXT, " +
                WtaContract.ShowEntry.COLUMN_STATUS + " TEXT, " +
                WtaContract.ShowEntry.COLUMN_YEAR + " INTEGER, " +
                WtaContract.ShowEntry.COLUMN_FIRST_AIRED + " INTEGER, " +
                WtaContract.ShowEntry.COLUMN_AIR_DAY + " TEXT, " +
                WtaContract.ShowEntry.COLUMN_RUNTIME + " INTEGER, " +
                WtaContract.ShowEntry.COLUMN_NETWORK + " TEXT, " +
                WtaContract.ShowEntry.COLUMN_COUNTRY + " TEXT, " +
                WtaContract.ShowEntry.COLUMN_HOMEPAGE + " TEXT, " +
                WtaContract.ShowEntry.COLUMN_RATING + " REAL, " +
                WtaContract.ShowEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                WtaContract.ShowEntry.COLUMN_LANGUAGE + " TEXT, " +
                WtaContract.ShowEntry.COLUMN_AIRED_EPISODES + " INTEGER, " +
                WtaContract.ShowEntry.COLUMN_WATCHING + " INTEGER DEFAULT 0, " +
                WtaContract.ShowEntry.COLUMN_WATCHED + " INTEGER DEFAULT 0, " +
                WtaContract.ShowEntry.COLUMN_WATCHLIST + " INTEGER DEFAULT 0, " +
                WtaContract.ShowEntry.COLUMN_WTA_UPDATE_DATE + " INTEGER, " +
                WtaContract.ShowEntry.COLUMN_LAST_SEARCH_RESULT + " INTEGER DEFAULT 0, " +
                WtaContract.ShowEntry.COLUMN_SEARCH_SCORE + " REAL, " +
                WtaContract.ShowEntry.COLUMN_POPULARITY + " INTEGER DEFAULT 0 " +
                ");";

        final String SQL_CREATE_SHOW_GENRE_TABLE = "CREATE TABLE " +
                WtaContract.ShowEntry.GENRE_RELATION_TABLE_NAME + " (" +
                WtaContract.ShowEntry.COLUMN_SHOW_ID + " INTEGER NOT NULL, " +
                WtaContract.ShowEntry.COLUMN_GENRE_ID + " INTEGER NOT NULL, " +
                " PRIMARY KEY (" + WtaContract.ShowEntry.COLUMN_SHOW_ID + ", " +
                WtaContract.ShowEntry.COLUMN_GENRE_ID + "), " +
                " FOREIGN KEY (" + WtaContract.ShowEntry.COLUMN_SHOW_ID + ") REFERENCES " +
                WtaContract.ShowEntry.TABLE_NAME + " (" + WtaContract.ShowEntry._ID + ") " +
                " ON UPDATE CASCADE ON DELETE CASCADE, " +
                " FOREIGN KEY (" + WtaContract.ShowEntry.COLUMN_GENRE_ID + ") REFERENCES " +
                WtaContract.GenreEntry.TABLE_NAME + " (" + WtaContract.GenreEntry._ID + ") " +
                " ON UPDATE CASCADE ON DELETE CASCADE);";

        final String SQL_CREATE_SHOW_PEOPLE_TABLE = "CREATE TABLE " +
                WtaContract.ShowEntry.PERSON_RELATION_TABLE_NAME + " (" +
                WtaContract.ShowEntry.COLUMN_SHOW_ID + " INTEGER NOT NULL, " +
                WtaContract.ShowEntry.COLUMN_PERSON_ID + " INTEGER NOT NULL, " +
                WtaContract.ShowEntry.COLUMN_CHARACTER + " TEXT, " +
                " PRIMARY KEY (" + WtaContract.ShowEntry.COLUMN_SHOW_ID + ", " +
                WtaContract.ShowEntry.COLUMN_PERSON_ID + ", " +
                WtaContract.ShowEntry.COLUMN_CHARACTER + "), " +
                " FOREIGN KEY (" + WtaContract.ShowEntry.COLUMN_SHOW_ID + ") REFERENCES " +
                WtaContract.ShowEntry.TABLE_NAME + " (" + WtaContract.ShowEntry._ID + ") " +
                " ON UPDATE CASCADE ON DELETE CASCADE, " +
                " FOREIGN KEY (" + WtaContract.ShowEntry.COLUMN_PERSON_ID + ") REFERENCES " +
                WtaContract.PersonEntry.TABLE_NAME + " (" + WtaContract.PersonEntry._ID + ") " +
                " ON UPDATE CASCADE ON DELETE CASCADE);";

        final String SQL_CREATE_SEASON_TABLE = "CREATE TABLE " +
                WtaContract.SeasonEntry.TABLE_NAME + " (" +
                WtaContract.SeasonEntry._ID + " INTEGER PRIMARY KEY," +
                WtaContract.SeasonEntry.COLUMN_NUMBER + " INTEGER NOT NULL, " +
                WtaContract.SeasonEntry.COLUMN_EPISODE_COUNT + " INTEGER, " +
                WtaContract.SeasonEntry.COLUMN_AIRED_EPISODES + " INTEGER, " +
                WtaContract.SeasonEntry.COLUMN_FIRST_AIRED + " INTEGER, " +
                WtaContract.SeasonEntry.COLUMN_SHOW_ID + " INTEGER NOT NULL, " +
                " FOREIGN KEY (" + WtaContract.SeasonEntry.COLUMN_SHOW_ID + ") REFERENCES " +
                WtaContract.ShowEntry.TABLE_NAME + " (" + WtaContract.ShowEntry._ID + ") " +
                " ON UPDATE CASCADE ON DELETE CASCADE);";

        final String SQL_CREATE_EPISODE_TABLE = "CREATE TABLE " +
                WtaContract.EpisodeEntry.TABLE_NAME + " (" +
                WtaContract.EpisodeEntry._ID + " INTEGER PRIMARY KEY," +
                WtaContract.EpisodeEntry.COLUMN_NUMBER + " INTEGER NOT NULL, " +
                WtaContract.EpisodeEntry.COLUMN_TITLE + " TEXT, " +
                WtaContract.EpisodeEntry.COLUMN_OVERVIEW + " TEXT, " +
                WtaContract.EpisodeEntry.COLUMN_SCREENSHOT_PATH + " TEXT, " +
                WtaContract.EpisodeEntry.COLUMN_FIRST_AIRED + " INTEGER, " +
                WtaContract.EpisodeEntry.COLUMN_RATING + " REAL, " +
                WtaContract.EpisodeEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                WtaContract.EpisodeEntry.COLUMN_SEASON_NUMBER + " INTEGER NOT NULL, " +
                WtaContract.EpisodeEntry.COLUMN_SEASON_ID + " INTEGER NOT NULL, " +
                WtaContract.EpisodeEntry.COLUMN_WATCHED + " INTEGER DEFAULT 0, " +
                WtaContract.EpisodeEntry.COLUMN_WATCHLIST + " INTEGER DEFAULT 0, " +
                " FOREIGN KEY (" + WtaContract.EpisodeEntry.COLUMN_SEASON_ID + ") REFERENCES " +
                WtaContract.SeasonEntry.TABLE_NAME + " (" + WtaContract.SeasonEntry._ID + ") " +
                " ON UPDATE CASCADE ON DELETE CASCADE);";

        final String SQL_CREATE_COMMENT_TABLE = "CREATE TABLE " +
                WtaContract.CommentEntry.TABLE_NAME + " (" +
                WtaContract.CommentEntry._ID + " INTEGER PRIMARY KEY," +
                WtaContract.CommentEntry.COLUMN_CREATED_AT + " INTEGER, " +
                WtaContract.CommentEntry.COLUMN_CONTENT + " TEXT, " +
                WtaContract.CommentEntry.COLUMN_SPOILER + " INTEGER DEFAULT 0 NOT NULL, " +
                WtaContract.CommentEntry.COLUMN_REVIEW + " INTEGER DEFAULT 0 NOT NULL, " +
                WtaContract.CommentEntry.COLUMN_LIKES + " INTEGER, " +
                WtaContract.CommentEntry.COLUMN_USER + " TEXT, " +
                WtaContract.CommentEntry.COLUMN_SHOW_ID+ " INTEGER, " +
                WtaContract.CommentEntry.COLUMN_EPISODE_ID + " INTEGER, " +
                " FOREIGN KEY (" + WtaContract.CommentEntry.COLUMN_SHOW_ID + ") REFERENCES " +
                WtaContract.ShowEntry.TABLE_NAME + " (" + WtaContract.ShowEntry._ID + ") " +
                " ON UPDATE CASCADE ON DELETE CASCADE, " +
                " FOREIGN KEY (" + WtaContract.CommentEntry.COLUMN_EPISODE_ID + ") REFERENCES " +
                WtaContract.EpisodeEntry.TABLE_NAME + " (" + WtaContract.EpisodeEntry._ID + ") " +
                " ON UPDATE CASCADE ON DELETE CASCADE);";

        final String SQL_CREATE_GENRE_TABLE = "CREATE TABLE " +
                WtaContract.GenreEntry.TABLE_NAME + " (" +
                WtaContract.GenreEntry._ID + " TEXT PRIMARY KEY," +
                WtaContract.GenreEntry.COLUMN_NAME + " TEXT " +
                ");";

        final String SQL_CREATE_PERSON_TABLE = "CREATE TABLE " +
                WtaContract.PersonEntry.TABLE_NAME + " (" +
                WtaContract.PersonEntry._ID + " INTEGER PRIMARY KEY," +
                WtaContract.PersonEntry.COLUMN_NAME + " TEXT, " +
                WtaContract.PersonEntry.COLUMN_HEADSHOT_PATH + " TEXT " +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_SHOW_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SHOW_GENRE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SHOW_PEOPLE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SEASON_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_EPISODE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_COMMENT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_GENRE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PERSON_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WtaContract.ShowEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WtaContract.ShowEntry.GENRE_RELATION_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WtaContract.ShowEntry.PERSON_RELATION_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WtaContract.SeasonEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WtaContract.EpisodeEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WtaContract.CommentEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WtaContract.GenreEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WtaContract.PersonEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
