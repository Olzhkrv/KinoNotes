package com.example.moviemaker.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.moviemaker.data.models.Movie;
import com.example.moviemaker.data.models.MovieFavourite;

@Database(entities = {Movie.class, MovieFavourite.class}, version = 4, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

  private static final String DB_NAME = "movies.db";
  private static MovieDatabase database;
  private static final Object LOCK = new Object();

  public static MovieDatabase getInstance(Context context) {
    synchronized (LOCK) {
      if (database == null) {
        database = Room.databaseBuilder(context, MovieDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
      }
      return database;
    }
  }

  public abstract MovieDao movieDao();
}