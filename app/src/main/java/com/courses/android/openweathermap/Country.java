package com.courses.android.openweathermap;

/**
 * Created by Asus on 17.03.2016.
 */
public class Country {
    public String name;
    private String country;

    public Country(String _name)
    {
        this.name = _name;
    }
    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setName(String name) {
        this.name = name;
    }
}
