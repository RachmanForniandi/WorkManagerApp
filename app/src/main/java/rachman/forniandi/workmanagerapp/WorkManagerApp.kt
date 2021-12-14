package rachman.forniandi.workmanagerapp

import android.app.Application
import rachman.forniandi.workmanagerapp.data.local.db.AppDatabase

class WorkManagerApp:Application() {

    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()

        db = AppDatabase.getInstance(this)
    }
}