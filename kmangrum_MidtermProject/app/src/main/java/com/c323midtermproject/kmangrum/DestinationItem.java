package com.c323midtermproject.kmangrum;

import android.content.pm.PackageManager;

// Create a class Destination item
public class DestinationItem {
    private String dName;
    private String dTimeToVisit;
    private String dLocation;

    public DestinationItem(String name, String time, String location) {
        dName = name;
        dTimeToVisit = time;
        dLocation = location;

    }

    public String getName() {
        return  dName;
    }

    public String getTime() {
        return  dTimeToVisit;
    }

    public String getdLocation() {
        return  dLocation;
    }

}
