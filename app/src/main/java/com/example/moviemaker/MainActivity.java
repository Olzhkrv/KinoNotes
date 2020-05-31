package com.example.moviemaker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviemaker.adapters.MovieAdapter;
import com.example.moviemaker.data.MainViewModel;
import com.example.moviemaker.data.models.Movie;
import com.example.moviemaker.utils.JsonUtils;
import com.example.moviemaker.utils.NetworkUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {
  private Switch switchSort;
  private RecyclerView recyclerViewPoster;
  private MovieAdapter movieAdapter;
  private TextView textViewTopRated;
  private TextView textViewPopularity;

  private MainViewModel mainViewModel;

  private static final int LOADER_ID = 133;
  private LoaderManager loaderManager;

  private static int page = 1;
  private static int methodOfSort;
  private static boolean isLoading = false;

  ProgressBar progressBarLoading;

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

  private int getColumnCount() {
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
    return width / 185 > 2 ? width / 185 : 2;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    loaderManager = LoaderManager.getInstance(this);

    mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

    textViewPopularity = findViewById(R.id.textViewPopular);
    textViewTopRated = findViewById(R.id.textViewTopRated);

    progressBarLoading = findViewById(R.id.progressBarLoading);

    switchSort = findViewById(R.id.switchSort);
    recyclerViewPoster = findViewById(R.id.recyclerViewPosters);
    recyclerViewPoster.setLayoutManager(new GridLayoutManager(this, getColumnCount()));
    movieAdapter = new MovieAdapter();
    switchSort.setChecked(true);
    recyclerViewPoster.setAdapter(movieAdapter);
    switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        page = 1;
        switchSort(b);
      }
    });
    switchSort.setChecked(false);
    movieAdapter.setOnPosterClickListener(new MovieAdapter.OnPosterClickListener() {
      @Override
      public void onPosterClick(int position) {
        Movie movie = movieAdapter.getMovies().get(position);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("id", movie.getId());
        startActivity(intent);
      }
    });
    movieAdapter.setOnRichOnListener(new MovieAdapter.OnRichOnListener() {
      @Override
      public void onReachEnd() {
        if(!isLoading) {
          downloadData(methodOfSort, page);
        }
      }
    });
    LiveData<List<Movie>> moviesFromLiveData = mainViewModel.getMovies();
    moviesFromLiveData.observe(this, new Observer<List<Movie>>() {
      @Override
      public void onChanged(List<Movie> movies) {
        if(page == 1) {
          movieAdapter.setMoviesList(movies);
        }
      }
    });
  }

  public void onClickSetTopRated(View view) {
    switchSort(true);
    switchSort.setChecked(true);
  }

  public void onClickSetPopularity(View view) {
    switchSort(false);
    switchSort.setChecked(false);
  }

  public void switchSort(boolean isTopRated) {
    if(isTopRated) {
      methodOfSort = NetworkUtils.TOP_RATED;
      textViewTopRated.setTextColor(getResources().getColor(R.color.colorAccent));
      textViewPopularity.setTextColor(getResources().getColor(R.color.colorWhite));
    } else {
      methodOfSort = NetworkUtils.POPULARITY;
      textViewTopRated.setTextColor(getResources().getColor(R.color.colorWhite));
      textViewPopularity.setTextColor(getResources().getColor(R.color.colorAccent));
    }
    downloadData(methodOfSort, page);
  }

  private void downloadData(int methodOfSort, int page) {
    URL url = NetworkUtils.buildURL(methodOfSort, page);
    Bundle bundle = new Bundle();
    bundle.putString("url", url.toString());
    loaderManager.restartLoader(LOADER_ID, bundle, this);
  }

  @NonNull
  @Override
  public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle bundle) {
    NetworkUtils.JSONLoader jsonLoaders = new NetworkUtils.JSONLoader(this, bundle);
    jsonLoaders.setOnStartLoadingListener(new NetworkUtils.JSONLoader.OnStartLoadingListener() {
      @Override
      public void onStartLoading() {
        progressBarLoading.setVisibility(View.VISIBLE);
        isLoading = true;
      }
    });
    return jsonLoaders;
  }

  @Override
  public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject jsonObject) {
    ArrayList<Movie> movies = JsonUtils.getMoviesFromJSON(jsonObject);
    if(movies != null && !movies.isEmpty()) {
      if(page == 1) {
        mainViewModel.deleteAllMovies();
        movieAdapter.clear();
      }
      for(Movie movie : movies) {
        mainViewModel.insterMovie(movie);
      }
      movieAdapter.addMovies(movies);
      page++;
    }
    isLoading = false;
    loaderManager.destroyLoader(LOADER_ID);
    progressBarLoading.setVisibility(View.INVISIBLE);
  }

  @Override
  public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

  }
}
