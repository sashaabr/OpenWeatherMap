package com.courses.android.openweathermap;

import android.graphics.drawable.BitmapDrawable;
import android.widget.RelativeLayout;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Asus on 06.03.2016.
 */
public class HTTPClient {
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String MY_APP_ID = "&appid=aa3b5a200b905b03e87b64ec1f33f3c9";
    private static final String IMG_URL = "http://openweathermap.org/img/w/";


    public String getWeatherData(String location) {//ород
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) (new URL(BASE_URL + location + MY_APP_ID)).openConnection();//ссылка на джейсон+ location + MY_APP_ID
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);

            InputStream content = connection.getInputStream();

            // Читаем полностью входное поток данных
            StringBuilder builder = new StringBuilder(128);
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
            reader.close();

            return builder.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;

    }

    public byte[] getImage(String code) {
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) (new URL(IMG_URL + code + ".png")).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            // con.setDoOutput(true);-из-за этого картинку непоказывало
            con.connect();


            InputStream is = con.getInputStream();
            byte[] buffer = new byte[8192];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while (is.read(buffer) != -1) {
                baos.write(buffer);
            }

            return baos.toByteArray();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        return null;

    }
}
