package com.example.casper.rateexchanger;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Map;

public class CurrencyList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_list);


        SharedPreferences sharedPreferences = getSharedPreferences("DATA",Context.MODE_PRIVATE);
        Map<String, ?> map = sharedPreferences.getAll();



    }
}
