package com.hp.paginationretrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hp.paginationretrofit.Retro.Retro;
import com.hp.paginationretrofit.model.MovieList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Context context;
    private RecyclerView mRecyclerView;

    private ProgressBar progressBar;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int page = 1;
    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView=findViewById(R.id.recyclerView);
        progressBar=findViewById(R.id.progressBar);


        try{
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mAdapter = new MovieAdapter(context);

            mRecyclerView.setAdapter(mAdapter);


            mRecyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
                @Override
                protected void loadMoreItems() {
                    isLoading = true;
                    if (!isLastPage) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadData(page);
                            }
                        }, 200);
                    }
                }

                @Override
                public boolean isLastPage() {
                    return isLastPage;
                }

                @Override
                public boolean isLoading() {
                    return isLoading;
                }
            });




            loadData(page);
        }catch (Exception e)
        {
            Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
        }




    }

    private void loadData(int page) {

        progressBar.setVisibility(View.VISIBLE);
        Retro retro=new Retro();
        Call<MovieList> movieListCall=retro.getApi().MOVIE_LIST_CALL(page);
        movieListCall.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {

               // Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
               MovieList movieLists=response.body();
                resultAction(movieLists);


            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {

                Toast.makeText(context, "failure  "+t, Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void resultAction(MovieList model) {
        progressBar.setVisibility(View.INVISIBLE);
        isLoading = false;
        if (model != null) {
            mAdapter.addItems(model.getResults());
            if (model.getPage() == model.getTotal_pages()) {
                isLastPage = true;
            } else {
                page = model.getPage() + 1;
            }
        }
    }

}
