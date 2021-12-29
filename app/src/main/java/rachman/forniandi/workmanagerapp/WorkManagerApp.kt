package rachman.forniandi.workmanagerapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import rachman.forniandi.workmanagerapp.data.local.db.AppDatabase

class WorkManagerApp:Application() {

    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()

        db = AppDatabase.getInstance(this)

        createNotificationChannel()
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(
                "ch01",
                "Channel One",
                NotificationManager.IMPORTANCE_HIGH).apply {
                description = "This is DailyWork notification channel"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}