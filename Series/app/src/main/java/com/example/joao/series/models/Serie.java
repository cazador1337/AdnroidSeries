package com.example.joao.series.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.joao.api.banco.annotations.Attribute;
import com.joao.api.banco.annotations.CharField;
import com.joao.api.banco.annotations.PrimaryKey;
import com.joao.api.banco.annotations.Table;
import com.joao.api.banco.core.AbstractModel;

/**
 * Created by joao on 13/06/2016.
 */
public class Serie extends AbstractModel<Serie> implements Parcelable {
    public static final Serie.Objects<Serie> objects = (Objects<Serie>) AbstractModel.objects;

    public static final String ID = "id", TITLE = "title",
            GENRES = "genres", STATUS = "status",
            SCORE = "score", EPISODES = "episodes", POSITION = "postion";

    @Attribute(column = ID, type = int.class)
    @PrimaryKey(auto_generated = true)
    private int id;

    @Attribute(column = TITLE, type = String.class)
    @CharField(max_length = 50)
    private String title;

    @Attribute(column = GENRES, type = String.class)
    @CharField(max_length = 100)
    private String genres;

    @Attribute(column = STATUS, type = int.class)
    private int status;

    @Attribute(column = SCORE, type = float.class)
    private float score;

    @Attribute(column = EPISODES, type = int.class)
    private int episodes;

    @Attribute(column = POSITION, type = int.class)
    private int position;

    @Table(name = "serie")
    public Serie() {
        super(Serie.class);
    }

    protected Serie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        genres = in.readString();
        status = in.readInt();
        score = in.readFloat();
        episodes = in.readInt();
        position = in.readInt();
    }

    public static final Creator<Serie> CREATOR = new Creator<Serie>() {
        @Override
        public Serie createFromParcel(Parcel in) {
            return new Serie(in);
        }

        @Override
        public Serie[] newArray(int size) {
            return new Serie[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(genres);
        dest.writeInt(status);
        dest.writeFloat(score);
        dest.writeInt(episodes);
        dest.writeInt(position);
    }
}
