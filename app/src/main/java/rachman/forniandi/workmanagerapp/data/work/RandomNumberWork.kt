package rachman.forniandi.workmanagerapp.data.work

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class RandomNumberWork(context: Context,workerParameters: WorkerParameters):
    Worker(context,workerParameters) {

    override fun doWork(): Result {
        for (i in 0..10){
            Log.d(TAG,"RandomNumberWork: $i")

            Thread.sleep(1000)
        }
        return Result.success()
    }

    companion object{
        const val TAG="RandomNumberWork"
    }
}