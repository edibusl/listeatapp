package com.edibusl.listeatapp.helpers;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GeneralUtils {
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
            // PNG is a lossless format, the compression factor (100) is ignored
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

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Nullable
    public static Date parseDateFromJsonString(String str){
        //String date format: 2018-03-17T00:00:00+02:00
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
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
