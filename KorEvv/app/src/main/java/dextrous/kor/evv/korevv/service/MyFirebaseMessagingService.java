package dextrous.kor.evv.korevv.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import dextrous.kor.evv.korevv.activity.NotificationActivity;
import dextrous.kor.evv.korevv.preferences.LoggedInUser;
import dextrous.kor.evv.korevv.util.Config;
import dextrous.kor.evv.korevv.util.NotificationUtils;

/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    LoggedInUser loggedInUser;

    private NotificationUtils notificationUtils;
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.d("NEW_TOKEN",s);
       // storeRegIdInPref(s);

        // sending reg id to your server
        sendRegistrationToServer(s);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", s);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF,0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            if(remoteMessage.getNotification().getBody().contains("dev.internal")){

                loggedInUser=new LoggedInUser(getApplicationContext());
                loggedInUser.setBASE_URL(remoteMessage.getNotification().getBody());
                Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            }else {
                handleNotification(remoteMessage.getNotification().getBody());
            }
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            if(message.contains("dev.internal")){

                loggedInUser=new LoggedInUser(getApplicationContext());
                loggedInUser.setBASE_URL(message);
                Log.e(TAG, "Notification Body: " + message);
            }
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
            Intent resultIntent = new Intent(getApplicationContext(), NotificationActivity.class);
            if(message.contains("dev.internal")){

                loggedInUser=new LoggedInUser(getApplicationContext());
                loggedInUser.setBASE_URL(message);
                Log.e(TAG, "Notification Body: " + message);
            }
            resultIntent.putExtra("message", message);

            // check for image attachment

            showNotificationMessage(getApplicationContext(), message, message, "", resultIntent);
        }else{
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            if(message.contains("dev.internal")){

                loggedInUser=new LoggedInUser(getApplicationContext());
                loggedInUser.setBASE_URL(message);
                Log.e(TAG, "Notification Body: " + message);
            }
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), NotificationActivity.class);
            if(message.contains("dev.internal")){

                loggedInUser=new LoggedInUser(getApplicationContext());
                loggedInUser.setBASE_URL(message);
                Log.e(TAG, "Notification Body: " + message);
            }
            resultIntent.putExtra("message", message);

            // check for image attachment

                showNotificationMessage(getApplicationContext(), message, message, "", resultIntent);

            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                if(message.contains("dev.internal")){

                    loggedInUser=new LoggedInUser(getApplicationContext());
                    loggedInUser.setBASE_URL(message);
                    Log.e(TAG, "Notification Body: " + message);
                }
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
                Intent resultIntent = new Intent(getApplicationContext(), NotificationActivity.class);
                if(message.contains("dev.internal")){

                    loggedInUser=new LoggedInUser(getApplicationContext());
                    loggedInUser.setBASE_URL(message);
                    Log.e(TAG, "Notification Body: " + message);
                }
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    if(message.contains("dev.internal")){

                        loggedInUser=new LoggedInUser(getApplicationContext());
                        loggedInUser.setBASE_URL(message);
                        Log.e(TAG, "Notification Body: " + message);
                    }
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            } else {
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                if(message.contains("dev.internal")){

                    loggedInUser=new LoggedInUser(getApplicationContext());
                    loggedInUser.setBASE_URL(message);
                    Log.e(TAG, "Notification Body: " + message);
                }
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), NotificationActivity.class);
                if(message.contains("dev.internal")){

                    loggedInUser=new LoggedInUser(getApplicationContext());
                    loggedInUser.setBASE_URL(message);
                    Log.e(TAG, "Notification Body: " + message);
                }
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    if(message.contains("dev.internal")){

                        loggedInUser=new LoggedInUser(getApplicationContext());
                        loggedInUser.setBASE_URL(message);
                        Log.e(TAG, "Notification Body: " + message);
                    }
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}
