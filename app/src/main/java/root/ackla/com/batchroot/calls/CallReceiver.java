package root.ackla.com.batchroot.calls;

/**
 * Created by ankurrawal on 14/9/17.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import root.ackla.com.batchroot.BatchRootApplication;
import root.ackla.com.batchroot.MainService;

public class CallReceiver extends BroadcastReceiver {

    private Context mcontext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mcontext = context;

        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
         String   savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
            Intent broadcastIntent = new Intent(mcontext, MainService.class);
            broadcastIntent.setAction(BatchRootApplication.BROADCAST_INTENT_CALL_OUTGOING);
            broadcastIntent.putExtra("savedNumber", savedNumber);
            mcontext.startService(broadcastIntent);

        } else {
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

            Intent broadcastIntent = new Intent(mcontext, MainService.class);
            broadcastIntent.setAction(BatchRootApplication.BROADCAST_INTENT_CALL);
            broadcastIntent.putExtra("statestr", stateStr);
            broadcastIntent.putExtra("number", number);
            mcontext.startService(broadcastIntent);

        }

    }


}