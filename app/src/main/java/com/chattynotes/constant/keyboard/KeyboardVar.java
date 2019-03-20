package com.chattynotes.constant.keyboard;

public class KeyboardVar {

    public static String baseUrl4Square = "https://api.foursquare.com/";
    public static String serverUrl4Square(double lat, double lon) {
        return "https://api.foursquare.com/v2/venues/search?client_id=" + Keyboard.FOURSQUARE_CLIENT_ID.getValue() + "&client_secret="	+ Keyboard.FOURSQUARE_CLIENT_SECRET.getValue() + "&v=20130815&ll=" + String.valueOf(lat) + "," + String.valueOf(lon);
    }
    //Static Maps API V2 Developer Guide
    //https://developers.google.com/maps/documentation/staticmaps/
    //https://maps.google.com/maps/api/staticmap?center=" + latitude + "," + longitude + "&zoom=15&size=200x200&sensor=false
    //https://maps.google.com/maps/api/staticmap?center=24.8600,67.0199&zoom=17&size=500x240&markers=color:red%7C24.8600,67.0199
    public static String locationUrl(String coordinates) {
        return "https://maps.google.com/maps/api/staticmap?center=" + coordinates + "&zoom=22.0F&size=500x240&markers=color:red%7C" + coordinates;
    }
}
