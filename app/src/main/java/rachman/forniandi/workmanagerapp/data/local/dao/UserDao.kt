package rachman.forniandi.workmanagerapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import io.reactivex.Single
import rachman.forniandi.workmanagerapp.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Insert
    fun insertDao(userEntity: UserEntity):Single<Long>

    @Update
    fun updateDao(userEntity: UserEntity):Single<Int>
}