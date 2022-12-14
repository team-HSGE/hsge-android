package com.starters.hsge.presentation.main.home.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.RemoteMessage.Notification
import com.starters.hsge.R
import com.starters.hsge.presentation.main.MainActivity
import com.starters.hsge.presentation.main.chat.ChatFragment

class FirebaseService : FirebaseMessagingService() {

    private val TAG = "FirebaseService"



    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FirebaseService", "refreshed FCM token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

//        Log.d("FirebaseService", message.notification?.title.toString())
//        Log.d("FirebaseService", message.notification?.body.toString())
//
//        Log.d("FirebaseMessagingService*************", "Message noti : ${message.notification}")


        //받은 remoteMessage의 값 출력해보기. 데이터메세지 / 알림메세지
        Log.d("FirebaseMessagingService*************", "Message data : ${message.data}")
        Log.d("FirebaseMessagingService*************", "Message noti : ${message.notification}")

        //알림 메세지의 경우.
        message.notification?.let {
            Log.d("FirebaseMessagingService*************", "Message Notification Body: ${it.body}")
            //알림 메세지 _ 포그라운드에서도 알림 받은 것 처럼 받은 정보를 가지고 notification 구현하기.
            sendNotification(message.notification!!)
        }

        //데이터 메세지의 경우.
        if(message.data.isNotEmpty()) {
            //sendDataMessage(message.data)
        }

    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.enableLights(true)
            channel.enableVibration(true)

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_NAME = "FCM STUDY"
        private const val CHANNEL_ID = "FCM__channel_id"

    }

    private fun sendNotification(data: Notification){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        notificationBuilder.setContentTitle(data.title)
            .setSmallIcon(R.drawable.ic_user_icon_test)
            .setContentText(data.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(100, notificationBuilder.build())
    }
}