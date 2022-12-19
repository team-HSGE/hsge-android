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
import android.view.WindowManager
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

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        @SuppressLint("InvalidWakeLockTag")
        val wakeLock = powerManager.newWakeLock(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, "screen_on")
        wakeLock.acquire(5000L /*10 minutes*/)

        // Data message를 수신함
        if (message.data.isNotEmpty()) {
            sendNotification(message)
        } else {
            Log.d("fcm push", "data가 비어있습니다. 메시지를 수신하지 못했습니다.")
        }
        wakeLock.release()
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {

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

        val notification = getNotificationBuilder(remoteMessage.data["title"]!!, remoteMessage.data["body"]!!, pendingIntent).build()
        notificationManager.notify(1, notification)

    }

    private fun getNotificationBuilder(title: String, content: String, pendingIntent: PendingIntent) : NotificationCompat.Builder{

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test_push_img)

        return NotificationCompat.Builder(this, resources.getString(R.string.default_notification_channel_id))
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(pendingIntent)
            .setGroupSummary(true)
            .setAutoCancel(true)
            .setLargeIcon(bitmap)
            .setSmallIcon(R.drawable.ic_logo)
            .setShowWhen(true)
    }

//    private fun getCircleBitmap(bitmap: Bitmap) : Bitmap {
//        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(output)
//
//        val color = Color.RED
//        val paint = Paint()
//        val setLength: Int
//        if(bitmap.width >= bitmap.height){
//            setLength = bitmap.height
//        }else {
//            setLength = bitmap.width
//        }
//        val rect = Rect(0, 0, setLength, setLength)
//        val rectF = RectF(rect)
//
//        paint.isAntiAlias = true
//        canvas.drawARGB(0, 0, 0, 0)
//        paint.setColor(color)
//        canvas.drawOval(rectF, paint)
//
//        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
//        canvas.drawBitmap(bitmap, rect, rect, paint)
//
//        bitmap.recycle()
//
//        return output
//    }
}