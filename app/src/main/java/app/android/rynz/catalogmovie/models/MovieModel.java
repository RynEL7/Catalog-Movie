package app.android.rynz.catalogmovie.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieModel implements Parcelable
{

    private String id,title,poster,overview,releaseDate;

    public MovieModel(String id, String title, String poster, String overview, String releaseDate)
    {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    public String getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getPoster()
    {
        return poster;
    }

    public String getOverview()
    {
        return overview;
    }

    public String getReleaseDate()
    {
        return releaseDate;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setPoster(String poster)
    {
        this.poster = poster;
    }

    public void setOverview(String overview)
    {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.poster);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
    }

    protected MovieModel(Parcel in)
    {
        this.id = in.readString();
        this.title = in.readString();
        this.poster = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
    }

    public static final Parcelable.Creator<MovieModel> CREATOR = new Parcelable.Creator<MovieModel>()
    {
        @Override
        public MovieModel createFromParcel(Parcel source)
        {
            return new MovieModel(source);
        }

        @Override
        public MovieModel[] newArray(int size)
        {
            return new MovieModel[size];
        }
    };
}
