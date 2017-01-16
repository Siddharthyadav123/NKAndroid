package com.netkoin.app.custom_views.autocompleteview;

import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpConnector {

	public static String getResponse(String urlString){
		Log.d("HttpConnector", "URL ==> "+urlString);
	    HttpURLConnection conn = null;
	    StringBuilder jsonResults = new StringBuilder();
	    try {

	        URL url = new URL(urlString);
	        conn = (HttpURLConnection) url.openConnection();
	        InputStreamReader in = new InputStreamReader(conn.getInputStream());

	        // Load the results into a StringBuilder
	        int read;
	        char[] buff = new char[512];
	        while ((read = in.read(buff)) != -1) {
	            jsonResults.append(buff, 0, read);
	        }
	        
	        
	    } catch (MalformedURLException e) {
	        Log.e("AppUtil", "Error processing Autocomplete API URL", e);
	    } catch (IOException e) {
	        Log.e("AppUtil", "Error connecting to Autocomplete API", e);
	    } finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }
	    Log.d("Result", jsonResults.toString());
	    return jsonResults.toString();
	}
}
