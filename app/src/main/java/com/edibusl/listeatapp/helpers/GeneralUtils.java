package com.edibusl.listeatapp.helpers;

import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GeneralUtils {
    @Nullable
    public static Date parseDateFromJsonString(String str){
        //String date format: 2018-03-17T00:00:00+02:00
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH);
        try {
            Date dateObj = format.parse(str);
            return dateObj;
        }
        catch(Exception ex)
        {
            return null;
        }
    }
}