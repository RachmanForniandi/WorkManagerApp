package rachman.forniandi.workmanagerapp.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import rachman.forniandi.workmanagerapp.WorkManagerApp
import rachman.forniandi.workmanagerapp.data.local.entity.EmployeeEntity
import rachman.forniandi.workmanagerapp.data.repository.EmployeeRepository

class EmployeeCoroutineWork(appContext: Context,params:WorkerParameters):CoroutineWorker(appContext, params) {

    companion object{
        const val TAG = "EmployeeCoroutineWork"
    }

    private val db = (appContext.applicationContext as WorkManagerApp).db
    private val repository = EmployeeRepository(db)

    override suspend fun doWork(): Result {
        val result = repository.insert(EmployeeEntity(name = "Forniandi",job = "Android Developer",age = 30))

        val data = workDataOf("KEY_RESULT" to result)

        return Result.success(data)
    }
}