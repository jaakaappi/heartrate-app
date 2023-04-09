package com.example.heartrate_monitor.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.example.heartrate_monitor.ui.MainActivity
import com.example.heartrate_monitor.db.HeartrateRepository
import com.example.heartrate_monitor.db.model.ExerciseModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HeartrateService : Service() {
    private val CHANNEL_ID = "HeartrateServiceChannel"

    @Inject
    lateinit var repository: HeartrateRepository

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        val pendingIntent = Intent(this, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
            )
        }

        val notification: Notification =
            Notification.Builder(this, CHANNEL_ID).setContentTitle("Heart rate service title")
                .setContentText("Heart rate service text").setSmallIcon(0)
                .setContentIntent(pendingIntent).setTicker("Heart rate service title")
                .setOngoing(true).build()

        // todo kysy notifikaatiolupa jossain
        startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)


        Log.d("HEARTRATEMONITOR", "Start")

        CoroutineScope(Dispatchers.IO).launch {

            for (i in 1..10) {
                delay(1000)
                repository.addExercise(ExerciseModel(0, 0, 0))
            }
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        Log.d("HEARTRATEMONITOR", "Stop")

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID, "heartratemonitorchannel", NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Heart rate monitor description"
        }

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(serviceChannel)
    }
}