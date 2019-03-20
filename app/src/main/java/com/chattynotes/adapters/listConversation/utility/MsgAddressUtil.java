package com.chattynotes.adapters.listConversation.utility;

import android.util.Base64;

import com.chattynotes.adapters.location.FoursquareItem;

import org.json.JSONArray;
import org.json.JSONObject;

public class MsgAddressUtil {

    public static FoursquareItem decodeModel(String base64) {
        FoursquareItem item  = null;
        try {
            byte[] data = Base64.decode(base64, Base64.DEFAULT);
            String jsonString = new String(data, "UTF-8");
            //LogUtil.e("jsonString", jsonString);

            item = new FoursquareItem();
            //here in first loop we are dealing with 0
            JSONObject jO = new JSONObject(jsonString);

            item.id = jO.getString("id");
            item.name = jO.getString("name");
            if(jO.has("url")) //we don't have URL from iOS therefore check
                item.url = jO.getString("url");
            else
                item.url = "https://foursquare.com/v/" + item.id;

            JSONObject jO_location = new JSONObject(jO.getString("location"));
            JSONArray jA_categories	= new JSONArray(jO.getString("categories"));

            //location JSONObject
            item.loc_address = jO_location.getString("address");
            item.loc_lat = jO_location.getString("lat");
            item.loc_lng = jO_location.getString("lng");

            //categories JSONArray
            //we only need to deal with 1st JSON object of category so no loop
            JSONObject jO_category = jA_categories.getJSONObject(0);
            item.cat_name = jO_category.getString("name");

            //type
            item.type = jO.getString("type");

            //LogUtil.e("decodeBase64", item.url + item.loc_lat + "," + item.loc_lng);
            return item;
        } catch (Exception ignored) {
        }
        return item;
    }





}
