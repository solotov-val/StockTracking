package com.example.stocktracking;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class StockResponse {
    @SerializedName("Time Series (Daily)")
    private Map<String, Map<String, String>> timeSeriesDaily;

    public Map<String, Map<String, String>> getTimeSeriesDaily() {
        return timeSeriesDaily;
    }

    public void setTimeSeriesDaily(Map<String, Map<String, String>> timeSeriesDaily) {
        this.timeSeriesDaily = timeSeriesDaily;
    }

    @Override
    public String toString() {
        return "StockResponse{" +
                "timeSeriesDaily=" + timeSeriesDaily +
                '}';
    }
}
