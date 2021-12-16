package rachman.forniandi.workmanagerapp.data.local.dao

import androidx.room.Dao
import io.reactivex.rxjava3.core.Single
import rachman.forniandi.workmanagerapp.data.local.entity.UserEntity

@Dao
interface UserDao {
    fun insertDao(userEntity: UserEntity):Single<Long>
}