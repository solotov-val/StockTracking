package com.example.stocktracking;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etStockSymbol;
    private Button btnGetStock;
    private TextView tvStockData;
    private LineChart lineChart;
    private EditText etTradeQuantity;
    private Button btnBuy;
    private Button btnSell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etStockSymbol = findViewById(R.id.et_stock_symbol);
        btnGetStock = findViewById(R.id.btn_get_stock);
        tvStockData = findViewById(R.id.tv_stock_data);
        lineChart = findViewById(R.id.lineChart);
        etTradeQuantity = findViewById(R.id.et_trade_quantity);
        btnBuy = findViewById(R.id.btn_buy);
        btnSell = findViewById(R.id.btn_sell);

        btnGetStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String symbol = etStockSymbol.getText().toString();
                fetchStockData(symbol);
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateTrade("buy");
            }
        });

        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateTrade("sell");
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
                        displayData(stockResponse.getTimeSeriesDaily());
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

    private void displayData(Map<String, Map<String, String>> timeSeriesDaily) {
        List<Entry> entries = new ArrayList<>();
        StringBuilder formattedData = new StringBuilder();

        int index = 0;
        for (Map.Entry<String, Map<String, String>> entry : timeSeriesDaily.entrySet()) {
            String date = entry.getKey();
            Map<String, String> dailyData = entry.getValue();
            float closePrice = Float.parseFloat(dailyData.get("4. close"));

            entries.add(new Entry(index, closePrice));
            formattedData.append("Date: ").append(date).append("\n")
                    .append("Open: ").append(dailyData.get("1. open")).append("\n")
                    .append("High: ").append(dailyData.get("2. high")).append("\n")
                    .append("Low: ").append(dailyData.get("3. low")).append("\n")
                    .append("Close: ").append(dailyData.get("4. close")).append("\n")
                    .append("Volume: ").append(dailyData.get("5. volume")).append("\n\n");
            index++;
        }

        LineDataSet dataSet = new LineDataSet(entries, "Stock Prices");
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // refresh

        tvStockData.setText(formattedData.toString());
    }

    private void simulateTrade(String type) {
        String symbol = etStockSymbol.getText().toString();
        int quantity = Integer.parseInt(etTradeQuantity.getText().toString());
        double price = 0.0; // Hier könntest du den aktuellen Preis des Stocks verwenden
        // Füge Logik hinzu, um den aktuellen Preis des Stocks zu holen

        Trade trade = new Trade(symbol, quantity, price, type);
        // Logik zur Verarbeitung des Trades hinzufügen, z.B. in einer Liste speichern
    }
}
