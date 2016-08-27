package com.nanodegree.android.watchthemall.api.trakt;

import com.nanodegree.android.watchthemall.util.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Class representing Trakt season info
 */
public class Season {

    private int number;
    private IdList ids;
    private int episode_count;
    private int aired_episodes;
    private Date first_aired;
    private List<Episode> episodes;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public IdList getIds() {
        return ids;
    }

    public void setIds(IdList ids) {
        this.ids = ids;
    }

    public int getEpisode_count() {
        return episode_count;
    }

    public void setEpisode_count(int episode_count) {
        this.episode_count = episode_count;
    }

    public int getAired_episodes() {
        return aired_episodes;
    }

    public void setAired_episode(int aired_episode) {
        this.aired_episodes = aired_episode;
    }

    public Date getFirst_aired() {
        return first_aired;
    }

    public void setFirst_aired(Date first_aired) {
        this.first_aired = first_aired;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    @Override
    public String toString() {
        return "Season{" +
                "number=" + number +
                ", ids=" + ((ids==null)?null:ids.toString()) +
                ", episode_count=" + episode_count +
                ", aired_episodes=" + aired_episodes +
                ", first_aired=" + ((first_aired==null)?null:new SimpleDateFormat(Utility.MONTH_DAY_YEAR_COMPLETE_HOUR_PATTERN).format(first_aired)) +
                '}';
    }
}
