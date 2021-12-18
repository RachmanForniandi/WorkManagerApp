package rachman.forniandi.workmanagerapp.data.repository

import io.reactivex.Single
import rachman.forniandi.workmanagerapp.data.local.db.AppDatabase
import rachman.forniandi.workmanagerapp.data.local.entity.UserEntity

class UserRepository(private val database: AppDatabase) {

    fun insertUser(userEntity: UserEntity):Single<Long> = database.userDao().insertDao(userEntity)

    fun updateUser(userEntity: UserEntity):Single<Int> = database.userDao().updateDao(userEntity)
}