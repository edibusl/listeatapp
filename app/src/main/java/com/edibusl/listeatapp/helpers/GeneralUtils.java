package com.edibusl.listeatapp.helpers;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GeneralUtils {
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    public static void printErrorToLog(String logTag, Exception ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : ex.toString();
        Log.e(logTag, message);
    }

    public static void printErrorToLog(String logTag, String error) {
        String message = error != null ? error : "";
        Log.e(logTag, message);
    }

    public static void saveBitmapToFile(Bitmap bmpImage, String filename) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            bmpImage.compress(Bitmap.CompressFormat.PNG, 100, out);
            // PNG is a losseless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    public static Date parseDateFromJsonString(String str){

        try {
            Date dateObj = DATE_FORMATTER.parse(str);
            return dateObj;
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    public static String dateToString(Date date){
        if (date == null) {
            return null;
        } else {
            return DATE_FORMATTER.format(date.getTime());
        }
    }
}
