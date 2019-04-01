package com.chattynotes.mvp.activities;

import com.chattynoteslite.R;
import com.chattynotes.adapters.location.FoursquareItem;
import com.chattynotes.swipe.rfit.API;
import com.chattynotes.swipe.rfit.converter.StringConverterFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.chattynotes.constant.keyboard.KeyboardVar;
import com.chattynotes.constant.IntentKeys;
import com.chattynotes.constant.MsgKind;
import com.chattynotes.util.NetworkUtil;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.chattynotes.adapters.location.AdapterLocationPicker;
import com.chattynotes.adapters.location.GPSTracker;
import com.chattynotes.adapters.location.GPSTracker.notifyLocationVariation;
import com.chattynotes.adapters.location.InterfaceLocation;
import com.chattynotes.adapters.location.StringLocationItem;
import com.chattynotes.util.PermissionUtil;
import android.Manifest;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v7.app.ActionBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import android.support.annotation.NonNull;

public class SendLocation extends AppCompatActivity implements
		OnItemClickListener, Callback<String>, notifyLocationVariation, OnMapReadyCallback {

	Boolean canGoBack = true;

	ArrayList<InterfaceLocation> fourSquareList;
	protected double latitude, longitude;

	// Google Map
	private GoogleMap googleMap;
	int original_height_map;
	boolean check_map_height;

	// GPSTracker class
	GPSTracker gps;

	ListView listview;
	AdapterLocationPicker adapter;

	//Layouts
	RelativeLayout layout_maps, layout_sendLoc;
	ImageView imgView_zoomIn, imgView_zoomOut;
	Button btn_sendLoc, btn_sendLocFull;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_picker);

		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_send_location);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(true);

		//layout
		layout_maps 	= (RelativeLayout) findViewById(R.id.rl_maps);
		layout_sendLoc 	= (RelativeLayout) findViewById(R.id.rl_nearby);
		imgView_zoomIn 	= (ImageView) findViewById(R.id.imgView_zoomIn);
		imgView_zoomOut = (ImageView) findViewById(R.id.imgView_zoomOut);
		btn_sendLoc 	= (Button) findViewById(R.id.btn_sendLoc);
		btn_sendLocFull = (Button) findViewById(R.id.btn_sendLocFull);

		gps = new GPSTracker(this);
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
		} else
			gps.showSettingsAlert();

		styleAccuracy();

		initializeMap();

		listview = (ListView) findViewById(R.id.list_location_picker);
		listview.setOnItemClickListener(this);

		//if permission call web service
		boolean checkPermission = PermissionUtil.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, getString(R.string.permission_location_send_request), getString(R.string.permission_location_send), R.drawable.permission_location, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_NO_PHOTO, PermissionUtil.PERMISSION_LOCATION);
		if (checkPermission)
			wsc4SquareDate();
	}

//_________________________________________________________________________________________________________________
	void wsc4SquareDate() {
		if (NetworkUtil.checkInternet()) {
			canGoBack = false;
			listview.setVisibility(View.GONE);
			findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
			String baseUrl = KeyboardVar.baseUrl4Square;
			Retrofit retrofit = new Retrofit.Builder()
					.baseUrl(baseUrl)
					.addConverterFactory(StringConverterFactory.create())
					.build();
			API api = retrofit.create(API.class);
			String url = KeyboardVar.serverUrl4Square(latitude, longitude);
			Call<String> call = api.fourSquareLocation(url);
			call.enqueue(this);
		} else
			NetworkUtil.internetNotAvailableDialog(this);
	}

	@Override
	public void onResponse(Call<String> call, Response<String> response) {
		//LogUtil.e("SendLocation:wsc4SquareDate", "success-" + response.body());
		wsr4SquareDate(response.body());
//............................................................................................... MANDATORY STUFF
		listview.setEnabled(true);
		canGoBack = true;
	}

	void wsr4SquareDate(String response) {
		if(response != null) {
			//load list
			fourSquareList = parse4SquareJson(response);
			fourSquareList.add(new StringLocationItem(getString(R.string.location_4square)));
			adapter = new AdapterLocationPicker(this, fourSquareList);
			listview.setAdapter(adapter);
			//list visible
			findViewById(R.id.loadingPanel).setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
			//adding markers
			addPlaceMarkersOnMap(fourSquareList);
		} else
			NetworkUtil.NoResponseToast();
	}

	@Override
	public void onFailure(Call<String> call, Throwable t) {
		NetworkUtil.internetNotAvailableToast();
//............................................................................................... MANDATORY STUFF
		listview.setEnabled(true);
		canGoBack = true;
	}

	private ArrayList<InterfaceLocation> parse4SquareJson(final String completeResponse) {
		ArrayList<InterfaceLocation> temp = new ArrayList<>();
		try {
			JSONObject jO_completeResponse = new JSONObject(completeResponse);
			//each 'meta', 'response', are objects
			String response =  jO_completeResponse.getString("response");

			//---------->>>>   response is a JSON Object which contains 'venues' and 'confident'
			JSONObject jO_response = new JSONObject(response);
			String venues =  jO_response.getString("venues");
			JSONArray jA_venues = new JSONArray(venues);

			for (int i = 0; i < jA_venues.length(); i++)
			//jA_venues have multiple JSON Objects 0,1,2..etc
			{
				//LogUtil.e("--->>", i + ":" + jA_venues.length() + ":" + temp.size() + ":");

				FoursquareItem item = new FoursquareItem();
				//here in first loop we are dealing with 0
				JSONObject jO_venues = jA_venues.getJSONObject(i);

				if(jO_venues.has("id"))
					item.id = jO_venues.getString("id");
				if(jO_venues.has("name"))
					item.name = jO_venues.getString("name");
				if(jO_venues.has("url"))
					item.url = jO_venues.getString("url");
				else
					item.url = "https://foursquare.com/v/" + item.id; //if no URL open the place at 4Square

				JSONObject 	jO_location 	= new JSONObject(jO_venues.getString("location"));
				JSONArray 	jA_categories 	= new JSONArray(jO_venues.getString("categories"));

				//location JSONObject
				if(jO_location.has("address"))
					item.loc_address = jO_location.getString("address");
				if(jO_location.has("country"))
					item.loc_address += " (" + jO_location.getString("country") + ")";
				if(jO_location.has("lat"))
					item.loc_lat = jO_location.getString("lat");
				if(jO_location.has("lng"))
					item.loc_lng = jO_location.getString("lng");

				//categories JSONArray
				//we only need to deal with 1st JSON object of category so no loop
				if(jA_categories.length()>0) {
					JSONObject jO_category = jA_categories.getJSONObject(0);
					if(jO_category.has("name"))
						item.cat_name = jO_category.getString("name");
				}

				//type
				item.type = "1";

				temp.add(item);
			}
		} catch(Exception ignored){
		}
		return temp;
	}

//_____________________________________________________________________________________________________________ CLICK
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//LogUtil.e(getClass().getSimpleName(), "item click : " + position);
		FoursquareItem item = (FoursquareItem)adapter.getItem(position);
		String jsonData = customJSONString(item);
		//encode base64
		String base64 = Base64.encodeToString(jsonData.getBytes(), Base64.NO_WRAP);//NO_WRAP to remove line breaks
		//LogUtil.e(getClass().getSimpleName(), "item click : Base64 : " + base64);
		finishActivity(base64, MsgKind.ADDRESS);
	}

	public void onClickSendLoc(View v) {
		//FoursquareItem item = new FoursquareItem("", "", "", String.valueOf(latitude), String.valueOf(longitude), "", "", "0");
		finishActivity(latitude + "," + longitude, MsgKind.LOCATION);
	}

	public void finishActivity(String msg, int msgKind) {
		Intent in = new Intent();
		in.putExtra(IntentKeys.MSG_KIND, msgKind);
		in.putExtra(IntentKeys.MSG , msg);
		setResult(RESULT_OK, in);
		this.finish();
	}

	//need to generate this
	/*
	 {
		   "name":"Nursery",
		   "id":"4e5cab081495cac4193c46db",
		   "type":"1",
		   "location": {
		     "lat":"24.860697",
		     "address":"Off Shahrah-e-Faisal",
		     "lng":"67.062153"
		   },
		   "categories": [
		     {
		       "name":"Market"
		     }
		   ]
	}*/
	public String customJSONString(FoursquareItem item) {
		try {
			JSONObject JSON = new JSONObject();
			JSONObject jO_loc = new JSONObject();
			JSONArray jA_cat = new JSONArray();
			JSONObject jO_cat = new JSONObject();

			//location
			jO_loc.put("lat", item.loc_lat);
			jO_loc.put("address", item.loc_address);
			jO_loc.put("lng", item.loc_lng);
			//categories
			jO_cat.put("name", item.cat_name);
			jA_cat.put(jO_cat);

			//main JSON
			JSON.put("name"	, item.name);
			JSON.put("id"	, item.id);
			JSON.put("url"	, item.url);
			JSON.put("type"	, item.type);
			JSON.put("location", jO_loc);
			JSON.put("categories", jA_cat);

			return JSON.toString();
		} catch (JSONException ignored) {
		}
		return null;
	}


	public void onClickZoomIn(View v) {
		original_height_map = layout_maps.getHeight();
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			RelativeLayout.LayoutParams maps_layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			//nearby_layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.relativeLayout_maps);

			btn_sendLoc.setVisibility(View.GONE);
			btn_sendLoc.setClickable(false);
			btn_sendLocFull.setVisibility(View.VISIBLE);
			btn_sendLocFull.setClickable(true);

			layout_maps.setLayoutParams(maps_layoutParams);
			RelativeLayout.LayoutParams nearby_layoutParams = new RelativeLayout.LayoutParams(getDisplayWidth()/2, LayoutParams.MATCH_PARENT);
			//nearby_layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			nearby_layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.rl_maps);
			layout_sendLoc.setLayoutParams(nearby_layoutParams);
		} else {
			//LogUtil.e(getClass().getSimpleName(), String.valueOf(btn_sendLoc.getHeight()));
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			layout_maps.setLayoutParams(layoutParams);
		}
		imgView_zoomIn.setVisibility(View.GONE);
		imgView_zoomIn.setClickable(false);
		imgView_zoomOut.setVisibility(View.VISIBLE);
		imgView_zoomOut.setClickable(true);

	}

	public void onClickZoomOut(View v) {

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			RelativeLayout.LayoutParams maps_layoutParams = new RelativeLayout.LayoutParams(getDisplayWidth()/2, LayoutParams.MATCH_PARENT);
			maps_layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			maps_layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.rl_nearby);
			RelativeLayout.LayoutParams nearby_layoutParams = new RelativeLayout.LayoutParams(getDisplayWidth()/2, LayoutParams.MATCH_PARENT);
			nearby_layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			//nearby_layoutParams.addRule(RelativeLayout.RIGHT_OF,R.id.relativeLayout_maps);
			layout_sendLoc.setLayoutParams(nearby_layoutParams);

			btn_sendLoc.setVisibility(View.GONE);
			btn_sendLoc.setClickable(false);
			btn_sendLocFull.setVisibility(View.VISIBLE);
			btn_sendLocFull.setClickable(true);

			layout_maps.setLayoutParams(maps_layoutParams);
		} else {
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, original_height_map);
			layout_maps.setLayoutParams(layoutParams);
		}
		imgView_zoomIn.setVisibility(View.VISIBLE);
		imgView_zoomIn.setClickable(true);
		imgView_zoomOut.setVisibility(View.GONE);
		imgView_zoomOut.setClickable(false);
	}

//______________________________________________________________________________________________________________
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if(canGoBack)
					finish();
				return false;
			case R.id.menu_refresh:
				if(canGoBack)//means call web-service only once at a time, not multiple times simultaneously
					wsc4SquareDate();
				return false;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (canGoBack)
			finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_location_picker, menu);
		return super.onCreateOptionsMenu(menu);
	}


//______________________________________________________________________________________________________________ ORIENTATION CHANGE
	public int getDisplayWidth() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size.x; //size.x = width
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (!check_map_height) {
			original_height_map = layout_maps.getHeight();
			//LogUtil.e(getClass().getSimpleName(), "Map Height : " + String.valueOf(original_height_map));
			check_map_height = true;
		}

		// Checks the orientation of the screen
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			//LogUtil.e(getClass().getSimpleName(), "onConfigurationChanged: ORIENTATION_LANDSCAPE");
			RelativeLayout.LayoutParams maps_layoutParams = new RelativeLayout.LayoutParams(getDisplayWidth()/2, LayoutParams.MATCH_PARENT);
			maps_layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			maps_layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.rl_nearby);
			RelativeLayout.LayoutParams nearby_layoutParams = new RelativeLayout.LayoutParams(getDisplayWidth()/2, LayoutParams.MATCH_PARENT);
			nearby_layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			//nearby_layoutParams.addRule(RelativeLayout.RIGHT_OF,R.id.relativeLayout_maps);
			layout_sendLoc.setLayoutParams(nearby_layoutParams);

			btn_sendLocFull.setVisibility(View.VISIBLE);
			btn_sendLocFull.setClickable(true);
			btn_sendLoc.setVisibility(View.GONE);
			btn_sendLoc.setClickable(false);

			layout_maps.setLayoutParams(maps_layoutParams);

		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			//LogUtil.e(getClass().getSimpleName(), "onConfigurationChanged: ORIENTATION_PORTRAIT");
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, original_height_map);

			btn_sendLocFull.setVisibility(View.GONE);
			btn_sendLocFull.setClickable(false);
			btn_sendLoc.setVisibility(View.VISIBLE);
			btn_sendLoc.setClickable(true);

			layout_maps.setLayoutParams(layoutParams);
			RelativeLayout.LayoutParams nearby_layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			nearby_layoutParams.addRule(RelativeLayout.BELOW, R.id.rl_maps);
			layout_sendLoc.setLayoutParams(nearby_layoutParams);
		}
	}

	@Override
	public void locationChange() {
		if(gps != null) {
			styleAccuracy();
			if (gps.canGetLocation()) {
				latitude = gps.getLatitude();
				longitude = gps.getLongitude();
			} else
				gps.showSettingsAlert();
		}
	}

//__________________________________________________________________________________________
	void styleAccuracy() {
		String text = "Send your current location\nAccurate to " + (int)gps.getAccuracy() + " meters";
		btn_sendLoc.setText(text);
		btn_sendLocFull.setText(text);
	}

//__________________________________________________________________________________________
	@Override
	protected void onDestroy() {
		gps.stopUsingGPS();
		gps.stopSelf();
		gps = null;
		super.onDestroy();
	}

//__________________________________________________________________________________________________
	//[os_marshmallow_: permission]
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			switch (requestCode) {
				case PermissionUtil.PERMISSION_LOCATION:
					if(canGoBack)//means call web-service only once at a time, not multiple times simultaneously
						wsc4SquareDate();
					break;
				default:
					super.onRequestPermissionsResult(requestCode, permissions, grantResults);
			}
		} else {
			Toast.makeText(this, PermissionUtil.permission_not_granted, Toast.LENGTH_SHORT).show();
		}
	}


//_______________________________________________________________________________________________________
	//function to load map. If map is not created it will create it for you
	private void initializeMap() {
		if (googleMap == null) {
			//http://stackoverflow.com/questions/24448378/java-lang-nullpointerexception-cameraupdatefactory-is-not-initialized-logcat-ex
			MapsInitializer.initialize(this);
			//getMap() depreciated in NOUGAT__
			MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
			mapFragment.getMapAsync(this);
		}
	}

	private void addPlaceMarkersOnMap(ArrayList<InterfaceLocation> _fourSquareList) {
		//adding Green Markers on the Maps based on the 4Square response
		for (InterfaceLocation i : _fourSquareList) {
			try {
				FoursquareItem item = (FoursquareItem)i;
				//create marker
				LatLng l = new LatLng(Double.parseDouble(item.loc_lat), Double.parseDouble(item.loc_lng));
				MarkerOptions marker = new MarkerOptions().position(l).title(item.name);
				//Changing marker icon
				marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
				//adding marker
				googleMap.addMarker(marker);
			} catch (Exception ignored) {
			}
		}
	}


	//http://stackoverflow.com/a/31371953/4754141
	@Override
	public void onMapReady(GoogleMap _googleMap) {
		googleMap = _googleMap;
		setUpMap();
	}

	public void setUpMap() {
		try {
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(latitude, longitude)) // Sets the center of the map to location user
					.zoom(17) // Sets the zoom
					.build(); // Creates a CameraPosition from the builder
			googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			googleMap.getUiSettings().setCompassEnabled(true);

			googleMap.setMyLocationEnabled(true);
			/* not required
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			googleMap.setTrafficEnabled(true);
			googleMap.setIndoorEnabled(true);
			googleMap.setBuildingsEnabled(true);
			googleMap.getUiSettings().setZoomControlsEnabled(true); */

		} catch(Exception ignored){
		}
		// check if map is created successfully or not
		if (googleMap == null) {
			Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
		}

	}
}
