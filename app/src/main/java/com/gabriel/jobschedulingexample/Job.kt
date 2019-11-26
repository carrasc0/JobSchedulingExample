package com.gabriel.jobschedulingexample

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.os.Build
import android.os.PersistableBundle
import android.util.Log
import androidx.annotation.RequiresApi


class Job {

    companion object {
        @RequiresApi(Build.VERSION_CODES.M)
        fun sheduleJob(context: Context, mode: String) {
            val componentName = ComponentName(context, MyJobService::class.java)
            val bundle = PersistableBundle()
            bundle.putString("mode", mode)
            val jobInfo = JobInfo.Builder(12, componentName)
                .setExtras(bundle)
                .setMinimumLatency(1 * 1000)
                .build()
            val jobScheduler = context.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler?
            val resultCode = jobScheduler?.schedule(jobInfo)
            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Log.d("Job", "Job scheduled!")
            } else {
                Log.d("Job", "Job not scheduled")
            }
        }
    }
}


