package com.nanodegree.android.watchthemall.api.trakt;

import com.nanodegree.android.watchthemall.util.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class representing Trakt episode info
 */
public class Episode {

    private int number;
    private String title;
    private String overview;
    private ImageList images;
    private IdList ids;
    private Date first_aired;
    private double rating;
    private int votes;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public ImageList getImages() {
        return images;
    }

    public void setImages(ImageList images) {
        this.images = images;
    }

    public IdList getIds() {
        return ids;
    }

    public void setIds(IdList ids) {
        this.ids = ids;
    }

    public Date getFirst_aired() {
        return first_aired;
    }

    public void setFirst_aired(Date first_aired) {
        this.first_aired = first_aired;
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

    @Override
    public String toString() {
        return "Episode{" +
                "number=" + number +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", images=" + ((images==null)?null:images.toString()) +
                ", ids=" + ((ids==null)?null:ids.toString()) +
                ", first_aired=" + ((first_aired==null)?null:new SimpleDateFormat(Utility.MONTH_DAY_YEAR_COMPLETE_HOUR_PATTERN).format(first_aired)) +
                ", rating=" + rating +
                ", votes=" + votes +
                '}';
    }
}
