package rachman.forniandi.workmanagerapp.data.work

import android.content.Context
import android.util.Log
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import io.reactivex.Single
import io.reactivex.rxjava3.annotations.NonNull
import rachman.forniandi.workmanagerapp.WorkManagerApp
import rachman.forniandi.workmanagerapp.data.local.entity.UserEntity
import rachman.forniandi.workmanagerapp.data.repository.UserRepository

class UserRxJavaWorker(appContext:Context,workerParams:WorkerParameters):RxWorker(appContext, workerParams) {

    private val db = (appContext.applicationContext as WorkManagerApp).db
    private val repository = UserRepository(db)

    override fun createWork(): Single<Result> {
        return repository.insertUser(UserEntity(name = "Forniandi",password = "123456"))
            .flatMap { id->
                repository.updateUser(UserEntity(id=
                id,name = "Forniandi's",password = "4735677"))
            }
            .map {
                result->
                val data = workDataOf("KEY_RESULT" to result)
                Log.e(TAG,result.toString())
                Result.success(data)
            }
    }


    companion object{
        const val TAG ="UserRxJavaWorker"
    }
}