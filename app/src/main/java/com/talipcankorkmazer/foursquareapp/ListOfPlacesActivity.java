package com.talipcankorkmazer.foursquareapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.LENGTH_LONG;

public class ListOfPlacesActivity extends AppCompatActivity {
	private ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_of_places);
		final ArrayList<String> names = (ArrayList<String>) getIntent().getSerializableExtra("mname");
		final ArrayList<Double> latitudes = (ArrayList<Double>) getIntent().getSerializableExtra("mlat");
		final ArrayList<Double> longitudes = (ArrayList<Double>) getIntent().getSerializableExtra("mlng");

		setContentView(R.layout.activity_list_of_places);

		lv = (ListView) findViewById(R.id.places);

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				this,
				android.R.layout.simple_list_item_1,
				names);

		lv.setAdapter(arrayAdapter);

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_LONG).show();
				Log.i("Position : ", String.valueOf(position));

				double selectedLatitude = latitudes.get(position);
				double selectedLongitude = longitudes.get(position);
				String uri = String.format(Locale.ENGLISH, "geo:%f,%f", selectedLatitude, selectedLongitude);
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
				startActivity(intent);
			}
		});
	}
}
