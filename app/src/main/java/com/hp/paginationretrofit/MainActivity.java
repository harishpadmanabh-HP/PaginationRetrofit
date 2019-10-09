package com.hp.paginationretrofit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hp.paginationretrofit.Retro.Retro;
import com.hp.paginationretrofit.model.MovieList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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

    public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MVH> implements Configs {
        MovieList movieLists;
        Context context;

        List<MovieList.ResultsBean>Items;

        public MovieAdapter(MovieList movieLists, Context context, List<MovieList.ResultsBean> items) {
            this.movieLists = movieLists;
            this.context = context;
            Items = items;
        }

        public MovieAdapter(Context mcontext) {
            Items = new ArrayList<>();
            context = mcontext;
        }

        @NonNull
        @Override
        public MovieAdapter.MVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movielistitem, parent, false);

            return new MovieAdapter.MVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MovieAdapter.MVH holder, int position) {
            //  holder.movieName.setText(movieLists.getResults().get(position).getTitle().trim());

            MovieList.ResultsBean model = Items.get(position);

            //Picasso.get().load(model.getAvatar()).into(holder.imgUserAvatar);
            //  holder.txtFirstName.setText("First Name: "+ model.getFirstName());

            Log.e("URL",BASR_BACKDROP_PATH+model.getBackdrop_path().trim());

           // Picasso.get(MainActivity.this).load(BASR_BACKDROP_PATH+model.getBackdrop_path().trim()).into(holder.poster);

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .priority(Priority.HIGH);

            Glide.with(MainActivity.this)
                    .asBitmap()
                    .load("https://i.ibb.co/wsM1wr9/0956089226fe3fad0a9da14dcf82ae43.jpg")
                   // .load(BASE_POSTER_PATH+model.getPoster_path().trim())
                    .apply(options)
                    .into(new BitmapImageViewTarget(holder.poster) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            super.onResourceReady(bitmap, transition);
                            Palette.from(bitmap).generate(palette -> setBackgroundColor(palette, holder));
                        }
                    });
            holder.movieName.setText(model.getTitle().toUpperCase());

        }

        private void setBackgroundColor(Palette palette, MovieAdapter.MVH holder ) {
            holder.movieName.setBackgroundColor(palette.getVibrantColor(MainActivity.this
                    .getResources().getColor(R.color.black_translucent_60)));
        }

        @Override
        public int getItemCount() {
            return Items.size();
        }

        public class MVH extends RecyclerView.ViewHolder{

            ImageView poster;
            TextView movieName;
            public MVH(@NonNull View itemView) {
                super(itemView);
                poster=itemView.findViewById(R.id.poster);
                movieName=itemView.findViewById(R.id.moviename);



            }
        }
        public void setItems(List<MovieList.ResultsBean> items){
            Items = items;
            notifyDataSetChanged();
        }

        public void addItems(List<MovieList.ResultsBean> items){
            Items.addAll(items);
            notifyDataSetChanged();;
        }


    }


}
