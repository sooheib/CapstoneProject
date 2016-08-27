package com.nanodegree.android.watchthemall.api.trakt;

import com.nanodegree.android.watchthemall.util.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Class representing Trakt show info
 */
public class Show {

    private String title;
    private int year;
    private IdList ids;
    private String overview;
    private Date first_aired;
    private AirInfo airs;
    private int runtime;
    private String network;
    private String country;
    private String homepage;
    private String status;
    private double rating;
    private int votes;
    private String language;
    private List<String> genres;
    private int aired_episodes;
    private ImageList images;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public IdList getIds() {
        return ids;
    }

    public void setIds(IdList ids) {
        this.ids = ids;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getFirst_aired() {
        return first_aired;
    }

    public void setFirst_aired(Date first_aired) {
        this.first_aired = first_aired;
    }

    public AirInfo getAirs() {
        return airs;
    }

    public void setAirs(AirInfo airs) {
        this.airs = airs;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getAired_episodes() {
        return aired_episodes;
    }

    public void setAired_episodes(int aired_episodes) {
        this.aired_episodes = aired_episodes;
    }

    public ImageList getImages() {
        return images;
    }

    public void setImages(ImageList images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Show{" +
                "title='" + title + '\'' +
                ", year=" + year +
                ", ids=" + ((ids==null)?null:ids.toString()) +
                ", overview='" + overview + '\'' +
                ", first_aired=" + ((first_aired==null)?null:new SimpleDateFormat(Utility.MONTH_DAY_YEAR_COMPLETE_HOUR_PATTERN).format(first_aired)) +
                ", airs=" + ((airs==null)?null:airs.toString()) +
                ", runtime=" + runtime +
                ", network='" + network + '\'' +
                ", country='" + country + '\'' +
                ", homepage='" + homepage + '\'' +
                ", status='" + status + '\'' +
                ", rating=" + rating +
                ", votes=" + votes +
                ", language='" + language + '\'' +
                ", genres=" + genres +
                ", aired_episodes=" + aired_episodes +
                ", images=" + ((images==null)?null:images.toString()) +
                '}';
    }
}
