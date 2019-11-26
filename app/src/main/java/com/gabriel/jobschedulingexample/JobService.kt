package com.gabriel.jobschedulingexample

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus


class MyJobService : JobService() {

    val TAG = "MyJobService"
    var isWorking = false
    var jobCancelled = false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Job cancelled before being completed.")
        jobCancelled = true
        val needsReschedule = isWorking
        jobFinished(params, needsReschedule)
        return needsReschedule
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Job started!")
        isWorking = true
        params?.let {
            startWorkOnNewThread(it)
        }
        return isWorking
    }

    private fun startWorkOnNewThread(jobParameters: JobParameters) {
        Log.d(TAG, "Job started!")
        val message = jobParameters.extras.getString("mode")
        message?.let {
            val observable = Observable.just(message)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe { item ->
                    emit(item)
                }
        }
        isWorking = false
        val needsReschedule = false
        jobFinished(jobParameters, needsReschedule)
    }

    private fun emit(message: String?) {
        message?.let {
            EventBus.getDefault().post(MainActivity.Companion.MessageEvent(it))
        }
    }

}