package com.courses.android.openweathermap;

import android.app.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Asus on 07.03.2016.
 */
public class MainAct extends Activity {

    final Context context = this;
    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView press;
    private TextView windSpeed;
    private TextView windDeg;
    private EditText city;
    private TextView hum;
    private ImageView imgView;
    private Button buttonGo;
    SharedPreferences sPref;
    final String SAVED_TEXT = "saved_text";
    FloatingActionButton fab;
    Timer timer;
    ListView listView;

    //FloatingActionButton fab;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton fab3;
    CoordinatorLayout rootLayout;

    //Save the FAB's active status
    //false -> fab = close
    //true -> fab = open
    private boolean FAB_Status = false;

    //Animations
    Animation show_fab_1;
    Animation hide_fab_1;
    Animation show_fab_2;
    Animation hide_fab_2;


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("cities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coordinator_layout_m);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setActionBar(toolbar);
        rl = (RelativeLayout) findViewById(R.id.hedCard);
        cityText = (TextView) findViewById(R.id.cityText);
        condDescr = (TextView) findViewById(R.id.condDescr);
        temp = (TextView) findViewById(R.id.temp);
        hum = (TextView) findViewById(R.id.hum);
        press = (TextView) findViewById(R.id.press);
        windSpeed = (TextView) findViewById(R.id.windSpeed);
        windDeg = (TextView) findViewById(R.id.windDeg);
        imgView = (ImageView) findViewById(R.id.condIcon);

        rootLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        listView = new ListView(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        //Animations
        show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);
        hide_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_hide);
        show_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_show);
        hide_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_hide);
        fab1 = (FloatingActionButton) findViewById(R.id.fab_1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab_2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FAB_Status == false) {
                    //Display FAB menu
                    expandFAB();
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    hideFAB();
                    FAB_Status = false;
                }
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Show();

            }
        });

        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (FAB_Status) {
                    hideFAB();
                    FAB_Status = false;
                }
                return false;
            }
        });
    }
    private void Dialog(){

        final EditText input = new EditText(MainAct.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT); //only text without nums
        //input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) }); //

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainAct.this);
        alertDialog.setTitle("Your city weather");
        alertDialog.setMessage("Enter city..");
        alertDialog.setIcon(R.drawable.search);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("Ок",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String city = input.getEditableText().toString();
                        JSONWeatherTask task = new JSONWeatherTask();
                        task.execute(city);
                    }
                });

        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
    ////////////
    //dialog with ukrainian cities
    void Show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ukrainian City");

        ListView modeList = new ListView(this);
        final ArrayList<String> items = getCountries("cities.json");
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, items);
        modeList.setAdapter(modeAdapter);
        builder.setView(modeList);
        final Dialog dialog = builder.create();
        dialog.show();

        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String city = items.get(position);
                JSONWeatherTask task = new JSONWeatherTask();
                task.execute(city);
                dialog.cancel();
            }
        });
    }


  /*  void saveText() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_TEXT, city.getText().toString());
        ed.commit();
        Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
    }

    public class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            //Since we want to change something which is on hte UI
            //so we have to run on UI thread..
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Random random = new Random();//this is random generator
                    rl.setBackgroundColor(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));

                }
            });
        }
    }*/

    /**
     * getCountries from asset file
     */
    private ArrayList<String> getCountries(String fileName) {
        JSONArray jsonArray;
        ArrayList<String> countryList = new ArrayList<String>();
        try {
            AssetManager assetManager = getResources().getAssets();
            InputStream is = assetManager.open(fileName);
            int size = is.available();
            byte[] data = new byte[size];
            is.read(data);
            is.close();
            String json = new String(data, "UTF-8");
            JSONObject jsonObjMain = new JSONObject(json);
            jsonArray = jsonObjMain.getJSONArray("countries");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length() - 1; i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    countryList.add(jsonObj.getString("name"));
                    //countryList.add(jsonArray.getJSONObject(i).getString("name"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return countryList;
    }

    /**
     * background operations
     */
    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        double lat;
        double lng;
        private GoogleMap googleMap;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ((new HTTPClient()).getWeatherData(params[0]));
            //mScrollView = (ScrollView) findViewById(R.id.sv_container);
            try {
                weather = JSONParser.getWeather(data);
                // iconka
                weather.iconData = ((new HTTPClient()).getImage(weather.currentCondition.getIcon()));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            if (weather.hasError) {
                Toast.makeText(MainAct.this, weather.errorMessage, Toast.LENGTH_SHORT).show();

                return;
            }
            //if night change background
            if (weather.currentCondition.getIcon().equals("01n") || weather.currentCondition.getIcon().equals("02n")
                    || weather.currentCondition.getIcon().equals("03n") || weather.currentCondition.getIcon().equals("04n")
                    || weather.currentCondition.getIcon().equals("09n")
                    || weather.currentCondition.getIcon().equals("10n")
                    || weather.currentCondition.getIcon().equals("11n")
                    || weather.currentCondition.getIcon().equals("13n")
                    || weather.currentCondition.getIcon().equals("50n")) {
                rl.setBackgroundResource(R.drawable.headn);
            }
            // rl.setBackground(R.drawable.headn);

            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                imgView.setImageBitmap(img);

            }

            cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
            condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
            temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "°");
            hum.setText("" + weather.currentCondition.getHumidity() + "%");
            press.setText("" + weather.currentCondition.getPressure() + " hPa");
            windSpeed.setText("" + weather.wind.getSpeed() + " mps");
            windDeg.setText("" + weather.wind.getDeg() + "° ");
            lat = Double.valueOf(weather.location.getLatitude());
            lng = Double.valueOf(weather.location.getLongitude());
            //city = weather.location.getCity()+weather.location.getCountry();

            final LatLng mPoint = new LatLng(lat,lng);
            try {
                if (googleMap == null) {

                    //googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragment_map)).getMap();
                    googleMap = ((MapFragment) getFragmentManager().
                            findFragmentById(R.id.map)).getMap();
                }
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                Marker m = googleMap.addMarker(new MarkerOptions().
                        position(mPoint).title("=)").icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker)));
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    private void expandFAB() {

        //Floating Action Button 1
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        layoutParams.rightMargin += (int) (fab1.getWidth() * 1.7);
        layoutParams.bottomMargin += (int) (fab1.getHeight() * 0.25);
        fab1.setLayoutParams(layoutParams);
        fab1.startAnimation(show_fab_1);
        fab1.setClickable(true);

        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
        layoutParams2.rightMargin += (int) (fab2.getWidth() * 1.5);
        layoutParams2.bottomMargin += (int) (fab2.getHeight() * 1.5);
        fab2.setLayoutParams(layoutParams2);
        fab2.startAnimation(show_fab_2);
        fab2.setClickable(true);

    }

    private void hideFAB() {

        //Floating Action Button 1
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        layoutParams.rightMargin -= (int) (fab1.getWidth() * 1.7);
        layoutParams.bottomMargin -= (int) (fab1.getHeight() * 0.25);
        fab1.setLayoutParams(layoutParams);
        fab1.startAnimation(hide_fab_1);
        fab1.setClickable(false);

        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
        layoutParams2.rightMargin -= (int) (fab2.getWidth() * 1.5);
        layoutParams2.bottomMargin -= (int) (fab2.getHeight() * 1.5);
        fab2.setLayoutParams(layoutParams2);
        fab2.startAnimation(hide_fab_2);
        fab2.setClickable(false);

    }

}
