package app.android.rynz.catalogmovie.views;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import app.android.rynz.catalogmovie.models.MovieModel;

public interface TMDBPresenterView
{
    ArrayList<MovieModel> searchMovieByKeyword(@NonNull String keyword);
    MovieModel getMovieDetail(@NonNull String id);
}
