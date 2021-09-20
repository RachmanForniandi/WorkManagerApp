package rachman.forniandi.workmanagerapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import rachman.forniandi.workmanagerapp.data.local.entity.EmployeeEntity

@Dao
interface EmployeeDao {

    @Insert
    suspend fun insertData(employeeEntity: EmployeeEntity):Long
}