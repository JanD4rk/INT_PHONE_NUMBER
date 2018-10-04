package com.voidaint.intphonenumber;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;

import net.rimoto.intlphoneinput.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Utils {

    /**
     * Fetch JSON from RAW resource
     *
     * @param context  Context
     * @param resource Resource int of the RAW file
     * @return JSON
     */
    private static String getJsonFromRaw(Context context, int resource) {
        String json;
        try {
            InputStream inputStream = context.getResources().openRawResource(resource);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }





    /**
     * Import CountryList from RAW resource
     *
     * @param context Context
     * @return CountryList
     */

    public static int getColor (Context context,int colorId){
        return ContextCompat.getColor(context, colorId);
    }
    public static Drawable getDrawable (Context context, int colorId){
        return ContextCompat.getDrawable(context, colorId);
    }

    public static CountryList getCountries(Context context) {
        CountryList countryList = new CountryList();
        countryList.addAll(Arrays.asList((new Gson().fromJson(getJsonFromRaw(context, R.raw.countries), Country[].class))));

        return countryList;
    }
    public static int getFlagResource(Country country,Context context) {
        return context.getResources().getIdentifier("country_" + country.getIso2().toLowerCase(), "drawable", context.getPackageName());
    }

    public static class CountryList extends ArrayList<Country> {
        /**
         * Fetch item index on the list by iso
         *
         * @param iso Country's iso2
         * @return index of the item in the list
         */
        public int indexOfIso(String iso) {
            for (int i = 0; i < this.size(); i++) {
                if (this.get(i).getIso2().toUpperCase().equals(iso.toUpperCase())) {
                    return i;
                }
            }
            return -1;
        }

        /**
         * Fetch item index on the list by dial coder
         *
         * @param dialCode Country's dial code prefix
         * @return index of the item in the list
         */
        @SuppressWarnings("unused")
        public int indexOfDialCode(int dialCode) {
            for (int i = 0; i < this.size(); i++) {
                if (this.get(i).getDialCode() == dialCode) {
                    return i;
                }
            }
            return -1;
        }
    }
}
