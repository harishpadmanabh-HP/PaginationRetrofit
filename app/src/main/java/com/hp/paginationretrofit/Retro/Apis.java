package com.hp.paginationretrofit.Retro;

import com.hp.paginationretrofit.model.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Apis {

    @GET("3/discover/movie?language=en&sort_by=popularity.desc&api_key=ef3ae31815851c717576d52c11043996")
    Call<MovieList>MOVIE_LIST_CALL(@Query("page") int page);



}
