package app.android.rynz.catalogmovie.presenters;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.android.rynz.catalogmovie.BuildConfig;
import app.android.rynz.catalogmovie.models.MovieModel;
import app.android.rynz.catalogmovie.utils.KeysReference;
import app.android.rynz.catalogmovie.views.TMDBPresenterView;
import cz.msebera.android.httpclient.Header;

public class TMDBPresenter extends AsyncTaskLoader<ArrayList<MovieModel>> implements TMDBPresenterView
{
    private ArrayList<MovieModel> movieModelArrayList = new ArrayList<>();
    private String keyword, id, action;
    private static final String ACT_TAG_SEARCH = "search";
    private static final String ACT_TAG_DETAIL = "detail";

    public TMDBPresenter(@NonNull Context context)
    {
        super(context);
        onContentChanged();
    }

    public TMDBPresenter toSearch(@NonNull String keyword)
    {
        action = ACT_TAG_SEARCH;
        this.keyword = keyword;
        return this;
    }

    public TMDBPresenter toGetDetail(@NonNull String id)
    {
        action = ACT_TAG_DETAIL;
        this.id = id;
        return this;
    }

    @Override
    protected void onStartLoading()
    {
        if (takeContentChanged())
        {
            forceLoad();
        } else if (movieModelArrayList.size() > 0)
        {
            deliverResult(movieModelArrayList);
        }
    }

    @Nullable
    @Override
    public ArrayList<MovieModel> loadInBackground()
    {
        switch (action)
        {
            case ACT_TAG_SEARCH:
            {
                searchMovieByKeyword(keyword);
                break;
            }
            case ACT_TAG_DETAIL:
            {
                getMovieDetail(id);
                break;
            }
        }
        return movieModelArrayList;
    }

    @Override
    public void deliverResult(@Nullable ArrayList<MovieModel> data)
    {
        movieModelArrayList = data;
        super.deliverResult(movieModelArrayList);
    }

    @Override
    protected void onReset()
    {
        super.onReset();
        onStopLoading();
        if (movieModelArrayList.size() > 0)
        {
            movieModelArrayList.clear();
        }
    }

    @Override
    public ArrayList<MovieModel> searchMovieByKeyword(@NonNull String keyword)
    {
        SyncHttpClient syncHttpClient = new SyncHttpClient();
        RequestParams params = new RequestParams();
        params.put(KeysReference.KEY_API, BuildConfig.tmdb_api_key);
        params.put(KeysReference.KEY_QUERY, keyword);
        syncHttpClient.get(KeysReference.URL_SEARCH, params, new AsyncHttpResponseHandler()
        {
            @Override
            public void onStart()
            {
                setUseSynchronousMode(true);
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                try
                {
                    String json = new String(responseBody);
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray datas = jsonObject.getJSONArray("results");
                    for (int i = 0; i < datas.length(); i++)
                    {
                        JSONObject object = datas.getJSONObject(i);
                        MovieModel movie = new MovieModel(
                                object.getString(KeysReference.KEY_ID),
                                object.getString(KeysReference.KEY_TITLE),
                                object.getString(KeysReference.KEY_POSTER),
                                object.getString(KeysReference.KEY_OVERVIEW),
                                object.getString(KeysReference.KEY_REALEASE_DATE)
                        );
                        movieModelArrayList.add(movie);
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {

            }
        });

        return movieModelArrayList;
    }

    @Override
    public MovieModel getMovieDetail(@NonNull String id)
    {
        return movieModelArrayList.get(0);
    }

}
