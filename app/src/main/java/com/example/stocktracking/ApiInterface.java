package com.example.stocktracking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("query?function=TIME_SERIES_DAILY")
    Call<StockResponse> getDailyStock(@Query("symbol") String symbol, @Query("apikey") String apiKey);
}
