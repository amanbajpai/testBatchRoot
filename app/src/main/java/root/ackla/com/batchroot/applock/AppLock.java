package root.ackla.com.batchroot.applock;

import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

/**
 * Created by ankurrawal on 16/10/17.
 */

public class AppLock extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getInstalledApps();
        checkCurrentOpenApp();


    }

    private void getInstalledApps() {
        final PackageManager pm = getPackageManager();
//get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            Log.d("ANKUR", "Installed package :" + packageInfo.packageName);
            Log.d("ANKUR", "Source dir : " + packageInfo.sourceDir);
            Log.d("ANKUR", "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
        }
    }


    private void checkCurrentOpenApp() {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRecentTasks(1, ActivityManager.RECENT_WITH_EXCLUDED);
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(
                        info.processName, PackageManager.GET_META_DATA));
                Log.w("LABEL", c.toString());
            } catch (Exception e) {
// Name Not FOund Exception
            }
        }
    }
}
