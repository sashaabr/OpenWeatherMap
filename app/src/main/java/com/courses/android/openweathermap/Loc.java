package com.courses.android.openweathermap;

import java.io.Serializable;

/**
 * Created by Asus on 06.03.2016.
 */
public class Loc implements Serializable{
    private String longitude;//ff
    private String latitude;//f
    private long sunset;
    private long sunrise;
    private String country;
    private String city;

    public Loc()
    {}


    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String  longitude) {
        this.longitude = longitude;
    }//f
    public String  getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }//// FIXME: 13.03.2016
    public long getSunset() {
        return sunset;
    }
    public void setSunset(long sunset) {
        this.sunset = sunset;
    }
    public long getSunrise() {
        return sunrise;
    }
    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
}
