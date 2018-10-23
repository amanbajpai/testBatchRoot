package root.ackla.com.batchroot.scheduler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by ankurrawal on 25/6/18.
 */

public class ParseUploadJobCreator implements JobCreator {

    @Override
    @Nullable
    public Job create(@NonNull String tag) {
        switch (tag) {
            case ParseSyncJob.TAG:
                return new ParseSyncJob();
            default:
                return null;
        }
    }
}
