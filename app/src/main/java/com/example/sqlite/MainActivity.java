package com.example.sqlite;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseHelper db;
    //0f15e1c3ccdd8926c4e3bfa4b81e3253 API KEY
    public static final String TAG = "mytag";
    private static final int REQUEST = 1;
    public Button b1, b2, b3, b4;
    String addcity, delcity, getweather;
    ImageView imageView;
    TextView resultView;
    public EditText t1, t2, t3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        b1 = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        resultView = findViewById(R.id.show);
        b2 = findViewById(R.id.buttondel);
        b3 = findViewById(R.id.button_next);
        b4 = findViewById(R.id.viewdata);
        t1 = findViewById(R.id.text1);
        t2 = findViewById(R.id.text13);
        t3 = findViewById(R.id.text11);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);

    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            addcity = t1.getText().toString().trim();
            if (addcity.isEmpty()) {

                Toast.makeText(getApplicationContext(), "Enter City", 0).show();
            } else {

                db.insert(addcity);
                Toast.makeText(getApplicationContext(), "City Added", 0).show();


            }


        } else if (v.getId() == R.id.buttondel) {
            delcity = t2.getText().toString().trim();
            if (delcity.isEmpty()) {

                Toast.makeText(getApplicationContext(), "Enter City to delete", 0).show();
            } else {
                Cursor cursor = db.result(delcity);

                if (cursor.getCount() > 0) {
                    db.del_data(delcity);
                    Toast.makeText(getApplicationContext(), "City Deleted", 0).show();


                } else {
                    Log.d(TAG, "7");

                    Toast.makeText(getApplicationContext(), "No such City found in database", 0).show();

                }


            }


        } else if (v.getId() == R.id.viewdata) {
            Cursor res = db.getallldata();
            if (res.getCount() == 0) {
                // show message
                showMessage("Error", "Nothing found");
                return;
            }

            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("City: " + res.getString(0) + "\n");
            }

            // Show all data
            showMessage("Data", buffer.toString());
            res.close();


        } else if (v.getId() == R.id.button_next) {
            getweather = t3.getText().toString().trim();
            //Log.d(TAG, "1");

            if (getweather.isEmpty()) {

                Toast.makeText(getApplicationContext(), "Enter City", 0).show();
                //Log.d(TAG, "2");

            } else {
                Cursor cursor = db.result(getweather);
                //Log.d(TAG, "3");

                if (cursor.getCount() > 0) {
                    //  Log.d(TAG, "4");
                    getweatherjson(getweather);
                    //Log.d(TAG, "5");
                    //cursor.close();
                    //Log.d(TAG, "6");
                    //resultView.setText("new");


                } else {
                    Log.d(TAG, "7");

                    Toast.makeText(getApplicationContext(), "No such City found in database", 0).show();

                }


            }


        }

    }

    public void getweatherjson(String getweather) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + getweather + "&appid=0f15e1c3ccdd8926c4e3bfa4b81e3253";
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray jsonArray = response.getJSONArray("weather");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String icon = jsonObject.getString("icon");
                    Log.d(TAG, icon);
                    JSONObject main_object1 = response.getJSONObject("wind");
                    JSONObject main_object2 = response.getJSONObject("coord");
                    //Log.d(TAG, "1-1");
                    String img_url = "https://openweathermap.org/img/w/" + icon + ".png";
                    Log.d(TAG, img_url);
                    Picasso.get().load(img_url).resize(400, 400).into(imageView);
                    String lon = String.valueOf(main_object2.getDouble("lon"));
                    String lat = String.valueOf(main_object2.getDouble("lat"));
                    String wind_speed = String.valueOf(main_object1.getDouble("speed"));
                    String temp = String.valueOf(main_object.getDouble("temp"));
                    String pressure = String.valueOf(main_object.getDouble("pressure"));
                    String humidity = String.valueOf(main_object.getDouble("humidity"));
                    Log.d(TAG, temp);

                    //Log.d(TAG, "2-2");
                    double temp_int = Double.parseDouble(temp);
                    DecimalFormat df2 = new DecimalFormat("#.##");
                    String temp1 = df2.format(temp_int - 273.0);
                    String addup = "Temp: " + temp1 + ", Pressure: " + pressure + ", Humidity: " + humidity + ", Wind Speed: " + wind_speed + ", Lat: " + lat + ", Lon: " + lon;
                    Log.d(TAG, addup);
                    resultView.setText(addup);
                    //Log.d(TAG, "5-5");//

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jor);
        Log.d(TAG, "6-6");


    }

}
