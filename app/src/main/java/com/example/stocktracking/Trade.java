package com.example.stocktracking;

public class Trade {
    private String stockSymbol;
    private int quantity;
    private double price;
    private String type; // "buy" or "sell"

    public Trade(String stockSymbol, int quantity, double price, String type) {
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }

    // Getter und Setter
}
