package com.steiner.app.utils.Notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.steiner.app.Activitys.MainActivity;
import com.steiner.app.Activitys.OtherDetailsActivity;
import com.steiner.app.R;
import com.steiner.app.utils.Config;
import com.steiner.app.utils.MyApplication;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NotificationService extends FirebaseMessagingService {


    private PendingIntent pendingIntent;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Config.Log("notification");
        updateMyActivity(getApplicationContext(), "");
        try {
            JSONObject json = new JSONObject(remoteMessage.getData().toString());
            String data = json.getString("data");
            JSONObject message = json.getJSONObject("message");
            String title = message.getString("title");
            String body = message.getString("body");
            String image = message.getString("image");

            Config.Log(title);
            Config.Log(body);
            Config.Log(image);

            new generatePictureStyleNotification(this, title, body, image);
            updateMyActivity(getApplicationContext(), "");
        } catch (Exception e) {
            Config.Log("Exception: " + e.getMessage());
        }
    }

    private NotificationChannel mChannel;
    private NotificationManager notifyManager;

    static void updateMyActivity(Context context, String message) {
        Intent intent = new Intent("unique_name");
        intent.putExtra("message", message);
        context.sendBroadcast(intent);
    }


    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.notification_logo : R.drawable.notification_logo;
    }

    public class generatePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {

        private Context mContext;
        private String title, message, imageUrl;

        public generatePictureStyleNotification(Context context, String title, String message, String imageUrl) {
            super();
            this.mContext = context;
            this.title = title;
            this.message = message;
            this.imageUrl = imageUrl;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (notifyManager == null) {
                notifyManager = (NotificationManager) getSystemService
                        (Context.NOTIFICATION_SERVICE);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationCompat.Builder builder;
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent;
                int importance = NotificationManager.IMPORTANCE_HIGH;
                if (mChannel == null) {
                    mChannel = new NotificationChannel("0", title, importance);
                    mChannel.setDescription(message);
                    mChannel.enableVibration(true);
                    notifyManager.createNotificationChannel(mChannel);
                }
                builder = new NotificationCompat.Builder(mContext, "0");
                Intent i = new Intent(MyApplication.context.getApplicationContext(), OtherDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(mContext, 1251, i, PendingIntent.FLAG_ONE_SHOT);
                builder.setContentTitle(title)
                        .setContentText(message)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setLargeIcon(result)
                        .setSmallIcon(R.drawable.notification_logo) //needs white icon with transparent BG (For all platforms)
                        .setVibrate(new long[]{1000, 1000})
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setContentIntent(pendingIntent)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setAutoCancel(true);

                Notification notification = builder.build();
                notifyManager.notify(0, notification);
            } else {

                Intent intent = new Intent(mContext, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("key", "value");
                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1251, intent, PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setLargeIcon(result)
                        .setSmallIcon(getNotificationIcon()) //needs white icon with transparent BG (For all platforms)
                        .setVibrate(new long[]{1000, 1000})
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setAutoCancel(true);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1251, notificationBuilder.build());
            }
        }
    }

}
