package com.example.casper.rateexchanger;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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




        /*final EditText StartValue = (EditText)findViewById(R.id.StartValue);
        final TextView EndValue = (TextView)findViewById(R.id.EndValue);
        String value;*/

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


       /* Button exchangeButton = (Button)findViewById(R.id.button);

        exchangeButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){




                        EditText editText = (EditText)findViewById(R.id.StartValue);
                        String textsum = editText.getText().toString();
                        float sum = Float.parseFloat(textsum);

                        Spinner spinnerStartCurrency = (Spinner) findViewById(R.id.StartCurrency);
                        String startCurrency = spinnerStartCurrency.getSelectedItem().toString();

                        Spinner spinnerEndCurrency = (Spinner)findViewById(R.id.EndCurrency);
                        String endCurrency = spinnerEndCurrency.getSelectedItem().toString();


                        Exchanger(startCurrency, endCurrency, sum);




                    }
                }
        );*/

        //Creates spinner adapter to style and fill
        Spinner spinner = (Spinner) findViewById(R.id.StartCurrency);
        Spinner spinner2 = (Spinner) findViewById(R.id.EndCurrency);

        //styles and fills the adapters
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //matches the spinners with the finished adapters
        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter);





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
