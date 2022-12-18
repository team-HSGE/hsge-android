package com.starters.hsge.presentation.main.home.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater.from
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.RemoteMessage.Notification
import com.starters.hsge.R
import com.starters.hsge.presentation.main.MainActivity
import com.starters.hsge.presentation.main.chat.ChatFragment
import com.starters.hsge.presentation.main.chat.ChatFragmentArgs
import okhttp3.internal.notify
import java.util.*

class FirebaseService : FirebaseMessagingService() {

    private val TAG = "FirebaseService"


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FirebaseService", "refreshed FCM token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.notification?.let {
            Log.d("FirebaseMessagingService*************", "Message Notification Body: ${it.body}")
            //알림 메세지 _ 포그라운드에서도 알림 받은 것 처럼 받은 정보를 가지고 notification 구현하기.
            sendNotification(message.notification!!)
        }

        //데이터 메세지의 경우.
        if (message.data.isNotEmpty()) {
            //sendDataMessage(message.data)
        }

    }


//    private fun createNotificationChannel(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                CHANNEL_NAME,
//                NotificationManager.IMPORTANCE_HIGH
//            )
//
//            channel.enableLights(true)
//            channel.enableVibration(true)
//
//            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
//        }
//    }

    companion object {
        private const val CHANNEL_NAME = "FCM STUDY"
        private const val CHANNEL_ID = "FCM__channel_id"

    }

    private fun sendNotification(data: Notification) {


        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("createdAt", "chatFragment")

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            UUID.randomUUID().hashCode(),
            intent,
            PendingIntent.FLAG_ONE_SHOT // 일회용 펜딩 인텐트
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

        val notification = getNotificationBuilder(data.title!!, data.body!!, pendingIntent).build()
        notificationManager.notify(1, notification)


//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
//        )

        ////
//        val manager = NotificationManagerCompat.from(this)
//        val channel = NotificationChannelCompat
//            .Builder(CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_HIGH)
//            .setName("File Download")
//            .build()
//
//        manager.createNotificationChannel(channel)
        ////

//        val intent = Intent(this, MainActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION

        /**/
//        val pendingIntent = NavDeepLinkBuilder(this)
//            .setGraph(R.navigation.nav_graph)
//            .setComponentName(MainActivity::class.java)
//            .setDestination(R.id.chatFragment)
//            .setArguments(ChatFragmentArgs("1").toBundle())
//            .createPendingIntent()
//
//
//        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle(data.title)
//            .setContentText(data.body)
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setContentIntent(pendingIntent)
//            .setFullScreenIntent(pendingIntent, true)
//            .build()
//
//        manager.notify(1, notificationBuilder)

        //?**/

        //val manager = NotificationManagerCompat.from(applicationContext)
        // manager.notify(1, notificationBuilder)


//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        createNotificationChannel()
//
//        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
//        notificationBuilder.setContentTitle(data.title)
//            .setSmallIcon(R.drawable.ic_user_icon_test)
//            .setContentText(data.body)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//
//        notificationManager.notify(100, notificationBuilder.build())
    }

    private fun getNotificationBuilder(title: String, content: String, pendingIntent: PendingIntent) : NotificationCompat.Builder{
        return NotificationCompat.Builder(this, resources.getString(R.string.default_notification_channel_id))
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(pendingIntent)
            .setGroupSummary(true)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_chat)
            .setShowWhen(true)
    }
}