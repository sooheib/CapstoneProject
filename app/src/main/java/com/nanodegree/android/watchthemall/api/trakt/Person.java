package com.nanodegree.android.watchthemall.api.trakt;

/**
 * Class representing Trakt person info
 */
public class Person {

    private String name;
    private ImageList images;
    private IdList ids;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", images=" + ((images==null)?null:images.toString()) +
                ", ids=" + ((ids==null)?null:ids.toString()) +
                '}';
    }
}
