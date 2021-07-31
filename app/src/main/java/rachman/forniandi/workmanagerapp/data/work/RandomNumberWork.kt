package rachman.forniandi.workmanagerapp.data.work

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class RandomNumberWork(context: Context,workerParameters: WorkerParameters):
    Worker(context,workerParameters) {

    //doWork method runs asynchronously in background thread - this wont block our ui
    override fun doWork(): Result {

        //get the input data
        val start = inputData.getInt("KEY_START",0)
        val count = inputData.getInt("KEY_COUNT",0)
        /*for (i in 0..count){
            Log.d(TAG,"RandomNumberWork: $i")

            Thread.sleep(1000)
        }*/
        for (i in start..count){
            Log.d(TAG,"RandomNumberWork: $i")

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
        const val TAG="RandomNumberWork"
    }
}