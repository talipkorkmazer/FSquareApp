package com.talipcankorkmazer.foursquareapp;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
	public  static final String TAG  = MainActivity.class.getSimpleName();
	private Button startButton;

	private NearPlaces mNearPlaces;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);














		startButton = (Button) findViewById(R.id.startButton);
		if (isNetworkAvaliable()) {
			startButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String CLIENT_ID = "SRYZDF42VFANQUOVDPJJFV5DOZICP2PFPGPJB4EB4OOIO1DZ";
					String CLIENT_SECRET = "BEH32VGU4BOB5VCKIOUJ0Q4M1O0NIAQPRMMU3K5T5BFHRTUB";

					double latitude = 41.025727;
					double longitude = 29.030372;


					String urlForFsquare = "https://api.foursquare.com/v2/venues/search?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&v=20130815&ll=" + latitude + "," + longitude;


					OkHttpClient client = new OkHttpClient();
					Request request = new Request.Builder().url(urlForFsquare).build();
					Call call = client.newCall(request);
					call.enqueue(new Callback() {
						@Override
						public void onFailure(Call call, IOException e) {
						}

						@RequiresApi(api = Build.VERSION_CODES.KITKAT)
						@Override
						public void onResponse(Call call, Response response) throws IOException {
							try {
								String jsonData = response.body().string();

								Log.d(TAG, jsonData);
								if (response.isSuccessful()) {

									mNearPlaces = getDetails(jsonData);

									listPlaces(mNearPlaces.getMname(), mNearPlaces.getMlat(), mNearPlaces.getMlng());



								} else {
									alertUserAboutError();
								}
							} catch (IOException e) {
								Log.e(TAG, "Exception caught: ", e);
							}
							catch (JSONException e){
								Log.e(TAG, "Exception caught: ", e);
							}
						}
					});
				}

			});
		}else{
			Toast.makeText(this, R.string.network_unavailable, Toast.LENGTH_LONG).show();
		}
	}

	private void listPlaces(List<String> mname, List<Double> mlat, List<Double> mlng) {
		Intent intent = new Intent(this, ListOfPlacesActivity.class);
		intent.putExtra("mname", (Serializable) mname);
		intent.putExtra("mlat", (Serializable) mlat);
		intent.putExtra("mlng", (Serializable) mlng);

		startActivity(intent);
	}


	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	private NearPlaces getDetails(String jsonData) throws JSONException {
		JSONObject jObj = new JSONObject(jsonData);
		JSONObject response = jObj.getJSONObject("response");

		JSONArray venues = response.getJSONArray("venues");

		List<String> names = new ArrayList<String>();
		List<Double> latitudes = new ArrayList<Double>();
		List<Double> longitudes = new ArrayList<Double>();
		for(int i=0;i<venues.length();i++)
		{
			JSONObject jb1 = venues.getJSONObject(i);
			String name = jb1.getString("name");
			JSONObject location = jb1.getJSONObject("location");
			double lat = location.getDouble("lat");
			double lng = location.getDouble("lng");
			names.add(name);
			latitudes.add(lat);
			longitudes.add(lng);
		}
		NearPlaces nearPlaces = new NearPlaces();
		nearPlaces.setMname(names);
		nearPlaces.setMlat(latitudes);
		nearPlaces.setMlng(longitudes);

		return nearPlaces;
	}

	private boolean isNetworkAvaliable() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		boolean isAvaliable = false;
		if(networkInfo != null && networkInfo.isConnected()){
			isAvaliable = true;
		}
		return isAvaliable;
	}
	private void alertUserAboutError() {
		AlertDialogFragment dialog = new AlertDialogFragment();
		dialog.show(getFragmentManager(), "error_dialog");
	}

}
