package com.nanodegree.android.watchthemall.api.trakt;

/**
 * Class representing Trakt image info
 */
public class Image {

    private String full;

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    @Override
    public String toString() {
        return "Image{" +
                "full='" + full + '\'' +
                '}';
    }
}
