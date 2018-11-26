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
import android.util.JsonReader;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private GestureDetectorCompat gestureObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //requestHandler();
        Simplerequest();

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

        spinnerChange();



        gestureObject = new GestureDetectorCompat(this, new LearnGesture());
    }


    public void spinnerChange(){


        Spinner spinner = (Spinner) findViewById(R.id.StartCurrency);
        Spinner spinner2 = (Spinner) findViewById(R.id.EndCurrency);




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

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
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });



        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
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
            public void onNothingSelected(AdapterView<?> parentView) {
                return;

            }

        });



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

            if(event2.getX() > event1.getX())       //Swipe höger
            {
                //Toast.makeText(getApplicationContext(), "swipe 1", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CurrencyList.class);
                startActivity(intent);

            }
            else if (event2.getX() < event1.getX()){     //swipe vänster
                Intent intent = new Intent(getApplicationContext(), AddCurrency.class);
                startActivity(intent);
            }
            return true;
        }
    }

    public void requestHandler(){
        String url ="http://data.fixer.io/api/latest?access_key=06be999ca35f0170790fa4b8b6da4399&format=1";
        RequestQueue queue = Volley.newRequestQueue(this);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "swipe 1", Toast.LENGTH_SHORT).show();
                        JSONObject data = response;

                        //String rates =  data.getString("rates");
                        response.getClass();
                        data.opt("rates");

                        data.optString("rates");

                        JSONObject test = data.optJSONObject("rates");

                        

                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

// Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest);
        //MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void Simplerequest(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://data.fixer.io/api/latest?access_key=06be999ca35f0170790fa4b8b6da4399&format=1";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject reader = new JSONObject(response); // This creates an JSON object out of the string we received from the Request. The string is already in an Json like format
                            JSONObject rates = reader.getJSONObject("rates"); // Creates an object which contains all the rates from the already previously reader object
                            String eur = rates.getString("AED"); // here we get the value from the rates object that matches the key we have chosen. for example EUR or AED. TODO Remove
                            JSONArray arr = rates.names(); // Json Array that contains all the names of the rates that we can fetch. This do not include the value of that rate.
                          //  Toast.makeText(getApplicationContext(), eur, Toast.LENGTH_SHORT).show(); // Prints the value we got from above. this is strictly for testing
                                for (int i = 0; i<arr.length(); i++)
                                {
                                    String c = arr.getString(i);
                                    String name = c.toString(); // Might be redundant TODO Check if this is redundant
                                    double exchange = rates.getDouble(name); // gets the currency as a double TODO fix the name
                                    float currency = (float) exchange; // casts the double as a float TODO fix this maybe as a nicer fix
                                    addCurrency(name,currency); // Inserts the currency name and value into the add currency function.
                                  //  Toast.makeText(getApplicationContext(), "hi", Toast.LENGTH_SHORT).show();
                                }
                        } catch (JSONException e) {
                            e.printStackTrace(); // Error handling. TODO
                        }

                        // Display the first 500 characters of the response string.
                      //  mTextView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  mTextView.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
/*
    public void requestHandler(){
        String url ="http://data.fixer.io/api/latest?access_key=06be999ca35f0170790fa4b8b6da4399&format=1";


        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONParser parser = new JSONParser();
                        JSONObject json = (JSONObject) parser.parse(stringToParse);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
*/




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


