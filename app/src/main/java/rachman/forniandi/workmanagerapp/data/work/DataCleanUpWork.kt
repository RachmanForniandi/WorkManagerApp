package rachman.forniandi.workmanagerapp.data.work

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class DataCleanUpWork(context:Context, workerParams:WorkerParameters): Worker(context,workerParams) {
    override fun doWork(): Result {
        //get the input data
        val start = inputData.getInt("KEY_RESULT",0)

        //for loop
        for (i in start..20){
            Log.d(TAG,"DataCleanUp: $i")

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
        const val TAG="DataCleanUp"
    }
}