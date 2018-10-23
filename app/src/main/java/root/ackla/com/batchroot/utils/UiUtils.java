package root.ackla.com.batchroot.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by ankurrawal on 19/6/18.
 */

public class UiUtils {

    public static void hideAppIcon(Context context) {
        PackageManager p = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, root.ackla.com.batchroot.SplashActivity.class); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

    }


    public static String getLocalTime() {
        //Date will return local time in Java
        Date localTime = new Date();

        //creating DateFormat for converting time from local timezone to GMT
        DateFormat converter = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");

        //getting GMT timezone, you can get any timezone e.g. UTC
        converter.setTimeZone(TimeZone.getTimeZone("GMT"));

        System.out.println("local time : " + localTime);

        return "" + localTime;
    }

    public static String getGMTTime() {
        //Date will return local time in Java
        Date localTime = new Date();

        //creating DateFormat for converting time from local timezone to GMT
        DateFormat converter = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");

        //getting GMT timezone, you can get any timezone e.g. UTC
        converter.setTimeZone(TimeZone.getTimeZone("GMT"));

        System.out.println("time in GMT : " + converter.format(localTime));

        return "" + converter.format(localTime);
    }

    public static void exportDB() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + "root.ackla.com.batchroot" + "/databases/" + "batchroot.db";
        String backupDBPath = "batchroot.db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
//            Toast.makeText(context, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
