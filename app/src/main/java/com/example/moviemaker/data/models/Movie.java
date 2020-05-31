package com.example.moviemaker.data.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie {
  @PrimaryKey(autoGenerate = true)
  private int uniqueId;
  private int id;
  private int voteCount;
  private String title;
  private String originalTitle;
  private String overview;
  private String posterPuth;
  private String logoPosterPuth;
  private String backDropPath;
  private String bigPosterPath;
  private double voteAverage;
  private String releaseDate;

  public Movie(int uniqueId, int id, int voteCount, String title, String originalTitle,
               String overview, String posterPuth,
               String logoPosterPuth, String backDropPath,
               String bigPosterPath, double voteAverage, String releaseDate) {
    this.uniqueId = uniqueId;
    this.id = id;
    this.voteCount = voteCount;
    this.title = title;
    this.originalTitle = originalTitle;
    this.overview = overview;
    this.posterPuth = posterPuth;
    this.logoPosterPuth = logoPosterPuth;
    this.backDropPath = backDropPath;
    this.bigPosterPath = bigPosterPath;
    this.voteAverage = voteAverage;
    this.releaseDate = releaseDate;
  }

  @Ignore
  public Movie(int id, int voteCount, String title, String originalTitle,
               String overview, String posterPuth,
               String logoPosterPuth, String backDropPath,
               String bigPosterPath, double voteAverage, String releaseDate) {
    this.id = id;
    this.voteCount = voteCount;
    this.title = title;
    this.originalTitle = originalTitle;
    this.overview = overview;
    this.posterPuth = posterPuth;
    this.logoPosterPuth = logoPosterPuth;
    this.backDropPath = backDropPath;
    this.bigPosterPath = bigPosterPath;
    this.voteAverage = voteAverage;
    this.releaseDate = releaseDate;
  }

  public void setUniqueId(int uniqueId) {
    this.uniqueId = uniqueId;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setVoteCount(int voteCount) {
    this.voteCount = voteCount;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setOriginalTitle(String originalTitle) {
    this.originalTitle = originalTitle;
  }

  public void setOverview(String overview) {
    this.overview = overview;
  }

  public void setPosterPuth(String posterPuth) {
    this.posterPuth = posterPuth;
  }

  public void setBackDropPath(String backDropPath) {
    this.backDropPath = backDropPath;
  }

  public void setVoteAverage(double voteAverage) {
    this.voteAverage = voteAverage;
  }

  public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }

  public void setBigPosterPath(String bigPosterPath) {
    this.bigPosterPath = bigPosterPath;
  }

  public void setLogoPosterPuth(String logoPosterPuth) {
    this.logoPosterPuth = logoPosterPuth;
  }

  public int getUniqueId() {
    return uniqueId;
  }

  public int getId() {
    return id;
  }

  public int getVoteCount() {
    return voteCount;
  }

  public String getTitle() {
    return title;
  }

  public String getOriginalTitle() {
    return originalTitle;
  }

  public String getOverview() {
    return overview;
  }

  public String getPosterPuth() {
    return posterPuth;
  }

  public String getLogoPosterPuth() {
    return logoPosterPuth;
  }

  public String getBigPosterPath() {
    return bigPosterPath;
  }

  public String getBackDropPath() {
    return backDropPath;
  }

  public double getVoteAverage() {
    return voteAverage;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

}
