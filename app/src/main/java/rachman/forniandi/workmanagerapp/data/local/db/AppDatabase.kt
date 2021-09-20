package rachman.forniandi.workmanagerapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import rachman.forniandi.workmanagerapp.data.local.dao.EmployeeDao
import rachman.forniandi.workmanagerapp.data.local.entity.EmployeeEntity


@Database(entities = [EmployeeEntity::class],version = 1,exportSchema = false)
abstract class AppDatabase:RoomDatabase() {

    abstract fun employeeDao():EmployeeDao

    companion object{

        @Volatile
        private var INSTANCE:AppDatabase?= null

        fun getInstance(context: Context):AppDatabase{

            synchronized(this){

                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "work_db"
                    )
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}