package com.example.moviemaker.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.moviemaker.data.models.Movie;
import com.example.moviemaker.data.models.MovieFavourite;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {

  private static MovieDatabase database;
  private LiveData<List<Movie>> movies;
  private LiveData<List<MovieFavourite>> movieFavourite;

  public MainViewModel(Application application) {
    super(application);
    database = MovieDatabase.getInstance(getApplication());
    movies = database.movieDao().getAllMovies();
    movieFavourite = database.movieDao().getAllFavMovies();
  }


  //Methods for just movies
  public Movie getMovieById(int id) {
    try {
      return new GetMovieTask().execute(id).get();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void deleteAllMovies() {
    new DeleteMoviesTask().execute();
  }

  public void insterMovie(Movie movie) {
    new InsertMovieTask().execute(movie);
  }

  public void deleteMovie(Movie movie) {
    new DeleteMovieTask().execute(movie);
  }

  public LiveData<List<Movie>> getMovies() {
    return movies;
  }

  private static class GetMovieTask extends AsyncTask<Integer, Void, Movie> {
    @Override
    protected Movie doInBackground(Integer... integers) {
      if(integers != null && integers.length > 0) {
        return database.movieDao().getMovieById(integers[0]);
      }
      return null;
    }
  }

  private static class DeleteMoviesTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... integers) {
      database.movieDao().deleteAllMovies();
      return null;
    }
  }

  private static class InsertMovieTask extends AsyncTask<Movie, Void, Void> {
    @Override
    protected Void doInBackground(Movie... movies) {
      if(movies != null && movies.length > 0) {
        database.movieDao().insertMovie(movies[0]);
      }
      return null;
    }
  }

  private static class DeleteMovieTask extends AsyncTask<Movie, Void, Void> {
    @Override
    protected Void doInBackground(Movie... movies) {
      if(movies != null && movies.length > 0) {
        database.movieDao().deleteMovie(movies[0]);
      }
      return null;
    }
  }

  //methods for favMovies

  public LiveData<List<MovieFavourite>> getMovieFavourite() {
    return movieFavourite;
  }

  public MovieFavourite getFavMovieById(int id) {
    try {
      return new GetFavMovieTask().execute(id).get();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void insterFavMovie(MovieFavourite movie) {
    new InsertFavMovieTask().execute(movie);
  }

  public void deleteFavMovie(MovieFavourite movie) {
    new DeleteFavMovieTask().execute(movie);
  }

  private static class InsertFavMovieTask extends AsyncTask<MovieFavourite, Void, MovieFavourite> {
    @Override
    protected MovieFavourite doInBackground(MovieFavourite... movieFavourites) {
      if (movieFavourites != null && movieFavourites.length > 0) {
        database.movieDao().insertFavMovie(movieFavourites[0]);
      }
      return null;
    }
  }

  private static class DeleteFavMovieTask extends AsyncTask<MovieFavourite, Void, Void> {
    @Override
    protected Void doInBackground(MovieFavourite... movieFavourites) {
      if (movieFavourites != null && movieFavourites.length > 0) {
        database.movieDao().deleteFavMovie(movieFavourites[0]);
      }
      return null;
    }
  }

  private static class GetFavMovieTask extends AsyncTask<Integer, Void, MovieFavourite> {
    @Override
    protected MovieFavourite doInBackground(Integer... integers) {
      if(integers != null && integers.length > 0) {
        return database.movieDao().getFavMovieById(integers[0]);
      }
      return null;
    }
  }

}
