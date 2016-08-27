package com.nanodegree.android.watchthemall.api.trakt;

import com.nanodegree.android.watchthemall.util.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class representing Trakt comment info (both for shows and episodes)
 */
public class Comment {

    private int id;
    private Date created_at;
    private String comment;
    private boolean spoiler;
    private boolean review;
    private int likes;
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isSpoiler() {
        return spoiler;
    }

    public void setSpoiler(boolean spoiler) {
        this.spoiler = spoiler;
    }

    public boolean isReview() {
        return review;
    }

    public void setReview(boolean review) {
        this.review = review;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", created_at=" + ((created_at==null)?null:new SimpleDateFormat(Utility.MONTH_DAY_YEAR_COMPLETE_HOUR_PATTERN).format(created_at)) +
                ", comment='" + comment + '\'' +
                ", spoiler=" + spoiler +
                ", review=" + review +
                ", likes=" + likes +
                ", user=" + ((user==null)?null:user.toString()) +
                '}';
    }
}
