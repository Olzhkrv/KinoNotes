package com.example.moviemaker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.moviemaker.FavouriteActivity;
import com.example.moviemaker.data.models.Movie;
import com.example.moviemaker.data.models.MovieFavourite;

import java.util.List;

@Dao
public interface MovieDao {
  @Query("SELECT * FROM movies")
  LiveData<List<Movie>> getAllMovies();

  @Query("SELECT * FROM movies WHERE id == :movieId")
  Movie getMovieById(int movieId);

  @Query("DELETE FROM movies")
  void deleteAllMovies();

  @Insert
  void insertMovie(Movie movie);

  @Delete
  void deleteMovie(Movie movie);

  @Query("SELECT * FROM favourite_movies")
  LiveData<List<MovieFavourite>> getAllFavMovies();

  @Query("SELECT * FROM favourite_movies WHERE id == :movieId")
  MovieFavourite getFavMovieById(int movieId);

  @Insert
  void insertFavMovie(MovieFavourite movie);

  @Delete
  void deleteFavMovie(MovieFavourite movie);
}
