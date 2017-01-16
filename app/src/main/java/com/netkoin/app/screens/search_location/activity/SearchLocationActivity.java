package com.netkoin.app.screens.search_location.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.netkoin.app.R;
import com.netkoin.app.base_classes.AbstractBaseActivity;
import com.netkoin.app.constants.Constants;
import com.netkoin.app.constants.RequestConstants;
import com.netkoin.app.custom_views.autocompleteview.AutoCompleteView;
import com.netkoin.app.custom_views.autocompleteview.Place;
import com.netkoin.app.pref.SharedPref;
import com.netkoin.app.volly.APIHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchLocationActivity extends AbstractBaseActivity {

    private AutoCompleteView locationSearchAutocomplete;
    private ListView locationListView;
    private LocationListAdapter locationListAdapter = null;
    private TextView noSearchResultTextView;
    private LinearLayout fetchingGeopointsLinLayout;
    private TextView saveBtnTextView;
    private TextView selectedCurrentLocTextView;
    private ImageView crossBtnImageView;

    private SharedPref sharedPref;


    private ArrayList<Place> places = null;

    private String selectedPlaceName = null;
    private double selectedLat;
    private double selectedLong;

    private String LocationFromPlaceIdURL = "https://maps.googleapis.com/maps/api/place/details/json?";

    @Override
    public View initView() {
        View view = getLayoutInflater().inflate(R.layout.activity_search_location, null);
        locationSearchAutocomplete = (AutoCompleteView) view.findViewById(R.id.locationSearchAutocomplete);
        locationListView = (ListView) view.findViewById(R.id.locationListView);
        noSearchResultTextView = (TextView) view.findViewById(R.id.noSearchResultTextView);
        fetchingGeopointsLinLayout = (LinearLayout) view.findViewById(R.id.fetchingGeopointsLinLayout);
        saveBtnTextView = (TextView) view.findViewById(R.id.saveBtnTextView);
        selectedCurrentLocTextView = (TextView) view.findViewById(R.id.selectedCurrentLocTextView);
        crossBtnImageView = (ImageView) view.findViewById(R.id.crossBtnImageView);
        initActionBarView(view);

        sharedPref = new SharedPref(SearchLocationActivity.this);
        return view;
    }

    @Override
    public void registerEvents() {
        locationSearchAutocomplete.setParser(new AutoCompleteView.AutoCompleteResponseParser() {
            @Override
            public ArrayList<? extends Object> parseAutoCompleteResponse(String response) {

                try {
                    JSONObject jsonObj = new JSONObject(response);
                    final JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

                    places = new ArrayList<Place>();
                    for (int i = 0; i < predsJsonArray.length(); i++) {
                        String placeName = predsJsonArray.getJSONObject(i).getString("description");
                        String place_id = predsJsonArray.getJSONObject(i).getString("place_id");


                        Place place = new Place();
                        place.setName(placeName);
                        place.setPlace_id(place_id);
                        places.add(place);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                refreshLocationAdapter();

                return null;
            }
        });

        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                fetchingGeopointsLinLayout.setVisibility(View.VISIBLE);
                selectedPlaceName = places.get(i).getName();

                String URL = LocationFromPlaceIdURL + "placeid=" + places.get(i).getPlace_id() + "&key=" + Constants.GOOGLE_API_KEY;

                APIHandler apiHandler = new APIHandler(SearchLocationActivity.this, SearchLocationActivity.this, RequestConstants.REQUEST_ID_GET_LOCATION_GEOPOINTS_BY_PLACE_ID,
                        Request.Method.GET, URL, false, null, null);
                apiHandler.setResponseFromOurServer(false);
                apiHandler.requestAPI();

            }
        });

        crossBtnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedCurrentLocTextView.getText().toString().trim().equals(Constants.CURRENT_LOCATION_TEXT)) {
                    return;
                }

                selectedCurrentLocTextView.setText(Constants.CURRENT_LOCATION_TEXT);
                saveBtnTextView.setVisibility(View.VISIBLE);
                selectedLong = 0.0;
                selectedLong = 0.0;
                selectedPlaceName = null;
            }
        });

        saveBtnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedCurrentLocTextView.getText().toString().trim().equals(Constants.CURRENT_LOCATION_TEXT)) {
                    sharedPref.put(SharedPref.KEY_SELECTED_LOC, Constants.CURRENT_LOCATION_TEXT);
                } else {
                    sharedPref.put(SharedPref.KEY_SELECTED_LOC, selectedPlaceName);
                }
                sharedPref.put(SharedPref.KEY_SELECTED_LOC_LAT, new Float(selectedLat));
                sharedPref.put(SharedPref.KEY_SELECTED_LOC_LONG, new Float(selectedLong));

                Toast.makeText(SearchLocationActivity.this, "Location changed successfully.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    private void refreshLocationAdapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (locationListAdapter == null) {
                    locationListAdapter = new LocationListAdapter();
                    locationListView.setAdapter(locationListAdapter);
                } else {
                    locationListAdapter.notifyDataSetChanged();
                }

                if (locationSearchAutocomplete.getText().toString().length() > 0) {
                    if (places == null || places.size() == 0) {
                        noSearchResultTextView.setVisibility(View.VISIBLE);
                    } else {
                        noSearchResultTextView.setVisibility(View.GONE);
                    }
                } else {
                    noSearchResultTextView.setVisibility(View.GONE);
                }

            }
        });

    }

    @Override
    public void updateUI() {
        setActionBarTitle("Select Location");

        String currentLocation = sharedPref.getString(SharedPref.KEY_SELECTED_LOC);
        if (currentLocation == null) {
            selectedCurrentLocTextView.setText(Constants.CURRENT_LOCATION_TEXT);
        } else {
            selectedCurrentLocTextView.setText(currentLocation);
        }

    }

    @Override
    public void onActionBarLeftBtnClick() {
        finish();
    }

    @Override
    public void onActionBarTitleClick() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAPIHandlerResponse(int requestId, boolean isSuccess, Object result, String errorString) {
        switch (requestId) {
            case RequestConstants.REQUEST_ID_GET_LOCATION_GEOPOINTS_BY_PLACE_ID:
                onLocationPlaceIdResponse(result);
                break;
        }
    }

    private void onLocationPlaceIdResponse(Object result) {
        fetchingGeopointsLinLayout.setVisibility(View.GONE);
        try {
            JSONObject jsonObject = new JSONObject(result.toString());

            JSONObject response = jsonObject.getJSONObject("result");
            if (response != null) {
                JSONObject geometry = response.getJSONObject("geometry");
                if (geometry != null) {
                    JSONObject location = geometry.getJSONObject("location");
                    selectedLat = location.getDouble("lat");
                    selectedLong = location.getDouble("lng");

                    saveBtnTextView.setVisibility(View.VISIBLE);
                    selectedCurrentLocTextView.setText(selectedPlaceName);

                    locationSearchAutocomplete.setText("");
                    places = null;
                    refreshLocationAdapter();

                } else {
                    Toast.makeText(SearchLocationActivity.this, "Unable to find Geo-points for the selected Address", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class LocationListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (places != null)
                return places.size();
            else
                return 0;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(SearchLocationActivity.this).inflate(R.layout.row_place, null);
            }

            TextView name = (TextView) view.findViewById(R.id.name);
            name.setText(places.get(i).getName());
            return view;
        }
    }
}
