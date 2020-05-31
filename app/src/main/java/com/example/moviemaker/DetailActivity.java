package com.example.moviemaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviemaker.adapters.ReviewsAdapter;
import com.example.moviemaker.adapters.TrailersAdapter;
import com.example.moviemaker.data.MainViewModel;
import com.example.moviemaker.data.models.Movie;
import com.example.moviemaker.data.models.MovieFavourite;
import com.example.moviemaker.data.models.Review;
import com.example.moviemaker.data.models.Trailer;
import com.example.moviemaker.utils.JsonUtils;
import com.example.moviemaker.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

  private ImageView imageViewStar;
  private ImageView imageViewBigPoster;
  private TextView textViewTitle;
  private TextView textViewRating;
  private TextView textViewReleaseDate;
  private TextView textViewDescription;

  private RecyclerView recyclerViewReviews;
  private RecyclerView recyclerViewTrailers;

  private ReviewsAdapter reviewsAdapter;
  private TrailersAdapter trailersAdapter;

  private int id;
  private MainViewModel mainViewModel;
  private Movie movie;
  private MovieFavourite movieFavourite;

  private ArrayList<Trailer> trailers;
  private ArrayList<Review> reviews;

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
    setContentView(R.layout.activity_detail);

    imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
    textViewTitle = findViewById(R.id.textViewTitle);
    textViewRating = findViewById(R.id.textViewRate);
    textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
    textViewDescription = findViewById(R.id.textViewDescription);
    imageViewStar = findViewById(R.id.imageStar);

    Intent intent = getIntent();
    if (intent != null && intent.hasExtra("id")) {
      id = intent.getIntExtra("id", -1);
    } else {
      finish();
    }

    mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    movie = mainViewModel.getMovieById(id);
    Picasso.get().load(movie.getBigPosterPath()).into(imageViewBigPoster);
    textViewTitle.setText(movie.getTitle());
    textViewRating.setText(Double.toString(movie.getVoteAverage()));
    textViewReleaseDate.setText(movie.getReleaseDate());
    textViewDescription.setText(movie.getOverview());
    setFavourite();

    recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
    reviewsAdapter = new ReviewsAdapter();
    recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
    recyclerViewReviews.setAdapter(reviewsAdapter);
    JSONObject jsonObjectReview = NetworkUtils.JSONObjectForReviews(movie.getId());
    reviews = JsonUtils.getReviewFromJSON(jsonObjectReview);
    reviewsAdapter.setReviews(reviews);

    recyclerViewTrailers = findViewById(R.id.recyclerViewTrailers);
    trailersAdapter = new TrailersAdapter();
    recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));
    recyclerViewTrailers.setAdapter(trailersAdapter);
    JSONObject jsonObjectTrailers = NetworkUtils.JSONObjectForVideos(movie.getId());
    trailers = JsonUtils.getVideoFromJSON(jsonObjectTrailers);
    trailersAdapter.setTrailers(trailers);
    trailersAdapter.setTrailerOnClickListener(new TrailersAdapter.OnTrailerClickListener() {
      @Override
      public void onTrailerClick(String url) {
        Intent intentToYoutube = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intentToYoutube);
      }
    });
  }

  public void changeFavourite(View view) {
    if(movieFavourite == null) {
      mainViewModel.insterFavMovie(new MovieFavourite(movie));
      Toast.makeText(this, R.string.added_to_fav, Toast.LENGTH_SHORT).show();
    } else {
      mainViewModel.deleteFavMovie(movieFavourite);
      Toast.makeText(this, R.string.deleted_from_fav, Toast.LENGTH_SHORT).show();
    }
    setFavourite();
  }

  private void setFavourite() {
    movieFavourite = mainViewModel.getFavMovieById(id);
    if (movieFavourite == null) {
      imageViewStar.setImageResource(R.drawable.ic_star_gray);
    } else {
      imageViewStar.setImageResource(R.drawable.ic_star_yellow);
    }
  }
}
