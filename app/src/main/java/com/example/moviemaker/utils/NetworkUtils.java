package com.example.moviemaker.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {

  private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
  public static final String BASE_URL_VIDEOS = "https://api.themoviedb.org/3/movie/%s/videos";
  public static final String BASE_URL_REVIEWS = "https://api.themoviedb.org/3/movie/%s/reviews";

  private static final String PARAMS_API_KEY = "api_key";
  private static final String PARAMS_LANGUAGE = "language";
  private static final String PARAMS_SORT_BY = "sort_by";
  private static final String PARAMS_PAGE = "page";
  public static final String PARAMS_MIN_VOTE_COUNT = "vote_count.gte";

  private static final String API_KEY = "ae5f5a60cd1117e427b1d9c7697c9323";
  private static final String LANGUAGE = "ru-RU";
  private static final String SORT_BY_POPULARITY = "popularity.desc";
  private static final String SORT_BY_TOP_RATED = "vote_average.desc";
  public static final String MIN_VOTE_COUNT_VALUE = "1000";

  public static final int POPULARITY = 0;
  public static final int TOP_RATED = 1;

  //Формируем запрос, который возвращает URL отзывов
  public static URL buildURLToReviews(int id) {
    Uri uri = Uri.parse(String.format(BASE_URL_REVIEWS, id)).buildUpon()
            .appendQueryParameter(PARAMS_API_KEY, API_KEY)
            .build();
    try {
      return new URL(uri.toString());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return null;
  }

  //Формируем запрос, который возвращает URL видео
  public static URL buildURLToVideos(int id) {
    Uri uri = Uri.parse(String.format(BASE_URL_VIDEOS, id)).buildUpon()
            .appendQueryParameter(PARAMS_API_KEY, API_KEY)
            .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE)
            .build();
    try {
      return new URL(uri.toString());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return null;
  }

  //Формируем запрос, возвращаем url с данными
  public static URL buildURL(int sortBy, int page) {
    URL result = null;
    String methodOfSort = sortBy == POPULARITY ? SORT_BY_POPULARITY : SORT_BY_TOP_RATED;

    Uri uri = Uri.parse(BASE_URL).buildUpon()
            .appendQueryParameter(PARAMS_API_KEY, API_KEY)
            .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE)
            .appendQueryParameter(PARAMS_SORT_BY, methodOfSort)
            .appendQueryParameter(PARAMS_PAGE, Integer.toString(page))
            .appendQueryParameter(PARAMS_MIN_VOTE_COUNT, MIN_VOTE_COUNT_VALUE)
            .build();
    try {
      result = new URL(uri.toString());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return result;
  }

  // Получаем JSON отзывов из сети.
  public static JSONObject JSONObjectForReviews(int id) {
    JSONObject result = null;
    // Получаем url
    URL url = buildURLToReviews(id);
    try {
      // Передаем в объект класса JSONLoadTask url и вызываем метод get, чтобы получить JSONObject
      result = new JSONLoadTask().execute(url).get();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return result;
  }

  // Получаем JSON видео из сети.
  public static JSONObject JSONObjectForVideos(int id) {
    JSONObject result = null;
    // Получаем url
    URL url = buildURLToVideos(id);
    try {
      // Передаем в объект класса JSONLoadTask url и вызываем метод get, чтобы получить JSONObject
      result = new JSONLoadTask().execute(url).get();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return result;
  }

  // Получаем JSON из сети.
  public static JSONObject getJSONFromNetwork(int sortBy, int page) {
    JSONObject result = null;
    // Получаем url
    URL url = buildURL(sortBy, page);
    try {
      // Передаем в объект класса JSONLoadTask url и вызываем метод get, чтобы получить JSONObject
      result = new JSONLoadTask().execute(url).get();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return result;
  }

  public static class JSONLoader extends AsyncTaskLoader<JSONObject> {
    private Bundle bundle;
    private OnStartLoadingListener onStartLoadingListener;

    public void setOnStartLoadingListener(OnStartLoadingListener onStartLoadingListener) {
      this.onStartLoadingListener = onStartLoadingListener;
    }

    public interface OnStartLoadingListener {
      void onStartLoading();
    }

    public JSONLoader(@NonNull Context context, Bundle bundle) {
      super(context);
      this.bundle = bundle;
    }

    @Override
    protected void onStartLoading() {
      super.onStartLoading();
      if (onStartLoadingListener != null) {
        onStartLoadingListener.onStartLoading();
      }
      forceLoad();
    }

    @Nullable
    @Override
    public JSONObject loadInBackground() {
      if (bundle == null) {
        return null;
      }
      String urlAsString = bundle.getString("url");
      URL url = null;
      try {
        url = new URL(urlAsString);
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }
      JSONObject result = null;
      if(url == null) {
        return result;
      }
      HttpsURLConnection connection = null;
      try {
        connection = (HttpsURLConnection) url.openConnection();
        InputStream inputStream = connection.getInputStream();
        InputStreamReader inputStream1 = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStream1);
        StringBuilder stringBuilder = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
          stringBuilder.append(line);
          line = reader.readLine();
        }
        result = new JSONObject(stringBuilder.toString());
      } catch (IOException | JSONException e) {
        e.printStackTrace();
      } finally {
        if(connection != null) {
          connection.disconnect();
        }
      }
      return result;
    }
  }

  //Принимаем в параметры URL, и парсим из него данные
  private static class JSONLoadTask extends AsyncTask<URL, Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(URL... urls) {
      JSONObject result = null;
      if(urls == null || urls.length == 0) {
        return result;
      }
      HttpsURLConnection connection = null;
      try {
        connection = (HttpsURLConnection) urls[0].openConnection();
        InputStream inputStream = connection.getInputStream();
        InputStreamReader inputStream1 = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStream1);
        StringBuilder stringBuilder = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
          stringBuilder.append(line);
          line = reader.readLine();
        }
        result = new JSONObject(stringBuilder.toString());
      } catch (IOException | JSONException e) {
        e.printStackTrace();
      } finally {
        if(connection != null) {
          connection.disconnect();
        }
      }
      return result;
    }
  }
}
