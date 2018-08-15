package app.android.rynz.catalogmovie;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import app.android.rynz.catalogmovie.models.MovieModel;
import app.android.rynz.catalogmovie.presenters.TMDBPresenter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<MovieModel>>, View.OnClickListener

{
    private final static int loaderID=100;
    private final static String KEY_QUERY="query",KEY_KEYWORD="keyword";
    private Button btnSearch;
    private EditText etKeyword;
    private ListView lvMovie;
    private Bundle loaderBundle=new Bundle();

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString(KEY_KEYWORD,etKeyword.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        if(savedInstanceState!=null)
        {
            etKeyword.setText(savedInstanceState.getString(KEY_KEYWORD));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
    }

    private void bindView()
    {
        etKeyword=findViewById(R.id.et_keyword);
        btnSearch=findViewById(R.id.btn_submit_search);
        btnSearch.setOnClickListener(this);
        lvMovie=findViewById(R.id.lv_search);
    }

    @NonNull
    @Override
    public Loader<ArrayList<MovieModel>> onCreateLoader(int i, @Nullable Bundle bundle)
    {
        try{
            return new TMDBPresenter(this).toSearch(bundle.getString(KEY_QUERY));
        }catch (NullPointerException e)
        {

            return new TMDBPresenter(this).toSearch("");
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<MovieModel>> loader, ArrayList<MovieModel> movieModels)
    {
        if(movieModels.size()>0)
        {
            ArrayList<String> listMovie=new ArrayList<>();
            for(MovieModel m:movieModels)
            {
                listMovie.add(m.getTitle()+"\n"+m.getReleaseDate());
            }
            ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,listMovie);
            lvMovie.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText(this, "Result not found!, try another keyword", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<MovieModel>> loader)
    {

    }

    @Override
    public void onClick(View view)
    {
        if(view==btnSearch)
        {
            if(!TextUtils.isEmpty(etKeyword.getText().toString()))
            {
                loaderBundle.clear();
                loaderBundle.putString(KEY_QUERY,etKeyword.getText().toString());
                if(getLoaderManager().getLoader(loaderID)!=null)
                {
                    getLoaderManager().restartLoader(loaderID,loaderBundle,this);
                }
                else
                {
                    getLoaderManager().initLoader(loaderID,loaderBundle,this);
                }
            }
            else
            {
                Toast.makeText(this, "keyword not valid!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
