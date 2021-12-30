package rachman.forniandi.workmanagerapp.data.work

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.*
import rachman.forniandi.workmanagerapp.R
import java.lang.Exception
import java.util.*

class ForegroundWork(private val context: Context, workerParameters: WorkerParameters):
    Worker(context,workerParameters) {
    override fun doWork(): Result {
        return try {
            setForegroundAsync(createNotification(applicationContext,id))

            //for loop
            for (i in 0..120){
                Log.d(RandomNumberWork.TAG, "ForegroundWork: $i")
                Thread.sleep(1000)
            }

            /*
            Pass data to result
            workDataOf is ktx extension methods
            * */
            val data = workDataOf("KEY_RESULT" to 120)
            Result.success(data)
        }catch (e:Exception){
            /*
            * if there is an error return FAILURE
            * */
            //Result.failure()

            /*
            * you may want to retry it later then you can return retry
            * */
            Result.retry()
        }
    }

    companion object{
        const val TAG = "ForegroundWork"

        private fun createNotification(context: Context,id:UUID):ForegroundInfo{

            val intent = WorkManager.getInstance(context).createCancelPendingIntent(id)

            val notification = NotificationCompat.Builder(context, "ch01")
                .setContentTitle("Long Running Task")
                .setContentText("We are downloading a file. Please Wait...")
                .setSmallIcon(R.drawable.ic_notification_download)
                .setOngoing(true)
                .addAction(R.drawable.ic_cancel,"Cancel",intent)
                .setProgress(100,0,false)
                .build()

            return ForegroundInfo(10001,notification)
        }
    }
}