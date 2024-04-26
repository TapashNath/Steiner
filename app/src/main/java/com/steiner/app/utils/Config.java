package com.steiner.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.steiner.app.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class Config {

    public static final int REQUEST_CHECK_CODE = 22;
    public static int NOTIFICATION_ID = 0;
    //    public static String MainUrl = "http://192.168.1.100/INTERIOR/API/";
    public static String MainUrl = "http://itstpssolution.com/interior/SteinerAdmin/";
    public static String ImageUrl = "http://steiner.itstpssolution.com/";
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME = "com.pariseva.in.Utils";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String RESULT_DATA_ST = PACKAGE_NAME + ".RESULT_DATA_ST";
    public static final String RESULT_DATA_DT = PACKAGE_NAME + ".RESULT_DATA_DT";
    public static final String RESULT_DATA_CN = PACKAGE_NAME + ".RESULT_DATA_CN";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
    public static final String RESULT_DATA_PIN = PACKAGE_NAME + ".RESULT_DATA_PIN";
    public static final String RESULT_DATA_CT = PACKAGE_NAME + ".RESULT_DATA_CT";
    public static final String RESULT_DATA_LLT = PACKAGE_NAME + ".RESULT_DATA_LLT";
    public static final String RESULT_DATA_LLT2 = PACKAGE_NAME + ".RESULT_DATA_LLT2";

    public static void showLongToast(String message) {
        Toast.makeText(MyApplication.context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context mContext, String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public static void showShortToast(Context mContext, String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public static void Log(String message) {
        Log.d("LOGS", message);
    }

    public static void showLongGravityToast(Context mContext, String message) {
        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void addToolbar(View view, AppCompatActivity activity, int ToolBarId) {
        Toolbar toolbar = view.findViewById(ToolBarId);
        activity.setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar
        Objects.requireNonNull(activity.getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(activity, R.color.appColor));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void addToolbarInActivity(Toolbar toolbar, AppCompatActivity activity) {
        activity.setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar
        Objects.requireNonNull(activity.getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(activity, R.color.appColor));
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void addToolbarWithNAme(View view, AppCompatActivity activity, int ToolBarId, String name) {
        Toolbar toolbar = view.findViewById(ToolBarId);
        activity.setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar
        Objects.requireNonNull(activity.getSupportActionBar()).setTitle(Html.fromHtml("<font color='#FFFFFF'>" + name + "</font>"));
        @SuppressLint("UseCompatLoadingForDrawables") final Drawable upArrow = activity.getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        upArrow.setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upArrow);

        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(activity, R.color.appColor));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void addToolbarWithNoBack(View view, AppCompatActivity activity, int ToolBarId, String name) {
        Toolbar toolbar = view.findViewById(ToolBarId);
        activity.setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar
        Objects.requireNonNull(activity.getSupportActionBar()).setTitle(Html.fromHtml("<font color='#FFFFFF'>" + name + "</font>"));
//        @SuppressLint("UseCompatLoadingForDrawables") final Drawable upArrow = activity.getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
//        upArrow.setColorFilter(activity.getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
//        toolbar.setNavigationIcon(upArrow);

        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(activity, R.color.appColor));
    }


    public static String compressImage(String imageUri, Activity activity) {
        String filename = "";
        try {
            String filePath = getRealPathFromURI(imageUri, activity);
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;
            float maxHeight = 816.0f;
            float maxWidth = 612.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
                bmp = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas;
            if (scaledBitmap != null) {
                canvas = new Canvas(scaledBitmap);
                canvas.setMatrix(scaleMatrix);
                canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
            }


            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);

                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);

                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);

                } else if (orientation == 3) {
                    matrix.postRotate(180);

                } else if (orientation == 8) {
                    matrix.postRotate(270);

                }
                if (scaledBitmap != null) {
                    scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream out;
            Date d = new Date();

            filename = String.valueOf(d.getTime());
            try {
                out = new FileOutputStream(filename);
                if (scaledBitmap != null) {
                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filename;
    }



    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private static String getRealPathFromURI(String contentURI, Activity activity) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = activity.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
}
