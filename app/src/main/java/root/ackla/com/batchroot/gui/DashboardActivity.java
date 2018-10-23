package root.ackla.com.batchroot.gui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import root.ackla.com.batchroot.R;
import root.ackla.com.batchroot.utils.UiUtils;

/**
 * Created by ankurrawal on 30/1/18.
 */

public class DashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UiUtils.exportDB();
    }
}
