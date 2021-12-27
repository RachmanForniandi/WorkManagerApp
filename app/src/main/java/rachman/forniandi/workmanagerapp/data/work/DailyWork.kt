package rachman.forniandi.workmanagerapp.data.work

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.*
import java.util.concurrent.TimeUnit

class DailyWork(context: Context,workerParameters: WorkerParameters):Worker(context,workerParameters) {


    override fun doWork(): Result {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        //set time to 8 am
        dueDate.set(Calendar.HOUR_OF_DAY,8)
        dueDate.set(Calendar.MINUTE,0)
        dueDate.set(Calendar.SECOND,0)

        if (dueDate.before(currentDate)){
            dueDate.add(Calendar.HOUR_OF_DAY,24)

        }

        //calculate time difference
        val timeDiff = (dueDate.timeInMillis - currentDate.timeInMillis)

        val dailyWorkRequest = OneTimeWorkRequestBuilder<DailyWork>()
            .setInitialDelay(timeDiff,TimeUnit.MILLISECONDS)
            .addTag(TAG)
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueue(dailyWorkRequest)

        return Result.success()
    }

    companion object{
        const val TAG = "DailyWork"
    }
}