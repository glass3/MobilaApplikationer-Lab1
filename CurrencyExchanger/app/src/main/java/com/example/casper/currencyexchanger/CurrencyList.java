package com.example.casper.currencyexchanger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Map;

public class CurrencyList extends AppCompatActivity {

    private GestureDetectorCompat gestureObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_list);


        gestureObject = new GestureDetectorCompat(this, new LearnGesture());


        SharedPreferences prefA = getSharedPreferences("DATA", MODE_PRIVATE);



        Map<String, ?> allEntries = prefA.getAll();

        int size = allEntries.size();
        String[] keys = new String[size];
        String[] values = new String[size];

        int i = 0;

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {

            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            keys[i] = entry.getKey();
            values[i] = entry.getValue().toString();
            i++;

        }

        TextView textView = findViewById(R.id.list);

        for (int j=0; j < size; j++){
            textView.append(keys[j]+ "\t"+values[j]+"\n");
        }








    }
    @Override
    public boolean onTouchEvent (MotionEvent event) {
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {

            if (event2.getX() < event1.getX()){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
            return true;
        }
    }
}
