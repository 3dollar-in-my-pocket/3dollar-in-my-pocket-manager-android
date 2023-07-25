package app.threedollars.manager.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import app.threedollars.manager.LoginActivity
import app.threedollars.manager.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class ThreedollarsManagerMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.apply {
            sendNotification(title.toString(), this.body.toString())
        }
    }

    private fun sendNotification(title: String, messageBody: String) {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            },
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT else PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // 알림 채널 설정
        val channelId = "threedollarsManager"
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_app_icon)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android Oreo 버전 이상을 위한 알림 채널 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "가슴속 3천원 사장님",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

}