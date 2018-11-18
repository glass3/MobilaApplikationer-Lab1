package com.example.casper.currencyexchanger;




import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private GestureDetectorCompat gestureObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        addCurrency("EUR", 1f);
        addCurrency("SEK",10.251925f);
        addCurrency("USD", 1.13171f);
        addCurrency("GBP", 0.887334523f);
        addCurrency("CNY", 7.85304383f);
        addCurrency("JPY", 128.413707f);
        addCurrency("KRW", 1277.32506f);



        EditText editText = findViewById(R.id.StartValue);


        //textchange listener, triggers when StartValue receives input
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                EditText editText = (EditText)findViewById(R.id.StartValue);
                TextView finalText = findViewById(R.id.EndValue);
                String textsum = editText.getText().toString();

                if(textsum.length() <= 0){ //checks that the field isn't empty
                    finalText.setText("");  //clears the converted field
                    return;
                }
                float sum = Float.parseFloat(textsum);

                Spinner spinnerStartCurrency = (Spinner) findViewById(R.id.StartCurrency);
                String startCurrency = spinnerStartCurrency.getSelectedItem().toString();

                Spinner spinnerEndCurrency = (Spinner)findViewById(R.id.EndCurrency);
                String endCurrency = spinnerEndCurrency.getSelectedItem().toString();


                Exchanger(startCurrency, endCurrency, sum);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        //Creates spinner adapter to style and fill
        Spinner spinner = (Spinner) findViewById(R.id.StartCurrency);
        Spinner spinner2 = (Spinner) findViewById(R.id.EndCurrency);

        //styles and fills the adapters
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //matches the spinners with the finished adapters
        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter);




        gestureObject = new GestureDetectorCompat(this, new LearnGesture());
    }




    @Override
    public void onResume(){
        super.onResume();


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





        Spinner spinner = (Spinner) findViewById(R.id.StartCurrency);
        Spinner spinner2 = (Spinner) findViewById(R.id.EndCurrency);

        ArrayAdapter<String> adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, keys);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter);


        return;
    }




    @Override
    public boolean onTouchEvent (MotionEvent event) {
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {

            if(event2.getX() > event1.getX())
            {
                Toast.makeText(getApplicationContext(), "swipe 1", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CurrencyList.class);
                startActivity(intent);

            }
            else if (event2.getX() < event1.getX()){
                Intent intent = new Intent(getApplicationContext(), AddCurrency.class);
                startActivity(intent);
            }
            return true;
        }
    }





    public void addCurrency(String name, float value){
        SharedPreferences sharedPreferences = getSharedPreferences("DATA",Context.MODE_PRIVATE);
        sharedPreferences.edit().putFloat(name,value).apply();
        return;
    }
    public float getCurrency(String name){
        SharedPreferences sharedPreferences = getSharedPreferences("DATA",Context.MODE_PRIVATE);
        float exchangerate = sharedPreferences.getFloat(name,1);

        return exchangerate;
    }

    public void Exchanger(String startCurrency, String endCurrency, float sum){

        try{
            float startValue = getCurrency(startCurrency);
            float endValue = getCurrency(endCurrency);

            float temp = sum/startValue;
            temp = temp*endValue;

            final TextView EndValue = (TextView)findViewById(R.id.EndValue);

            EndValue.setText(Float.toString(temp));
        }
        catch (Exception ex){
            return;
        }

        return;
    }



}
