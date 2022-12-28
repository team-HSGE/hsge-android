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
        val wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "screen_on")
        wakeLock.acquire(5000L /*10 minutes*/)

        // notification 수신한 경우
        if (message.getNotification() != null){
            Log.d("fcm_service_notification", message.notification?.title.toString())
        }

        // Data message를 수신함
        if (message.data.isNotEmpty()) {
            val about = message.data["about"].toString() // 서버로 받아온 푸시 구분값
            sendNotification(message, "chat")
            Log.d("fcm_service_data", message.data["title"].toString())
            Log.d("fcm_service_data", message.data["body"].toString())
            Log.d("fcm_service_data", message.data["about"].toString())


        } else {
            Log.d("fcm push", "data가 비어있습니다. 메시지를 수신하지 못했습니다.")
        }
        wakeLock.release()
    }

    private fun sendNotification(remoteMessage: RemoteMessage, about: String?) {

        Log.d("fcm_service_data_hey", "${remoteMessage.data["title"]}, ${remoteMessage.data["body"]}")
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        when(about){
            "like" -> { // 좋아요 -> 채팅 탭으로 이동
                intent.putExtra("pushAbout", "chatFragment")
                Log.d("hey?", intent.extras!!.getString("pushAbout").toString())
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

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                resources.getString(R.string.default_notification_channel_id), //채널 ID
                "CHATTING", //채널명
                NotificationManager.IMPORTANCE_HIGH// 알림음이 울리며 헤드업 알림 표시
            )
            channel.apply {
                enableLights(true)
                lightColor= Color.BLUE
                enableVibration(true)
                description = "notification"
                notificationManager.createNotificationChannel(channel)
            }
        }

        val notification = getNotificationBuilder(remoteMessage.data["title"]!!, remoteMessage.data["body"]!!, pendingIntent).build()
        notificationManager.notify((System.currentTimeMillis()).toInt(), notification)
    }

    private fun getNotificationBuilder(title: String, content: String, pendingIntent: PendingIntent) : NotificationCompat.Builder{

        //val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test_push_img)

        return NotificationCompat.Builder(this, resources.getString(R.string.default_notification_channel_id))
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(pendingIntent)
            .setGroupSummary(true)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_paw)
            .setShowWhen(true)
        //            .setLargeIcon(bitmap)
    }
}