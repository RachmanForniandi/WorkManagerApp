package rachman.forniandi.workmanagerapp.data.work

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import rachman.forniandi.workmanagerapp.R
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

class DailyWork(private val context: Context,workerParameters: WorkerParameters):Worker(context,workerParameters) {

    private lateinit var notificationManager: NotificationManagerCompat

    override fun doWork(): Result {
        //constraints
        /*val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

*/
        val currentDate = Calendar.getInstance()

        val dueDate = Calendar.getInstance()

        //set time to 8 am
        dueDate.set(Calendar.HOUR_OF_DAY,12)
        dueDate.set(Calendar.MINUTE,30)
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

        createNotification()
        return Result.success()
    }

    private fun createNotification(){
        notificationManager = NotificationManagerCompat.from(context)

        val notification = NotificationCompat.Builder(context,"ch01")
            .setSmallIcon(R.drawable.ic_notification_work)
            .setContentTitle("Title Work")
            .setContentText("This is a example of daily work notification")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .build()

        notificationManager.notify(1001,notification)
    }

    companion object{
        const val TAG = "DailyWork"
    }
}