package root.ackla.com.batchroot.readnotification;

/**
 * Created by ankurrawal on 28/9/17.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import root.ackla.com.batchroot.BatchRootApplication;


public class NotificationService extends NotificationListenerService {

    Context context;

    @Override

    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();

    }


    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }


    @Override

    public void onNotificationPosted(StatusBarNotification sbn) {


        String pack = sbn.getPackageName();
        String ticker = sbn.getNotification().tickerText.toString();
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title");
        String text = extras.getCharSequence("android.text").toString();

        BatchRootApplication.Logger("ANKUR", "Package: " + pack);
        BatchRootApplication.Logger("ANKUR", "Ticker: " + ticker);
        BatchRootApplication.Logger("ANKUR", "Title: " + title);
        BatchRootApplication.Logger("ANKUR", "Text: " + text);

        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra("package", pack);
        msgrcv.putExtra("ticker", ticker);
        msgrcv.putExtra("title", title);
        msgrcv.putExtra("text", text);

        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);


    }

    @Override

    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg", "Notification Removed");

    }
}