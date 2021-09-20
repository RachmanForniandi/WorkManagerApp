package rachman.forniandi.workmanagerapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employee")
data class EmployeeEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0L,
    @ColumnInfo(name="name")
    val name:String,
    @ColumnInfo(name = "job")
    val job:String,
    @ColumnInfo(name = "age")
    val age:Int
)
