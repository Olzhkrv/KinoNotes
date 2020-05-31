package com.example.moviemaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.moviemaker.adapters.FavMovieAdapter;
import com.example.moviemaker.adapters.MovieAdapter;
import com.example.moviemaker.data.MainViewModel;
import com.example.moviemaker.data.models.Movie;
import com.example.moviemaker.data.models.MovieFavourite;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

  private RecyclerView recyclerViewFavMovies;
  private FavMovieAdapter favMovieAdapter;
  private MainViewModel mainViewModel;
  private List<Movie> movies;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.itemMain:
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        break;
      case R.id.itemFav:
        Intent intentToFav = new Intent(this, FavouriteActivity.class);
        startActivity(intentToFav);
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favourite);
    recyclerViewFavMovies = findViewById(R.id.recyclerViewFavouriteMovies);
    favMovieAdapter = new FavMovieAdapter();
    recyclerViewFavMovies.setLayoutManager(new LinearLayoutManager(this));
    recyclerViewFavMovies.setAdapter(favMovieAdapter);
    mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    movies = new ArrayList<>();

    LiveData<List<MovieFavourite>> favMovies = mainViewModel.getMovieFavourite();
    favMovies.observe(this, new Observer<List<MovieFavourite>>() {
      @Override
      public void onChanged(List<MovieFavourite> movieFavouriteFromLiveData) {
        if (movieFavouriteFromLiveData != null) {
          movies.addAll(movieFavouriteFromLiveData);
          favMovieAdapter.setMovies(movies);
        }
      }
    });

    favMovieAdapter.setOnFavPosterClickListener(new FavMovieAdapter.OnFavPosterClickListener() {
      @Override
      public void onFavPosterClick(int position) {
        MovieFavourite movieFavourite = (MovieFavourite) favMovieAdapter.getMovies().get(position);
        Intent intent = new Intent(FavouriteActivity.this, DetailActivity.class);
        intent.putExtra("id", movieFavourite.getId());
        startActivity(intent);
      }
    });
  }
}
