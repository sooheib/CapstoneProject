package com.nanodegree.android.watchthemall.api.trakt;

import java.util.List;

/**
 * Class representing Trakt cast info
 */
public class Cast {

    private List<Role> cast;

    public List<Role> getCast() {
        return cast;
    }

    public void setCast(List<Role> cast) {
        this.cast = cast;
    }

    @Override
    public String toString() {
        return "Cast{" +
                "cast=" + cast +
                '}';
    }
}
