package com.starters.hsge.presentation.main.home.push

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.starters.hsge.R
import com.starters.hsge.presentation.main.MainActivity
import java.util.*

class FirebaseService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FirebaseService", "refreshed FCM token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // 화면 깨우기
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager

        @SuppressLint("InvalidWakeLockTag")
        val wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_DIM_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "screen_on"
        )
        wakeLock.acquire(5000L /*10 minutes*/)

        // notification 수신한 경우
        if (message.getNotification() != null) {
            Log.d("fcm_service_notification", message.notification?.title.toString())
        }

        // Data message를 수신함
        if (message.data.isNotEmpty()) {
            val about = message.data["about"].toString() // 서버로 받아온 푸시 구분값
            val img = message.data["image"]?.toInt()
            sendNotification(message, "chat", img)
            Log.d("fcm_service_data", message.data["about"].toString())

        } else {
            Log.d("fcm push", "data가 비어있습니다. 메시지를 수신하지 못했습니다.")
        }
        wakeLock.release()
    }

    private fun sendNotification(remoteMessage: RemoteMessage, about: String?, img: Int?) {

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        when (about) {
            "match" -> { // 좋아요 -> 채팅 탭으로 이동
                intent.putExtra("pushAbout", "chatFragment")
            }
            "chat" -> { // 채팅 -> 대화방으로 이동 (현재 마이페이지. 수정 필요)
                intent.putExtra("pushAbout", "chatRoomFragment")
            }
            else -> return
        }

        intent.putExtra("createdAt", "chatFragment")

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            UUID.randomUUID().hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT // 일회용 펜딩 인텐트
        )

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                resources.getString(R.string.default_notification_channel_id), //채널 ID
                "CHATTING", //채널명
                NotificationManager.IMPORTANCE_HIGH// 알림음이 울리며 헤드업 알림 표시
            )
            channel.apply {
                enableLights(true)
                lightColor = Color.BLUE
                enableVibration(true)
                description = "notification"
                notificationManager.createNotificationChannel(channel)
            }
        }

        val notification = getNotificationBuilder(remoteMessage.data["title"]!!, remoteMessage.data["body"]!!, img, pendingIntent
        ).build()
        notificationManager.notify((System.currentTimeMillis()).toInt(), notification)
    }

    private fun getNotificationBuilder(title: String, content: String, img: Int?, pendingIntent: PendingIntent): NotificationCompat.Builder {
        val bitmap = when (img) {
            1 -> {
                BitmapFactory.decodeResource(resources, R.drawable.dog_profile_1)
            }
            2 -> {
                BitmapFactory.decodeResource(resources, R.drawable.dog_profile_2)
            }
            3 -> {
                BitmapFactory.decodeResource(resources, R.drawable.dog_profile_3)
            }
            4 -> {
                BitmapFactory.decodeResource(resources, R.drawable.dog_profile_4)
            }
            5 -> {
                BitmapFactory.decodeResource(resources, R.drawable.dog_profile_5)
            }
            6 -> {
                BitmapFactory.decodeResource(resources, R.drawable.dog_profile_6)
            }
            7 -> {
                BitmapFactory.decodeResource(resources, R.drawable.dog_profile_7)
            }
            8 -> {
                BitmapFactory.decodeResource(resources, R.drawable.dog_profile_8)
            }
            9 -> {
                BitmapFactory.decodeResource(resources, R.drawable.dog_profile_9)
            }
            10 -> {
                BitmapFactory.decodeResource(resources, R.drawable.dog_profile_10)
            }
            11 -> {
                BitmapFactory.decodeResource(resources, R.drawable.dog_profile_11)
            }
            12 -> {
                BitmapFactory.decodeResource(resources, R.drawable.dog_profile_12)
            }
            13 -> {
                BitmapFactory.decodeResource(resources, R.drawable.dog_profile_13)
            }
            14 -> {
                BitmapFactory.decodeResource(resources, R.drawable.dog_profile_14)
            }
            else -> {
                BitmapFactory.decodeResource(resources, R.drawable.dog_profile_15)
            }
        }

        return NotificationCompat
            .Builder(this, resources.getString(R.string.default_notification_channel_id))
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(pendingIntent)
            .setGroupSummary(true)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_paw)
            .setShowWhen(true)
            .setLargeIcon(bitmap)

    }
}
