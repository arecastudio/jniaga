package io.github.arecastudio.jniaga.srv;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

//import com.firebase.jobdispatcher.JobParameters;
//import com.firebase.jobdispatcher.JobService;

/**
 * Created by android on 12/13/17.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class FirebaseJob extends JobService{
    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
