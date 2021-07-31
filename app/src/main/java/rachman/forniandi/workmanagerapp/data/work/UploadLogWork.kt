package rachman.forniandi.workmanagerapp.data.work

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class UploadLogWork(context:Context, workerParams:WorkerParameters): Worker(context,workerParams) {
    override fun doWork(): Result {
        for (i in 50..60){
            Log.d(TAG,"UploadLogWork: $i")

            Thread.sleep(1000)
        }
        /*
        pass data to result
        workDataOf is a ktx extension method
        */
        val data = workDataOf("KEY_RESULT" to 10)

        //return Result.success()
        return Result.success(data)
    }

    companion object{
        const val TAG="UploadLogWork"
    }
}