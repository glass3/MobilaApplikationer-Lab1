package com.example.casper.currencyexchanger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Map;


public class AddCurrency extends AppCompatActivity {


    private GestureDetectorCompat gestureObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_currency);


        gestureObject = new GestureDetectorCompat(this, new LearnGesture());



        final Button button = findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addCurrency();
            }
        });



        final Button delButton = findViewById(R.id.deleteButton);
        delButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                deleteCurrency();
            }
        });



    }

    @Override
    public void onResume(){
        super.onResume();

        //updates spinner container
        spinnerRefresh();
    }

    public void spinnerRefresh(){
        SharedPreferences pref = getSharedPreferences("DATA", MODE_PRIVATE);
        Map<String, ?> allEntries = pref.getAll();

        int size = allEntries.size();
        String[] keys = new String[size];

        int i = 0;


        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {

            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            keys[i] = entry.getKey();
            i++;

        }





        Spinner spinner = (Spinner) findViewById(R.id.deleteSpinner);


        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, keys);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);



        return;
    }


    public void addCurrency(){

        try{
            EditText editText = findViewById(R.id.symbolInput);
            String name = editText.getText().toString();

            EditText ratioInput = findViewById(R.id.ratioInput);
            String temp = ratioInput.getText().toString();

            //String ratioInput = findViewById(R.id.ratioInput).getText().toString();
            final float value = Float.parseFloat(temp);

            SharedPreferences sharedPreferences = getSharedPreferences("DATA",Context.MODE_PRIVATE);
            sharedPreferences.edit().putFloat(name,value).apply();







            return;
        }
        catch(Exception ex){
            return;
        }

    }

    public void deleteCurrency(){
        try{


            Spinner spinnerDeleteCurrency = (Spinner) findViewById(R.id.deleteSpinner);
            String deleteSpinnerItem = spinnerDeleteCurrency.getSelectedItem().toString();


            SharedPreferences pref = getSharedPreferences("DATA", MODE_PRIVATE);

            pref.edit().remove(deleteSpinnerItem).apply();
            Toast.makeText(getApplicationContext(), "Succesfully deleted", Toast.LENGTH_SHORT).show();

            spinnerRefresh();

            return;

        }
        catch(Exception ex){
            Toast.makeText(getApplicationContext(), "Failed deleting currency", Toast.LENGTH_SHORT).show();
            return;
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

            if (event2.getX() > event1.getX()){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
            return true;
        }
    }
}
