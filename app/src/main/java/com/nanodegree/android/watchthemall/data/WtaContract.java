package com.nanodegree.android.watchthemall.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the WatchThemAll database.
 */
public class WtaContract {

    public static final String CONTENT_AUTHORITY = "com.nanodegree.android.watchthemall";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SHOWS = "shows";
    public static final String PATH_SHOW_GENRES = "showgenres";
    public static final String PATH_SHOW_PEOPLE = "showpeople";
    public static final String PATH_SEASONS = "seasons";
    public static final String PATH_EPISODES = "episodes";
    public static final String PATH_COMMENTS = "comments";
    public static final String PATH_GENRES = "genres";
    public static final String PATH_PEOPLE = "people";

    /* Inner class that defines the table contents of the show table (and show-genre
       and show-person relations tables) */
    public static final class ShowEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SHOWS).build();
        public static final Uri SHOW_GENRE_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SHOW_GENRES).build();
        public static final Uri SHOW_PERSON_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SHOW_PEOPLE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        PATH_SHOWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        PATH_SHOWS;

        public static final String TABLE_NAME = "show";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BANNER_PATH = "banner_path";
        public static final String COLUMN_THUMB_PATH = "thumb_path";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_FIRST_AIRED = "first_aired";
        public static final String COLUMN_AIR_DAY = "air_day";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_NETWORK = "network";
        public static final String COLUMN_COUNTRY = "country";
        public static final String COLUMN_HOMEPAGE = "homepage";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_LANGUAGE = "language";
        public static final String COLUMN_AIRED_EPISODES = "aired_episodes";
        public static final String COLUMN_WATCHING = "watching";
        public static final String COLUMN_WATCHED = "watched";
        public static final String COLUMN_WATCHLIST = "watchlist";
        public static final String COLUMN_WTA_UPDATE_DATE = "update_date";
        public static final String COLUMN_LAST_SEARCH_RESULT = "last_search_result";
        public static final String COLUMN_SEARCH_SCORE = "search_score";
        public static final String COLUMN_POPULARITY = "popularity";

        public static final String GENRE_RELATION_TABLE_NAME = "showgenre";

        public static final String COLUMN_SHOW_ID = "show_id";
        public static final String COLUMN_GENRE_ID = "genre_id";

        public static final String PERSON_RELATION_TABLE_NAME = "showperson";

        public static final String COLUMN_PERSON_ID = "person_id";
        public static final String COLUMN_CHARACTER = "character";

        public static Uri buildShowUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildShowGenreUri(long id) {
            return ContentUris.withAppendedId(SHOW_GENRE_CONTENT_URI, id);
        }

        public static Uri buildShowPersonUri(long id) {
            return ContentUris.withAppendedId(SHOW_PERSON_CONTENT_URI, id);
        }

        public static String getShowIdFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }
    }

    /* Inner class that defines the table contents of the season table */
    public static final class SeasonEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SEASONS).build();
        public static final Uri SHOW_SEASONS_CONTENT_URI =
                WtaContract.ShowEntry.CONTENT_URI.buildUpon()
                        .appendPath(WtaContract.PATH_SEASONS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        PATH_SEASONS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        PATH_SEASONS;

        public static final String TABLE_NAME = "season";

        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_EPISODE_COUNT = "episode_count";
        public static final String COLUMN_AIRED_EPISODES = "aired_episodes";
        public static final String COLUMN_FIRST_AIRED = "first_aired";
        public static final String COLUMN_SHOW_ID = "show_id";

        public static Uri buildSeasonUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildShowSeasonsUri(long id) {
            return ContentUris.withAppendedId(SHOW_SEASONS_CONTENT_URI, id);
        }

        public static String getSeasonIdFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }

        public static String getShowIdFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }
    }

    /* Inner class that defines the table contents of the episode table */
    public static final class EpisodeEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EPISODES).build();
        public static final Uri SEASON_EPISODES_CONTENT_URI =
                WtaContract.SeasonEntry.CONTENT_URI.buildUpon()
                        .appendPath(WtaContract.PATH_EPISODES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        PATH_EPISODES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        PATH_EPISODES;

        public static final String TABLE_NAME = "episode";

        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_SCREENSHOT_PATH = "screenshot_path";
        public static final String COLUMN_FIRST_AIRED = "first_aired";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_SEASON_NUMBER = "season_number";
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_WATCHED = "watched";
        public static final String COLUMN_WATCHLIST = "watchlist";

        public static Uri buildEpisodeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildSeasonEpisodesUri(long id) {
            return ContentUris.withAppendedId(SEASON_EPISODES_CONTENT_URI, id);
        }

        public static String getEpisodeIdFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }

        public static String getSeasonIdFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }
    }

    /* Inner class that defines the table contents of the comment table */
    public static final class CommentEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMMENTS).build();
        public static final Uri SHOW_COMMENTS_CONTENT_URI =
                WtaContract.ShowEntry.CONTENT_URI.buildUpon()
                        .appendPath(WtaContract.PATH_COMMENTS).build();
        public static final Uri EPISODE_COMMENTS_CONTENT_URI =
                WtaContract.EpisodeEntry.CONTENT_URI.buildUpon()
                        .appendPath(WtaContract.PATH_COMMENTS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        PATH_COMMENTS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        PATH_COMMENTS;

        public static final String TABLE_NAME = "comment";

        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_SPOILER = "spoiler";
        public static final String COLUMN_REVIEW = "review";
        public static final String COLUMN_LIKES = "likes_count";
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_SHOW_ID = "show_id";
        public static final String COLUMN_EPISODE_ID = "episode_id";

        public static Uri buildCommentUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildShowCommentsUri(long id) {
            return ContentUris.withAppendedId(SHOW_COMMENTS_CONTENT_URI, id);
        }

        public static Uri buildEpisodeCommentsUri(long id) {
            return ContentUris.withAppendedId(EPISODE_COMMENTS_CONTENT_URI, id);
        }

        public static String getCommentIdFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }

        public static String getEpisodeIdFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }

        public static String getShowIdFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }
    }

    /* Inner class that defines the table contents of the genre table */
    public static final class GenreEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        PATH_GENRES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        PATH_GENRES;

        public static final String TABLE_NAME = "genre";

        public static final String COLUMN_NAME = "name";

        public static Uri buildGenreUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getGenreIdFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }

        public static String getShowIdFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }
    }

    /* Inner class that defines the table contents of the person table */
    public static final class PersonEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PEOPLE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        PATH_PEOPLE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        PATH_PEOPLE;

        public static final String TABLE_NAME = "person";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_HEADSHOT_PATH = "headshot_path";

        public static Uri buildPersonUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getPersonIdFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }

        public static String getShowIdFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }
    }
}
