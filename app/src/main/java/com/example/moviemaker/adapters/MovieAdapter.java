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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
  private List<Movie> movies;
  private OnPosterClickListener onPosterClickListener;
  private OnRichOnListener onRichOnListener;

  public MovieAdapter() {
    this.movies = new ArrayList<>();
  }

  public void setOnRichOnListener(OnRichOnListener onRichOnListener) {
    this.onRichOnListener = onRichOnListener;
  }

  public List<Movie> getMovies() {
    return movies;
  }

  public void addMovies(List<Movie> movies) {
    this.movies.addAll(movies);
    notifyDataSetChanged();
  }

  public void setMoviesList(List<Movie> moviesList) {
    this.movies = moviesList;
    notifyDataSetChanged();
  }

  public interface OnPosterClickListener {
    void onPosterClick(int position);
  }

  public interface OnRichOnListener {
    void onReachEnd();
  }

  public void setOnPosterClickListener(OnPosterClickListener onPosterClickListener) {
    this.onPosterClickListener = onPosterClickListener;
  }

  @NonNull
  @Override
  public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
    return new MovieViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
    Movie movie = movies.get(position);
    Picasso.get().load(movie.getPosterPuth()).into(holder.smallPoster);

//    Picasso.get().load(movie.getLogoPosterPuth()).into(holder.imageViewLogoPoster);
//    holder.textViewFilmTitle.setText(movie.getTitle());
//    holder.textViewFilmRate.setText(Double.toString(movie.getVoteAverage()));

    if(movies.size() >= 20 && position > movies.size() - 4 && onRichOnListener != null)
      onRichOnListener.onReachEnd();
  }

  @Override
  public int getItemCount() {
    return movies.size();
  }

  public void clear() {
    this.movies.clear();
    notifyDataSetChanged();
  }


  class MovieViewHolder extends RecyclerView.ViewHolder {
    private ImageView smallPoster;

//    private ImageView imageViewLogoPoster;
//    private TextView textViewFilmTitle;
//    private TextView textViewFilmRate;

    public MovieViewHolder(@NonNull View itemView) {
      super(itemView);

//      imageViewLogoPoster = itemView.findViewById(R.id.logoPoster);
//      textViewFilmTitle = itemView.findViewById(R.id.textViewFavTitle);
//      textViewFilmRate = itemView.findViewById(R.id.textViewFavRate);

      smallPoster = itemView.findViewById(R.id.ImageViewSmallPoster);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if(onPosterClickListener != null) onPosterClickListener.onPosterClick(getAdapterPosition());
        }
      });
    }
  }
}
