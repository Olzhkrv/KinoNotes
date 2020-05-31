package com.example.moviemaker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviemaker.R;
import com.example.moviemaker.data.models.Movie;
import com.example.moviemaker.data.models.MovieFavourite;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavMovieAdapter extends RecyclerView.Adapter<FavMovieAdapter.MovieFavViewHolder>{
  private List<Movie> movies;
  private OnFavPosterClickListener onFavPosterClickListener;

  public void setOnFavPosterClickListener(OnFavPosterClickListener onFavPosterClickListener) {
    this.onFavPosterClickListener = onFavPosterClickListener;
  }

  public interface OnFavPosterClickListener {
    void onFavPosterClick(int position);
  }

  public FavMovieAdapter() {
    this.movies = new ArrayList<>();
  }

  public List<Movie> getMovies() {
    return movies;
  }

  public void setMovies(List<Movie> movies) {
    this.movies = movies;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public MovieFavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_fav_item, parent, false);
    return new MovieFavViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull MovieFavViewHolder holder, int position) {
    Movie movie = movies.get(position);
    Picasso.get().load(movie.getLogoPosterPuth()).into(holder.imageViewLogoPoster);
    holder.textViewFilmTitle.setText(movie.getTitle());
    holder.textViewFilmRate.setText(Double.toString(movie.getVoteAverage()));
  }

  @Override
  public int getItemCount() {
    return movies.size();
  }

  class MovieFavViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageViewLogoPoster;
    private TextView textViewFilmTitle;
    private TextView textViewFilmRate;
    public MovieFavViewHolder(@NonNull View itemView) {
      super(itemView);
      imageViewLogoPoster = itemView.findViewById(R.id.logoPoster);
      textViewFilmTitle = itemView.findViewById(R.id.textViewFavTitle);
      textViewFilmRate = itemView.findViewById(R.id.textViewFavRate);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if(onFavPosterClickListener != null) onFavPosterClickListener.onFavPosterClick(getAdapterPosition());
        }
      });
    }
  }
}
