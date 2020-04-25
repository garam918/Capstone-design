package com.example.garam.myapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseTest : FirebaseMessagingService() {

    override fun onNewToken(s: String?) {
        super.onNewToken(s)
        Log.e("FCM_TEST",s)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage!!.data["title"]
        val message = remoteMessage!!.data["message"]
        val test = remoteMessage!!.data["test"]

        val intent = Intent(this,MainActivity::class.java)
        intent.putExtra("test",test)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
             val channel = "채널"
             val channel_nm = "채널명"
             val notichannel = getSystemService(NotificationManager::class.java)
             val channelMessage = NotificationChannel(channel,channel_nm,NotificationManager.IMPORTANCE_HIGH)

            channelMessage.description = "채널에 대한 설명"
            channelMessage.enableLights(true)
            channelMessage.enableVibration(true)
            channelMessage.setShowBadge(false)
            channelMessage.vibrationPattern = longArrayOf(1000, 1000)
            notichannel.createNotificationChannel(channelMessage)
            val notificationBuilder = NotificationCompat.Builder(this,channel).setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title).setContentText(message).setStyle(NotificationCompat.BigTextStyle().bigText(message)).setChannelId(channel).setAutoCancel(true)
                .setContentIntent(pendingIntent).setDefaults(Notification.DEFAULT_VIBRATE)

             val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
             notificationManager.notify(9999,notificationBuilder.build())
        }
        else {
            val notificationBuilder = NotificationCompat.Builder(this,"")
                .setSmallIcon(R.drawable.ic_noun_homeless_outreach)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(9999,notificationBuilder.build())
        }
    }
}