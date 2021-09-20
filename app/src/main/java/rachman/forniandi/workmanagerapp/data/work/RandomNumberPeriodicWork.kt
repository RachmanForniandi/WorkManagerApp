package rachman.forniandi.workmanagerapp.data.work

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.lang.Exception

class RandomNumberPeriodicWork(context: Context, workerParameters: WorkerParameters):
    Worker(context,workerParameters) {

    //doWork method runs asynchronously in background thread - this wont block our ui
    override fun doWork(): Result {


        /*for (i in 0..count){
            Log.d(TAG,"RandomNumberWork: $i")

            Thread.sleep(1000)
        }*/
        return try {
            //get the input data
            val start = inputData.getInt("KEY_START",0)
            val count = inputData.getInt("KEY_COUNT",0)
            for (i in start..count){
                Log.d(TAG,"RandomNumberPeriodicWork: $i")

                if ( i == 5){
                    throw Exception("Error on the for loop")
                }
                Thread.sleep(1000)
            }
            /*
            pass data to result
            workDataOf is a ktx extension method
            */
            val data = workDataOf("KEY_RESULT" to 10)

            //return Result.success()
            return Result.success(data)
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
        const val TAG="PeriodicWork"
    }
}