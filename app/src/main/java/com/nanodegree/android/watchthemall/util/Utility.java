package com.nanodegree.android.watchthemall.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;

import com.nanodegree.android.watchthemall.BuildConfig;
import com.nanodegree.android.watchthemall.R;
import com.nanodegree.android.watchthemall.api.trakt.AirInfo;
import com.nanodegree.android.watchthemall.api.trakt.Cast;
import com.nanodegree.android.watchthemall.api.trakt.Comment;
import com.nanodegree.android.watchthemall.api.trakt.Episode;
import com.nanodegree.android.watchthemall.api.trakt.Genre;
import com.nanodegree.android.watchthemall.api.trakt.ImageList;
import com.nanodegree.android.watchthemall.api.trakt.Role;
import com.nanodegree.android.watchthemall.api.trakt.SearchResult;
import com.nanodegree.android.watchthemall.api.trakt.Season;
import com.nanodegree.android.watchthemall.api.trakt.Show;
import com.nanodegree.android.watchthemall.api.trakt.TraktService;
import com.nanodegree.android.watchthemall.api.trakt.User;
import com.nanodegree.android.watchthemall.data.WtaContract;
import com.nanodegree.android.watchthemall.data.WtaProvider;
import com.nanodegree.android.watchthemall.network.SearchByKeywordsAsyncTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utility {

    private Utility() {
        // In order to avoid instantiation for this utility class
    }

    //WatchThemAll always shows a maximum of MAX_SHOWS in search results screen
    public static final String MAX_SHOWS = "20";
    //WatchThemAll always shows a maximum of MAX_COMMENTS in show and episode comments tabs
    public static final String MAX_COMMENTS = "10";
    //WatchThemAll always shows a maximum of MAX_CAST_ENTRIES in show details info tab
    public static final String MAX_CAST_ENTRIES = "15";

    //MM/DD/YYYY HH24:MI:SS DateFormat pattern
    public static final String MONTH_DAY_YEAR_COMPLETE_HOUR_PATTERN = "MM/DD/YYYY HH24:MI:SS";

    //Trakt API stuff
    private static final String TRAKT_API_KEY_HEADER = "trakt-api-key";
    private static final String TRAKT_API_BASE_URL = "https://api-v2launch.trakt.tv";

    //WebView parameters
    public static final String HTML_TEXT_FORMAT =
            "<html><body style=\"text-align:justify; color:#7E888D;\"> %s </body></Html>";
    public static final String HTML_TEXT_MIME_TYPE = "text/html; charset=utf-8";
    public static final String HTML_TEXT_ENCODING = "UTF-8";

    //Activity and Fragments stuff
    public static final String SEARCH_KEYWORDS_EXTRA_KEY = "SEARCH_KEYWORDS";
    public static final String COLLECTION_EXTRA_KEY = "COLLECTION";
    public static final String DETAIL_URI_EXTRA_KEY = "URI";

    public static void updateShowsSearch(Context context, String searchText) {
        Utility.updateShowsSearch(context, searchText, null, null, null);
    }

    public static void updateShowsSearch(Context context, String searchText, Fragment fragment,
                                         int[] loaderIds, LoaderManager.LoaderCallbacks callbacks) {
        Boolean newActivity = Boolean.TRUE;
        if (fragment!=null) {
            // When calling from ShowsFragment in SearchResultsActivity, we don't need to transient
            // to a new activity
            newActivity = Boolean.FALSE;
            if (loaderIds!=null){
                for (int loaderId : loaderIds) {
                    fragment.getLoaderManager().restartLoader(loaderId, null, callbacks);
                }
            }
        }
        if ((searchText!=null) && (!searchText.isEmpty())) {
            SearchByKeywordsAsyncTask searchTask = new SearchByKeywordsAsyncTask(context);
            searchTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, newActivity, searchText);
        }
    }

    public static TraktService getTraktService() {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .header(Utility.TRAKT_API_KEY_HEADER,
                                        BuildConfig.TRAKT_API_KEY).build();
                        return chain.proceed(request);
                    }
                }).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Utility.TRAKT_API_BASE_URL)
                .client(client).addConverterFactory(GsonConverterFactory.create()).build();

        return retrofit.create(TraktService.class);
    }

    public static void synchronizeGenresData(Context context, String logTag, TraktService traktService) throws IOException {
        Call<List<Genre>> showGenres = traktService.showGenres();
        Response<List<Genre>> genresResponse = showGenres.execute();
        if (genresResponse.isSuccessful()) {
            List<Genre> receivedGenres = genresResponse.body();
            Vector<ContentValues> genresValues = new Vector<>(receivedGenres.size());
            for (Genre genre: receivedGenres) {
                ContentValues values = new ContentValues();
                values.put(WtaContract.GenreEntry._ID, genre.getSlug());
                values.put(WtaContract.GenreEntry.COLUMN_NAME, genre.getName());

                genresValues.add(values);
            }

            // Add to DB
            if (genresValues.size() > 0) {
                // Remove from DB old values (as only the new ones are valid from now)
                context.getContentResolver()
                        .delete(WtaContract.GenreEntry.CONTENT_URI, null, null);

                ContentValues[] insertValues = new ContentValues[genresValues.size()];
                genresValues.toArray(insertValues);
                context.getContentResolver()
                        .bulkInsert(WtaContract.GenreEntry.CONTENT_URI, insertValues);
            }

            Log.d(logTag, "Genres synchronization correctly ended");
        } else {
            Log.e(logTag, "Error occurred calling showGenres API endpoint: " + genresResponse.message());
        }
    }

    public static List<Integer> synchronizeShowsByKeywordsData(Context context, String logTag, TraktService traktService, String keywords, Integer year) throws IOException {
        Call<List<SearchResult>> searchShows = traktService.searchShowsByKeywords(keywords, year);
        Response<List<SearchResult>> searchResponse = searchShows.execute();
        List<Integer> receivedIds = new Vector<>();
        if (searchResponse.isSuccessful()) {
            List<SearchResult> searchResults = searchResponse.body();
            receivedIds = new Vector<>(searchResults.size());
            Vector<ContentValues> showsValues = new Vector<>(searchResults.size());
            Vector<ContentValues> showGenreValues = new Vector<>();
            Integer showId;
            for (SearchResult result: searchResults) {
                showId = result.getShow().getIds().getTrakt();
                receivedIds.add(showId);
                processShowSummaryData(context, logTag, traktService, showsValues, showGenreValues, showId, result.getScore());
            }

            updateShowsDbInfo(context, showsValues, showGenreValues,
                    WtaContract.ShowEntry.CONTENT_URI);

            Log.d(logTag, "Searched shows synchronization correctly ended");
        } else {
            Log.e(logTag, "Error occurred calling searchShowsByKeywords API endpoint: " + searchResponse.message());
        }

        return receivedIds;
    }

    public static List<Integer> synchronizePopularShowsData(Context context, String logTag, TraktService traktService) throws IOException {
        Call<List<Show>> popularShows = traktService.popularShows();
        Response<List<Show>> showsResponse = popularShows.execute();
        List<Integer> receivedIds = new Vector<>();
        if (showsResponse.isSuccessful()) {
            List<Show> receivedShows = showsResponse.body();
            receivedIds = new Vector<>(receivedShows.size());
            Vector<ContentValues> showsValues = new Vector<>(receivedShows.size());
            Vector<ContentValues> showGenreValues = new Vector<>();
            int popularity = 1;
            for (Show show: receivedShows) {
                receivedIds.add(show.getIds().getTrakt());
                Utility.processReceivedShow(context, popularity, show, showsValues, showGenreValues, null);
                popularity++;
            }

            Utility.updateShowsDbInfo(context, showsValues, showGenreValues, WtaContract.ShowEntry.CONTENT_URI);

            Log.d(logTag, "Popular shows synchronization correctly ended");
        } else {
            Log.e(logTag, "Error occurred calling popularShows API endpoint: " + showsResponse.message());
        }

        return receivedIds;
    }

    public static void synchronizeShowComments(Context context, String logTag, TraktService traktService, Integer showId) throws IOException {
        Call<List<Comment>> showComments = traktService.showComments(showId);
        Response<List<Comment>> commentsResponse = showComments.execute();
        if (commentsResponse.isSuccessful()) {
            List<Comment> receivedComments = commentsResponse.body();
            Vector<ContentValues> commentsValues = new Vector<>(receivedComments.size());
            Utility.processReceivedComments(context, receivedComments, commentsValues, showId);

            // Add to DB
            if (commentsValues.size() > 0) {
                ContentValues[] insertValues = new ContentValues[commentsValues.size()];
                commentsValues.toArray(insertValues);
                context.getContentResolver()
                        .bulkInsert(WtaContract.CommentEntry.CONTENT_URI, insertValues);
            }

            Log.d(logTag, "Show comments (for show " + showId + ") synchronization correctly ended");
        } else {
            Log.e(logTag, "Error occurred calling showComments API endpoint: " + commentsResponse.message());
        }
    }

    public static void synchronizeShowPeople(Context context, String logTag, TraktService traktService, Integer showId) throws IOException {
        Call<Cast> showPeople = traktService.showPeople(showId);
        Response<Cast> peopleResponse = showPeople.execute();
        if (peopleResponse.isSuccessful()) {
            Cast receivedCast = peopleResponse.body();
            List<Role> receivedPeople = receivedCast.getCast();
            if (receivedPeople!=null) {
                int synchronizeDataSize = Math.min(receivedPeople.size(), Integer.valueOf(Utility.MAX_CAST_ENTRIES));
                Vector<ContentValues> peopleValues = new Vector<>(synchronizeDataSize);
                Vector<ContentValues> showPeopleValues = new Vector<>(synchronizeDataSize);
                int count = 0;
                for (Role role : receivedPeople) {
                    if (count==Integer.valueOf(Utility.MAX_CAST_ENTRIES)) {
                        break;
                    }
                    ContentValues values = new ContentValues();
                    values.put(WtaContract.PersonEntry._ID, role.getPerson().getIds().getTrakt());
                    values.put(WtaContract.PersonEntry.COLUMN_NAME, role.getPerson().getName());
                    values.put(WtaContract.PersonEntry.COLUMN_HEADSHOT_PATH, Utility.checkForHeadshotNullValues(role.getPerson().getImages(), null));

                    ContentValues relation = new ContentValues();
                    relation.put(WtaContract.ShowEntry.COLUMN_SHOW_ID, showId);
                    relation.put(WtaContract.ShowEntry.COLUMN_PERSON_ID, role.getPerson().getIds().getTrakt());
                    relation.put(WtaContract.ShowEntry.COLUMN_CHARACTER, role.getCharacter());

                    peopleValues.add(values);
                    showPeopleValues.add(relation);
                    count++;
                }

                // Add to DB
                if (peopleValues.size() > 0) {
                    ContentValues[] insertValues = new ContentValues[peopleValues.size()];
                    peopleValues.toArray(insertValues);
                    context.getContentResolver()
                            .bulkInsert(WtaContract.PersonEntry.CONTENT_URI, insertValues);
                }

                // Add to DB show-people relations
                if (showPeopleValues.size() > 0) {
                    ContentValues[] insertRelationValues = new ContentValues[showPeopleValues.size()];
                    showPeopleValues.toArray(insertRelationValues);
                    context.getContentResolver()
                            .bulkInsert(WtaContract.ShowEntry.SHOW_PERSON_CONTENT_URI, insertRelationValues);
                }
            }
            Log.d(logTag, "Show people (for show " + showId + ") synchronization correctly ended");
        } else {
            Log.e(logTag, "Error occurred calling showPeople API endpoint: " + peopleResponse.message());
        }
    }

    public static void synchronizeShowSeasons(Context context, String logTag, TraktService traktService, Integer showId) throws IOException {
        Call<List<Season>> showSeasons = traktService.seasonsSummary(showId);
        Response<List<Season>> seasonsResponse = showSeasons.execute();
        if (seasonsResponse.isSuccessful()) {
            List<Season> receivedSeasons = seasonsResponse.body();
            Vector<ContentValues> seasonsValues = new Vector<>(receivedSeasons.size());
            Vector<ContentValues> episodesValues = new Vector<>();
            Vector<ContentValues> commentsValues = new Vector<>();
            Vector<Integer> episodes;
            for (Season season: receivedSeasons) {
                ContentValues values = new ContentValues();
                values.put(WtaContract.SeasonEntry._ID, season.getIds().getTrakt());
                values.put(WtaContract.SeasonEntry.COLUMN_NUMBER, season.getNumber());
                values.put(WtaContract.SeasonEntry.COLUMN_EPISODE_COUNT, season.getEpisode_count());
                values.put(WtaContract.SeasonEntry.COLUMN_AIRED_EPISODES, season.getAired_episodes());
                values.put(WtaContract.SeasonEntry.COLUMN_FIRST_AIRED, Utility.checkForNullValues(season.getFirst_aired(), null));
                values.put(WtaContract.SeasonEntry.COLUMN_SHOW_ID, showId);

                seasonsValues.add(values);

                episodes = Utility.processSeasonEpisodes(context, season.getIds().getTrakt(), season.getNumber(), season.getEpisodes(), episodesValues);
                for (Integer episodeNumber : episodes) {
                    Utility.processEpisodeComments(context, logTag, traktService, showId, season.getNumber(), episodeNumber, commentsValues);
                }
            }

            // Add to DB seasons
            if (seasonsValues.size() > 0) {
                ContentValues[] insertValues = new ContentValues[seasonsValues.size()];
                seasonsValues.toArray(insertValues);
                context.getContentResolver()
                        .bulkInsert(WtaContract.SeasonEntry.CONTENT_URI, insertValues);
            }

            // Add to DB episodes
            if (episodesValues.size() > 0) {
                ContentValues[] insertEpisodeValues = new ContentValues[episodesValues.size()];
                episodesValues.toArray(insertEpisodeValues);
                context.getContentResolver()
                        .bulkInsert(WtaContract.EpisodeEntry.CONTENT_URI, insertEpisodeValues);
            }

            // Add to DB comments
            if (commentsValues.size() > 0) {
                ContentValues[] insertCommentValues = new ContentValues[commentsValues.size()];
                commentsValues.toArray(insertCommentValues);
                context.getContentResolver()
                        .bulkInsert(WtaContract.CommentEntry.CONTENT_URI, insertCommentValues);
            }

            Log.d(logTag, "Show seasons (for show " + showId + ") synchronization correctly ended");
        } else {
            Log.e(logTag, "Error occurred calling seasonsSummary API endpoint: " + seasonsResponse.message());
        }
    }

    public static String getCurrentQueryPreference(Context context) {
        //Get show_collections_sort_order preference value. By default, use sort by title option
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        return preferences.getString(context.getString(R.string.pref_sort_order_key),
                context.getString(R.string.pref_sort_order_title));
    }

    public static String capitalizeAndFormatDelimiters(String str) {
        return capitalizeAndFormatDelimiters(str, null);
    }

    public static String capitalizeAndFormatDelimiters(String str, char[] delimiters) {
        return capitalizeAndFormatDelimiters(str, delimiters, null);
    }

    public static String capitalizeAndFormatDelimiters(String str, char[] delimiters, char[] fixedDelimiters) {
        int delimLen = (delimiters == null ? -1 : delimiters.length);
        if (str == null || str.length() == 0 || delimLen == 0) {
            return str;
        }
        str = str.toLowerCase();
        int strLen = str.length();
        StringBuffer buffer = new StringBuffer(strLen);
        boolean capitalizeNext = true;
        for (int i = 0; i < strLen; i++) {
            char ch = str.charAt(i);
            if (isDelimiter(ch, delimiters)) {
                if (isDelimiter(ch, fixedDelimiters)) {
                    buffer.append(ch);
                } else {
                    buffer.append(' ');
                }
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer.append(Character.toTitleCase(ch));
                capitalizeNext = false;
            } else {
                buffer.append(ch);
            }
        }

        return buffer.toString();
    }

    private static boolean isDelimiter(char ch, char[] delimiters) {
        if (delimiters == null) {
            return Character.isWhitespace(ch);
        }
        for (char delimiter : delimiters) {
            if (ch == delimiter) {
                return true;
            }
        }
        return false;
    }

    private static void processShowSummaryData(Context context, String logTag, TraktService traktService, Vector<ContentValues> showsValues,
                                               Vector<ContentValues> showGenreValues, Integer showId, Double score) throws IOException {

        Call<Show> showSummary = traktService.showSummary(showId);
        Response<Show> showResponse = showSummary.execute();
        if (showResponse.isSuccessful()) {
            Show show = showResponse.body();
            processReceivedShow(context, -1, show, showsValues, showGenreValues, score);
        } else {
            Log.e(logTag, "Error occurred calling showSummary API endpoint: " + showResponse.message());
        }
    }

    private static void processReceivedShow(Context context, int popularity, Show receivedShow, Vector<ContentValues> showsValues,
                                            Vector<ContentValues> showGenreValues, Double score) throws IOException {
        Date now = new Date();
        ContentValues values = new ContentValues();
        values.put(WtaContract.ShowEntry._ID, receivedShow.getIds().getTrakt());
        values.put(WtaContract.ShowEntry.COLUMN_TITLE, Utility.checkForNullValues(receivedShow.getTitle(),
                context.getString(R.string.unknown_title)));
        values.put(WtaContract.ShowEntry.COLUMN_OVERVIEW, Utility.checkForNullValues(receivedShow.getOverview(),
                context.getString(R.string.unknown_overview)));
        values.put(WtaContract.ShowEntry.COLUMN_POSTER_PATH, Utility.checkForPosterNullValues(receivedShow.getImages(), null));
        values.put(WtaContract.ShowEntry.COLUMN_BANNER_PATH, Utility.checkForBannerNullValues(receivedShow.getImages(), null));
        values.put(WtaContract.ShowEntry.COLUMN_THUMB_PATH, Utility.checkForThumbNullValues(receivedShow.getImages(), null));
        values.put(WtaContract.ShowEntry.COLUMN_STATUS, Utility.checkForNullValues(receivedShow.getStatus(),
                context.getString(R.string.unknown_status)));
        values.put(WtaContract.ShowEntry.COLUMN_YEAR, receivedShow.getYear());
        values.put(WtaContract.ShowEntry.COLUMN_FIRST_AIRED, Utility.checkForNullValues(receivedShow.getFirst_aired(), null));
        values.put(WtaContract.ShowEntry.COLUMN_AIR_DAY, Utility.checkForNullValues(context, receivedShow.getAirs(),
                context.getString(R.string.unknown_air_info)));
        values.put(WtaContract.ShowEntry.COLUMN_RUNTIME, receivedShow.getRuntime());
        values.put(WtaContract.ShowEntry.COLUMN_NETWORK, Utility.checkForNullValues(receivedShow.getNetwork(),
                context.getString(R.string.unknown_network)));
        values.put(WtaContract.ShowEntry.COLUMN_COUNTRY, receivedShow.getCountry());
        values.put(WtaContract.ShowEntry.COLUMN_HOMEPAGE, receivedShow.getHomepage());
        values.put(WtaContract.ShowEntry.COLUMN_RATING, receivedShow.getRating());
        values.put(WtaContract.ShowEntry.COLUMN_VOTE_COUNT, receivedShow.getVotes());
        values.put(WtaContract.ShowEntry.COLUMN_LANGUAGE, Utility.checkForNullValues(receivedShow.getLanguage(),
                context.getString(R.string.unknown_language)));
        values.put(WtaContract.ShowEntry.COLUMN_AIRED_EPISODES, receivedShow.getAired_episodes());
        values.put(WtaContract.ShowEntry.COLUMN_WTA_UPDATE_DATE, now.getTime());
        if (score!=null) {
            values.put(WtaContract.ShowEntry.COLUMN_LAST_SEARCH_RESULT, 1);
            values.put(WtaContract.ShowEntry.COLUMN_SEARCH_SCORE, score);
        }
        if (popularity<=(Integer.valueOf(Utility.MAX_SHOWS))) {
            values.put(WtaContract.ShowEntry.COLUMN_POPULARITY, popularity);
        }
        List<String> showGenres = receivedShow.getGenres();
        if (showGenres!=null) {
            for (String genre : showGenres) {
                ContentValues relationValues = new ContentValues();
                relationValues.put(WtaContract.ShowEntry.COLUMN_SHOW_ID, receivedShow.getIds().getTrakt());
                relationValues.put(WtaContract.ShowEntry.COLUMN_GENRE_ID, genre);

                showGenreValues.add(relationValues);
            }
        }

        showsValues.add(values);
    }

    private static void processReceivedComments(Context context, List<Comment> receivedComments, Vector<ContentValues> commentsValues, Integer showId) {
        for (Comment comment: receivedComments) {
            ContentValues values = new ContentValues();
            values.put(WtaContract.CommentEntry._ID, comment.getId());
            values.put(WtaContract.CommentEntry.COLUMN_CREATED_AT, Utility.checkForNullValues(comment.getCreated_at(), null));
            values.put(WtaContract.CommentEntry.COLUMN_CONTENT, comment.getComment());
            values.put(WtaContract.CommentEntry.COLUMN_SPOILER, comment.isSpoiler());
            values.put(WtaContract.CommentEntry.COLUMN_REVIEW, comment.isReview());
            values.put(WtaContract.CommentEntry.COLUMN_LIKES, comment.getLikes());
            values.put(WtaContract.CommentEntry.COLUMN_USER, Utility.checkForNullValues(comment.getUser(),
                    context.getString(R.string.unknown_user)));
            values.put(WtaContract.CommentEntry.COLUMN_SHOW_ID, showId);

            commentsValues.add(values);
        }
    }

    private static Vector<Integer> processSeasonEpisodes(Context context, Integer seasonId, Integer seasonNumber, List<Episode> episodes, Vector<ContentValues> episodesValues) {
        Vector<Integer> receivedEpisodeNumbers = new Vector<>();

        if ((episodes!=null) && (!episodes.isEmpty())) {

            receivedEpisodeNumbers = new Vector<>(episodes.size());

            for (Episode episode : episodes) {
                ContentValues values = new ContentValues();
                values.put(WtaContract.EpisodeEntry._ID, episode.getIds().getTrakt());
                values.put(WtaContract.EpisodeEntry.COLUMN_NUMBER, episode.getNumber());
                values.put(WtaContract.EpisodeEntry.COLUMN_TITLE, Utility.checkForNullValues(episode.getTitle(),
                        context.getString(R.string.unknown_title)));
                values.put(WtaContract.EpisodeEntry.COLUMN_OVERVIEW, Utility.checkForNullValues(episode.getOverview(),
                        context.getString(R.string.unknown_overview)));
                values.put(WtaContract.EpisodeEntry.COLUMN_SCREENSHOT_PATH, Utility.checkForScreenshotNullValues(episode.getImages(), null));
                values.put(WtaContract.EpisodeEntry.COLUMN_FIRST_AIRED, Utility.checkForNullValues(episode.getFirst_aired(), null));
                values.put(WtaContract.EpisodeEntry.COLUMN_RATING, episode.getRating());
                values.put(WtaContract.EpisodeEntry.COLUMN_VOTE_COUNT, episode.getVotes());
                values.put(WtaContract.EpisodeEntry.COLUMN_SEASON_NUMBER, seasonNumber);
                values.put(WtaContract.EpisodeEntry.COLUMN_SEASON_ID, seasonId);

                receivedEpisodeNumbers.add(episode.getNumber());
                episodesValues.add(values);
            }
        }

        return receivedEpisodeNumbers;
    }

    private static void processEpisodeComments(Context context, String logTag, TraktService traktService, Integer showId, Integer seasonNumber,
                                        Integer episodeNumber, Vector<ContentValues> commentsValues) throws IOException {
        Call<List<Comment>> episodeComments = traktService.episodeComments(showId, seasonNumber, episodeNumber);
        Response<List<Comment>> episodesResponse = episodeComments.execute();
        if (episodesResponse.isSuccessful()) {
            List<Comment> receivedComments = episodesResponse.body();
            processReceivedComments(context, receivedComments, commentsValues, showId);
        } else {
            Log.e(logTag, "Error occurred calling episodeComments API endpoint: " + episodesResponse.message());
        }
    }

    private static void updateShowsDbInfo(Context context, Vector<ContentValues> showsValues, Vector<ContentValues> showGenreValues,
                                   Uri contentUri) {
        // Add to DB
        if (showsValues.size() > 0) {
            ContentValues[] insertValues = new ContentValues[showsValues.size()];
            showsValues.toArray(insertValues);
            context.getContentResolver()
                    .bulkInsert(contentUri, insertValues);
        }

        // Add to DB show-genre relations
        if (showGenreValues.size() > 0) {
            ContentValues[] insertRelationValues = new ContentValues[showGenreValues.size()];
            showGenreValues.toArray(insertRelationValues);
            context.getContentResolver()
                    .bulkInsert(WtaContract.ShowEntry.SHOW_GENRE_CONTENT_URI, insertRelationValues);
        }

        // Remove from DB old data (more than one month without being updated)
        // This way, we avoid building up an endless history
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        context.getContentResolver()
                .delete(WtaContract.ShowEntry.CONTENT_URI, WtaProvider.sShowsByUpdateDateSelection,
                        new String[] {Long.toString(cal.getTimeInMillis())});
    }

    private static String checkForNullValues(String value, String defaultValue) {
        if ((value != null) && (!value.isEmpty())) {
            return value;
        }
        return defaultValue;
    }

    private static Long checkForNullValues(Date date, Long defaultValue) {
        if (date != null) {
            return date.getTime();
        }
        return defaultValue;
    }

    private static String checkForNullValues(Context context, AirInfo airInfo, String defaultValue) {
        if (airInfo != null) {
            String day = airInfo.getDay();
            String time = airInfo.getTime();
            String timezone = airInfo.getTimezone();
            return ((day==null)?context.getString(R.string.unknown_air_day):day) + ", " +
                    ((time==null)?context.getString(R.string.unknown_air_time):time) + " (" +
                    ((timezone==null)?context.getString(R.string.unknown_air_timezone):Utility
                            .capitalizeAndFormatDelimiters(timezone,
                                    new char[]{' ','_','/','-'},
                                    new char[]{'/','-'})) + ")";
        }
        return defaultValue;
    }

    private static String checkForNullValues(User user, String defaultValue) {
        if (user != null){
            return user.getUsername();
        }
        return defaultValue;
    }

    private static String checkForPosterNullValues(ImageList imageList, String defaultValue) {
        if ((imageList != null) && (imageList.getPoster()!=null)){
            return imageList.getPoster().getFull();
        }
        return defaultValue;
    }

    private static String checkForBannerNullValues(ImageList imageList, String defaultValue) {
        if ((imageList != null) && (imageList.getBanner()!=null)){
            return imageList.getBanner().getFull();
        }
        return defaultValue;
    }

    private static String checkForHeadshotNullValues(ImageList imageList, String defaultValue) {
        if ((imageList != null) && (imageList.getHeadshot()!=null)){
            return imageList.getHeadshot().getFull();
        }
        return defaultValue;
    }

    private static String checkForScreenshotNullValues(ImageList imageList, String defaultValue) {
        if ((imageList != null) && (imageList.getScreenshot()!=null)){
            return imageList.getScreenshot().getFull();
        }
        return defaultValue;
    }

    private static String checkForThumbNullValues(ImageList imageList, String defaultValue) {
        if ((imageList != null) && (imageList.getThumb()!=null)){
            return imageList.getThumb().getFull();
        }
        return defaultValue;
    }
}
