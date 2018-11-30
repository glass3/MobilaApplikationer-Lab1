package com.example.casper.currencyexchanger;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;
import com.hrskrs.instadotlib.InstaDotView;


public class MainActivity extends AppCompatActivity {


    ProgressDialog progressBar;
    private GestureDetectorCompat gestureObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setTitle("Loading currencies");
        progressBar.setMessage("Please Wait...");
        progressBar.show();



        final InstaDotView instaDotView = findViewById(R.id.instadot);
        instaDotView.setNoOfPages(3);
        instaDotView.onPageChange(1);




        SimpleIPrequest();  //pulls users IP from an API and gets the country the user is in
                            //which then gets that countries currency.
                            // Fetches all currencies and sets the default currency.



        // TODO fix animations whens swiping


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



    public void spinnerUpdater(){
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
    }

    @Override
    public void onResume(){
        super.onResume();

        spinnerUpdater(); //gets latest spinner values


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



    public void Simplerequest(final String countrycurr){
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
                                }


                            spinnerUpdater();   //fills spinners with new values
                            Spinner spinner = findViewById(R.id.StartCurrency);


                            if (countrycurr != null) {  //sets the spinner to current country
                                Adapter adapter = (ArrayAdapter) spinner.getAdapter();
                                int spinnerPosition = ((ArrayAdapter) adapter).getPosition(countrycurr);
                                spinner.setSelection(spinnerPosition);
                                progressBar.cancel();  //stops the progressbar because everything is loaded


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
    public void SimpleIPrequest(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://api.ipstack.com/check?access_key=8d45036b5d2e921ad7fd9679f68cd0a9";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject reader = new JSONObject(response);
                            String landcode = reader.getString("country_code");
                            GetCountry(landcode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
    public void GetCountry(String arg){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://restcountries.eu/rest/v2/alpha/"+arg;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject reader = new JSONObject(response); // make an json object out of the string
                            JSONArray currencie = reader.getJSONArray("currencies"); // Make an array reader for the root of the subobject currencies of hte mian object
                            JSONObject currlvl = currencie.getJSONObject(0); // make an object of the array slot of the array
                            String currcode = currlvl.getString("code"); // fetch the value out of the value pair of the object we made out of the array
                            Simplerequest(currcode);
                            //Toast.makeText(getApplicationContext(), currcode, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
    private int getIndex(Spinner spinner, String myString){     //används inte?

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
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


