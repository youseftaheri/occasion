package com.iranwebyar.occasions.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.provider.CalendarContract
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.iranwebyar.occasions.R
import com.iranwebyar.occasions.data.model.db.Occasion
import com.iranwebyar.occasions.ui.occasionList.OccasionListActivity
import android.content.ContentResolver
import android.net.Uri
import android.media.AudioAttributes
import java.util.*












class AlarmReceiver:BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {
        sendNotification(context, intent?.extras?.getString(INTENT_EXTRA_TITLE)!!,
            intent?.extras?.getInt(INTENT_EXTRA_ROW_ID)?:0)

//        //notification Extras..
//        val notificationId = intent?.extras?.getInt(INTENT_EXTRA_ROW_ID)?:0
//        val reminderIdentifier = intent?.extras?.getString(INTENT_EXTRA_REMINDER_IDENTIFIER)
//        val title = intent?.extras?.getString(INTENT_EXTRA_TITLE)
//
//        val notificationIntent = Intent(context, this::class.java)//Intent(context!!.applicationContext,OccasionListActivity::class.java)
//        notificationIntent.putExtra(INTENT_EXTRA_REMINDER_IDENTIFIER,reminderIdentifier)
//        val pendingIntent = PendingIntent.getActivity(context,notificationId,notificationIntent,PendingIntent.FLAG_ONE_SHOT)//FLAG_UPDATE_CURRENT)
//        val channelId = "Occasion"+"1-2-3-4-5"+ CommonUtils.randomNumber
//
//        val notificationBuilder = NotificationCompat.Builder(context!!,channelId)//context.getString(R.string.reminder_notification_channel_id))
//            .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.mipmap.ic_launcher))
//            .setSmallIcon(R.drawable.ic_notifications_smallicon)
//            .setContentTitle(title)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//            .setOnlyAlertOnce(true)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//
//        val notificationManager = ContextCompat.getSystemService(context.applicationContext,NotificationManager::class.java) as NotificationManager
//        notificationManager.notify(notificationId,notificationBuilder.build())

    }

    private fun sendNotification1(context: Context?, titleBody: String, notificationId: Int){ //, messageBody: String) {
        var sound: Uri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context!!.packageName + "/" + R.raw.noti_ring
        )
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

        val intent = Intent(context, this::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context, notificationId /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = "Occasion"+"1-2-3-4-5"+ CommonUtils.randomNumber
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notifications_smallicon)
            .setContentTitle(titleBody)
            .setColor(context.getColor(R.color.colorPrimary))
//            .setContentText(messageBody)
//            .setStyle(NotificationCompat.BigTextStyle().bigText(titleBody))
            .setAutoCancel(true)
            .setSound(sound)
            .setContentIntent(pendingIntent)
            .setLights(Color.GREEN, 3000, 3000)


        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())

        //For API 26+ you need to put some additional code like below:
        val mChannel: NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH
            )
            mChannel.lightColor = Color.GRAY
            mChannel.enableLights(true)
//            mChannel.description = Utils.CHANNEL_SIREN_DESCRIPTION
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            mChannel.setSound(sound, audioAttributes)

//            // Since android Oreo notification channel is needed.
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val channel = NotificationChannel(
//                    channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT
//                )
//                notificationManager.createNotificationChannel(channel)
//            }
            notificationManager.createNotificationChannel(mChannel)


//            ApplicationProvider.getApplicationContext<Context>()
//                .getSystemService(Context.NOTIFICATION_SERVICE)
//                ?.createNotificationChannel(mChannel)
        }

    }

    private fun sendNotification(context: Context?, titleBody: String, notificationId: Int){
        var sound: Uri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context!!.packageName + "/" + R.raw.noti_ring
        )
        val channelId = "Occasion"+"1-2-3-4-5"+ CommonUtils.randomNumber
        val channelName = "Occasion"+"1-2-3-4-5"
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

//        val mNotificationManager =
//            context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
        val mChannel: NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            mChannel.lightColor = Color.GRAY
            mChannel.enableLights(true)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            mChannel.setSound(sound, audioAttributes)
            mNotificationManager?.createNotificationChannel(mChannel)
        }

        val mBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notifications_smallicon)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.getResources(),
                        R.mipmap.ic_launcher
                    )
                )
                .setContentTitle(titleBody)
                .setAutoCancel(true)
                .setLights(-0xffff01, 300, 1000) // blue color
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            mBuilder.setSound(sound)
        }

        val NOTIFICATION_ID = 1 // Causes to update the same notification over and over again.

        mNotificationManager?.notify(NOTIFICATION_ID, mBuilder.build())
    }

    companion object {

        private const val TAG = "AlarmReceiver"

        fun getAlarmPendingIntent(
            pckgContext: Context,
            reminder:Occasion
        ): PendingIntent {
            val intent = Intent(pckgContext, AlarmReceiver::class.java)
            intent.apply {
                putExtra(INTENT_EXTRA_REMINDER_IDENTIFIER, UUID.randomUUID().toString())
                putExtra(INTENT_EXTRA_ROW_ID, reminder.id.toInt())
                putExtra(INTENT_EXTRA_TITLE, reminder.title)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                pckgContext,
                reminder.id.toInt(),
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            Log.d(TAG, "getAlarmPendingIntent: ${reminder.id}")
            return pendingIntent
        }


    }

}