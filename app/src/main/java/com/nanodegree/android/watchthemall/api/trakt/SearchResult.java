package com.nanodegree.android.watchthemall.api.trakt;

/**
 * Class representing Trakt search result info
 */
public class SearchResult {

    private String type;
    private double score;
    private Show show;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "type='" + type + '\'' +
                ", score=" + score +
                ", show=" + ((show==null)?null:show.toString()) +
                '}';
    }
}
