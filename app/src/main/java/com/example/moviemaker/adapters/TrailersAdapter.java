package com.example.moviemaker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviemaker.R;
import com.example.moviemaker.data.models.Trailer;

import java.util.ArrayList;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {
  private ArrayList<Trailer> trailers;
  private OnTrailerClickListener onTrailerClickListener;

  public void setTrailerOnClickListener(OnTrailerClickListener trailerOnClickListener) {
    this.onTrailerClickListener = trailerOnClickListener;
  }

  public interface OnTrailerClickListener {
    void onTrailerClick(String url);
  }

  public void setTrailers(ArrayList<Trailer> trailers) {
    this.trailers = trailers;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
    return new TrailerViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
    Trailer trailer = trailers.get(position);
    holder.textViewVideoName.setText(trailer.getName());
  }

  @Override
  public int getItemCount() {
    return trailers.size();
  }

  class TrailerViewHolder extends RecyclerView.ViewHolder {
    private TextView textViewVideoName;

    public TrailerViewHolder(@NonNull View itemView) {
      super(itemView);
      textViewVideoName = itemView.findViewById(R.id.textViewNameOfVideo);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if(onTrailerClickListener != null) {
            onTrailerClickListener.onTrailerClick(trailers.get(getAdapterPosition()).getKey());
          }
        }
      });
    }
  }
}