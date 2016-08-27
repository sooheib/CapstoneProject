package com.nanodegree.android.watchthemall.api.trakt;

import com.nanodegree.android.watchthemall.util.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface representing Trakt API endpoints used by WatchThemAll
 */
public interface TraktService {

    String FULL_IMAGES_EXTENDED_INFO_QUERY_PARAMETER = "extended=full,images";
    String IMAGES_EXTENDED_INFO_QUERY_PARAMETER = "extended=images";
    String FULL_IMAGES_EPISODES_EXTENDED_INFO_QUERY_PARAMETER = "extended=full,images,episodes";
    String SHOW_TYPE_QUERY_PARAMETER = "type=show";
    String WTA_CUSTOM_SHOWS_PAGINATION_PARAMETERS = "page=1&limit=" + Utility.MAX_SHOWS;
    String WTA_CUSTOM_COMMENTS_PAGINATION_PARAMETERS = "page=1&limit=" + Utility.MAX_COMMENTS;

    @Headers({
            "Content-type: application/json",
            "trakt-api-version: 2"
    })
    @GET("genres/shows")
    Call<List<Genre>> showGenres();

    @Headers({
            "Content-type: application/json",
            "trakt-api-version: 2"
    })
    @GET("search?" + SHOW_TYPE_QUERY_PARAMETER + "&" + WTA_CUSTOM_SHOWS_PAGINATION_PARAMETERS)
    Call<List<SearchResult>> searchShowsByKeywords(@Query("query") String keywords, @Query("year") Integer year);

    @Headers({
            "Content-type: application/json",
            "trakt-api-version: 2"
    })
    @GET("shows/{id}?" + FULL_IMAGES_EXTENDED_INFO_QUERY_PARAMETER)
    Call<Show> showSummary(@Path("id") int showId);

    @Headers({
            "Content-type: application/json",
            "trakt-api-version: 2"
    })
    @GET("shows/popular?" + FULL_IMAGES_EXTENDED_INFO_QUERY_PARAMETER + "&" + WTA_CUSTOM_SHOWS_PAGINATION_PARAMETERS)
    Call<List<Show>> popularShows();

    @Headers({
            "Content-type: application/json",
            "trakt-api-version: 2"
    })
    @GET("shows/{id}/comments?" + WTA_CUSTOM_COMMENTS_PAGINATION_PARAMETERS)
    Call<List<Comment>> showComments(@Path("id") int showId);

    @Headers({
            "Content-type: application/json",
            "trakt-api-version: 2"
    })
    @GET("shows/{id}/people?" + IMAGES_EXTENDED_INFO_QUERY_PARAMETER)
    Call<Cast> showPeople(@Path("id") int showId);

    @Headers({
            "Content-type: application/json",
            "trakt-api-version: 2"
    })
    @GET("shows/{id}/seasons?" + FULL_IMAGES_EPISODES_EXTENDED_INFO_QUERY_PARAMETER)
    Call<List<Season>> seasonsSummary(@Path("id") int showId);

    @Headers({
            "Content-type: application/json",
            "trakt-api-version: 2"
    })
    @GET("shows/{id}/seasons/{season}/episodes/{episode}/comments?" + WTA_CUSTOM_COMMENTS_PAGINATION_PARAMETERS)
    Call<List<Comment>> episodeComments(@Path("id") int showId, @Path("season") int season, @Path("episode") int episode);
}
