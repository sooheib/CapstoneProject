package com.nanodegree.android.watchthemall.api.trakt;

/**
 * Class representing Trakt image list
 */
public class ImageList {

    private Image screenshot;
    private Image headshot;
    private Image poster;
    private Image banner;
    private Image thumb;

    public Image getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(Image screenshot) {
        this.screenshot = screenshot;
    }

    public Image getHeadshot() {
        return headshot;
    }

    public void setHeadshot(Image headshot) {
        this.headshot = headshot;
    }

    public Image getPoster() {
        return poster;
    }

    public void setPoster(Image poster) {
        this.poster = poster;
    }

    public Image getBanner() {
        return banner;
    }

    public void setBanner(Image banner) {
        this.banner = banner;
    }

    public Image getThumb() {
        return thumb;
    }

    public void setThumb(Image thumb) {
        this.thumb = thumb;
    }

    @Override
    public String toString() {
        return "ImageList{" +
                "screenshot=" + ((screenshot==null)?null:screenshot.toString()) +
                ", headshot=" + ((headshot==null)?null:headshot.toString()) +
                ", poster=" + ((poster==null)?null:poster.toString()) +
                ", banner=" + ((banner==null)?null:banner.toString()) +
                ", thumb=" + ((thumb==null)?null:thumb.toString()) +
                '}';
    }
}
