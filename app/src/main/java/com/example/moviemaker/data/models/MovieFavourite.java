package com.example.moviemaker.data.models;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favourite_movies")
public class MovieFavourite extends Movie {

  public MovieFavourite(int uniqueId, int id, int voteCount, String title,
                        String originalTitle, String overview,
                        String posterPuth, String logoPosterPuth,
                        String backDropPath, String bigPosterPath,
                        double voteAverage, String releaseDate) {
    super(uniqueId, id, voteCount, title, originalTitle, overview, posterPuth,
            logoPosterPuth, backDropPath,
            bigPosterPath, voteAverage, releaseDate);
  }

  @Ignore
  public MovieFavourite(Movie movie) {
    super(movie.getUniqueId(), movie.getId(), movie.getVoteCount(),
            movie.getTitle(), movie.getOriginalTitle(),
            movie.getOverview(), movie.getPosterPuth(),
            movie.getLogoPosterPuth(), movie.getBackDropPath(),
            movie.getBigPosterPath(), movie.getVoteAverage(),
            movie.getReleaseDate());
  }

}
