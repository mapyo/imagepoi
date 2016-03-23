package com.mapyo.imagepoi;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class FileUtil {
    public static String getFilePath(Context context, Uri fileUri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = null;
        if (contentResolver != null) {
            cursor = contentResolver.query(fileUri, projection, null, null, null);
        }
        if (cursor != null && cursor.getCount() == 1) {
            cursor.moveToNext();
            return cursor.getString(0);
        } else {
            return "";
        }
    }
}
