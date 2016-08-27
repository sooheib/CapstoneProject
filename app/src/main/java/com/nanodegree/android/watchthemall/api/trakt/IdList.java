package com.nanodegree.android.watchthemall.api.trakt;

/**
 * Class representing Trakt id list
 */
public class IdList {

    private int trakt;
    private String slug;
    private int tvdb;
    private int tmdb;
    private String imdb;

    public int getTrakt() {
        return trakt;
    }

    public void setTrakt(int trakt) {
        this.trakt = trakt;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getTvdb() {
        return tvdb;
    }

    public void setTvdb(int tvdb) {
        this.tvdb = tvdb;
    }

    public int getTmdb() {
        return tmdb;
    }

    public void setTmdb(int tmdb) {
        this.tmdb = tmdb;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    @Override
    public String toString() {
        return "IdList{" +
                "trakt=" + trakt +
                ", slug='" + slug + '\'' +
                ", tvdb=" + tvdb +
                ", tmdb=" + tmdb +
                ", imdb='" + imdb + '\'' +
                '}';
    }
}
