package com.demo.retrofit_database_demo.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ucreateuser on 26-Sep-17.
 */

public class ApiClient {
    //    public static final String BASE_URL = "https://reqres.in/api/";
    public static final String BASE_URL = "http://api.themoviedb.org/3/";


    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
