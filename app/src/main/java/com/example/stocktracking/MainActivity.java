package com.example.stocktracking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etStockSymbol;
    private Button btnGetStock;
    private TextView tvStockData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etStockSymbol = findViewById(R.id.et_stock_symbol);
        btnGetStock = findViewById(R.id.btn_get_stock);
        tvStockData = findViewById(R.id.tv_stock_data);

        btnGetStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String symbol = etStockSymbol.getText().toString();
                fetchStockData(symbol);
            }
        });
    }

    private void fetchStockData(String symbol) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<StockResponse> call = apiService.getDailyStock(symbol, "0ISK3VWFCBE4Q5KY");

        call.enqueue(new Callback<StockResponse>() {
            @Override
            public void onResponse(Call<StockResponse> call, Response<StockResponse> response) {
                if (response.isSuccessful()) {
                    StockResponse stockResponse = response.body();
                    if (stockResponse != null && stockResponse.getTimeSeriesDaily() != null) {
                        StringBuilder formattedData = new StringBuilder();
                        for (Map.Entry<String, Map<String, String>> entry : stockResponse.getTimeSeriesDaily().entrySet()) {
                            String date = entry.getKey();
                            Map<String, String> dailyData = entry.getValue();
                            formattedData.append("Date: ").append(date).append("\n")
                                    .append("Open: ").append(dailyData.get("1. open")).append("\n")
                                    .append("High: ").append(dailyData.get("2. high")).append("\n")
                                    .append("Low: ").append(dailyData.get("3. low")).append("\n")
                                    .append("Close: ").append(dailyData.get("4. close")).append("\n")
                                    .append("Volume: ").append(dailyData.get("5. volume")).append("\n\n");
                        }
                        tvStockData.setText(formattedData.toString());
                    } else {
                        tvStockData.setText("No data available");
                    }
                } else {
                    tvStockData.setText("Response not successful");
                }
            }

            @Override
            public void onFailure(Call<StockResponse> call, Throwable t) {
                tvStockData.setText("Error: " + t.getMessage());
            }
        });
    }


}
