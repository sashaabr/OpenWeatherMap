package com.courses.android.openweathermap;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Asus on 06.03.2016.
 */
public class JSONParser {
    public static Weather getWeather(String data) throws JSONException {
        Weather weather = new Weather();

        // We create out JSONObject from the data
        JSONObject jObj = new JSONObject(data);
        if (jObj.has("cod")) {
            int code = jObj.optInt("cod");
            String message = jObj.optString("message");
            if (code == 404) {
                // not found city
                weather.hasError = true;
                weather.errorMessage = message;
                return weather;
            }
        }

        // We start extracting the info
        Loc loc = new Loc();


        // "coord":{
        //      "lon":-0.13,
        //     "lat":51.51
        //  },
        JSONObject coordObj = getObject("coord", jObj);
        loc.setLongitude(getString("lon", coordObj));
        loc.setLatitude(getString("lat", coordObj));

        // "weather":[
        //   {
        //     "id":801,
        //          "main":"Clouds",
        //        "description":"few clouds",
        //          "icon":"02d"
        //   }
        //  ],
        JSONArray jArr = jObj.getJSONArray("weather");
        JSONObject JSONWeather = jArr.getJSONObject(0);
        weather.currentCondition.setWeatherId(getInt("id", JSONWeather));
        weather.currentCondition.setDescr(getString("description", JSONWeather));
        weather.currentCondition.setIcon(getString("icon", JSONWeather));

        /**  "main":{
         "temp":282.729,
         "pressure":1033.77,
         "humidity":84,
         "temp_min":282.729,
         "temp_max":282.729,
         "sea_level":1044,
         "grnd_level":1033.77
         },*/

        JSONObject mainObj = getObject("main", jObj);
        weather.currentCondition.setPressure(getFloat("humidity", mainObj));
        weather.currentCondition.setHumidity(getFloat("pressure", mainObj));
        weather.temperature.setMinTemp(getFloat("temp_min", mainObj));
        weather.temperature.setMaxTemp(getFloat("temp_max", mainObj));
        weather.temperature.setTemp(getFloat("temp", mainObj));

        /***
         "wind":{
         "speed":2.01,
         "deg":154.501
         }
         */
        // Wind
        JSONObject wObj = getObject("wind", jObj);
        weather.wind.setSpeed(getFloat("speed", wObj));
        weather.wind.setDeg(getFloat("deg", wObj));

        /***
         "clouds":{
         "all":24
         }
         */

        JSONObject cObj = getObject("clouds", jObj);
        weather.clouds.setPerc(getInt("all", cObj));

        // We download the icon to show
       /* "dt":1457797625,
                "sys":{
            "message":0.0053,
                    "country":"GB",
                    "sunrise":1457763545,
                    "sunset":1457805705
        },
        "id":2643743,
                "name":"London",
                "cod":200

*/
        JSONObject sysObj = getObject("sys", jObj);
        loc.setCountry(getString("country", sysObj));
        loc.setSunrise(getInt("sunrise", sysObj));
        loc.setSunset(getInt("sunset", sysObj));
        loc.setCity(getString("name", jObj));
        weather.location = loc;
        return weather;

    }

    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }
}
