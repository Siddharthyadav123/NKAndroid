package com.netkoin.app.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.netkoin.app.constants.Constants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ashishkumarpatel on 1/5/2017.
 */
public class SharedPref {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    // Shared preferences file name
    private static final String PREF_NAME = "netkoin_pref";

    //to save user specific info
    public static String KEY_AUTH_TOKEN = "KEY_AUTH_TOKEN";
    public static String KEY_USER_ID = "KEY_USER_ID";

    //for silent login
    public static String KEY_SILENT_LOGIN_REQUEST_PARAM = "KEY_SILENT_LOGIN_REQUEST_PARAM";
    public static String KEY_SILENT_LOGIN_TYPE = "KEY_SILENT_LOGIN_TYPE";

    //for SETTINGS
    public static String KEY_SETTING_SOUND_ENABLED = "KEY_SOUND_ENABLED";
    public static String KEY_SETTING_ID = "KEY_SETTING_ID";
    public static String KEY_SETTING_NOTI_KOINS_REC_VIA_STEP_IN = "KOINS_REC_VIA_STEP_IN";
    public static String KEY_SETTING_NOTI_NEAR_BY_STORES = "NEAR_BY_STORES";
    public static String KEY_SETTING_NOTI_KOIN_REC_VIA_TRANSFER = "KOIN_REC_VIA_TRANSFER";

    public static String KEY_SETTING_DIS_NEAR_BY_STORES = "DIS_NEAR_BY_STORES";
    public static String KEY_SETTING_DIS_NEAR_BY_TRENDING_ADS = "DIS_NEAR_BY_TRENDING_ADS";
    public static String KEY_SETTING_DIS_CAT_ADS = "DIS_CAT_ADS";

    //for location
    public static String KEY_SELECTED_LOC = "SELECTED_LOC";
    public static String KEY_SELECTED_LOC_LAT = "SELECTED_LOC_LAT";
    public static String KEY_SELECTED_LOC_LONG = "SELECTED_LOC_LONG";

    //for previous location
    public static String KEY_SELF_LOC_LAT = "KEY_SELF_LOC_LAT";
    public static String KEY_SELF_LOC_LONG = "KEY_SELF_LOC_LONG";

    public static String KEY_DICTIONARY_NEAR_BY_STORE = "KEY_DICTIONARY_NEAR_BY_STORE";


    public SharedPref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void put(String key, Object value) {
        if (value == null) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, ((Integer) value).intValue());
        } else if (value instanceof Float) {
            editor.putFloat(key, ((Float) value).floatValue());
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, ((Boolean) value).booleanValue());
        }
        boolean saved = editor.commit();

        if (saved) {
            //System.out.println(">>saved >>" + key);
        } else {
            //System.out.println(">>unable to save >>" + key);
        }
    }

    public boolean getBoolean(String key) {
        return pref.getBoolean(key, false);
    }

    public String getString(String key) {
        return pref.getString(key, null);
    }

    public int getInt(String key) {
        return pref.getInt(key, 0);
    }

    public boolean getSettingBool(String key) {
        return pref.getBoolean(key, true);
    }

    public int getIntWithMinusOneAsDefault(String key) {
        return pref.getInt(key, -1);
    }

    public float getFloat(String key) {
        return pref.getFloat(key, 0.0f);
    }


    // method to clear all the values from shared pref. called on logout
    public void clearSharedPref() {
        pref.edit().clear().commit();
    }

    ////////////----- getter setters for distance setting

    public int getSettingDistanceByKey(String key) {
        int distance = pref.getInt(key, 100);
        //returning default distance
        return distance;
    }

    public void setSettingDistanceByKey(String key, int distance) {
        put(key, distance);
    }

    //to show the location
    public String getSelectedLocationString() {
        String location = getString(KEY_SELECTED_LOC);
        //returning default distance
        if (location == null) {
            return Constants.CURRENT_LOCATION_TEXT;
        }
        return location;
    }

    public void putMap(Map<String, String> inputMap, String key) {
        JSONObject jsonObject = new JSONObject(inputMap);
        String jsonString = jsonObject.toString();
        editor.remove(key).commit();
        editor.putString(key, jsonString).commit();
    }

    public Map<String, String> getMap(String mapKey) {
        Map<String, String> outputMap = new HashMap<String, String>();
        try {
            String jsonString = pref.getString(mapKey, (new JSONObject()).toString());
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<String> keysItr = jsonObject.keys();
            while (keysItr.hasNext()) {
                String key = keysItr.next();
                String value = (String) jsonObject.get(key);
                outputMap.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputMap;
    }
}
