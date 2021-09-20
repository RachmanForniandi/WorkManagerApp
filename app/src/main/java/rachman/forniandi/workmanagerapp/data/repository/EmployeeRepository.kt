package rachman.forniandi.workmanagerapp.data.repository

import rachman.forniandi.workmanagerapp.data.local.db.AppDatabase
import rachman.forniandi.workmanagerapp.data.local.entity.EmployeeEntity

class EmployeeRepository(private val database: AppDatabase) {

    suspend fun insert(entity: EmployeeEntity):Long =
        database.employeeDao().insertData(entity)
}