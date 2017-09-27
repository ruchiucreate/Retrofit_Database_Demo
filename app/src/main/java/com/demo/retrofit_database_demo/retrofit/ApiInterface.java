package com.demo.retrofit_database_demo.retrofit;

import com.demo.retrofit_database_demo.models.MovieResponseBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ucreateuser on 26-Sep-17.
 */

public interface ApiInterface {

    @GET("movie/top_rated")
    Call<MovieResponseBean> getMovieDetailService(@Query("api_key") String api_key);
}
