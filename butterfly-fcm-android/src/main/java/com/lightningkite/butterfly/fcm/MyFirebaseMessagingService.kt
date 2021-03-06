package com.lightningkite.butterfly.fcm

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lightningkite.butterfly.post
import com.lightningkite.butterfly.views.EntryPoint
import com.lightningkite.butterfly.views.ViewGenerator

open class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        var main: ViewGenerator? = null
        const val FROM_NOTIFICATION: String = "fromNotification"
    }

    override fun onNewToken(token: String) {
        Notifications.notificationToken.value = token
    }

    override fun onMessageReceived(message: RemoteMessage) {
        post {
            if ((main as? ForegroundNotificationHandler)?.handleNotificationInForeground(message.data) != ForegroundNotificationHandlerResult.SUPPRESS_NOTIFICATION) {
                //show notification
                message.notification?.let { notification ->
                    val meta = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).metaData
                    val builder =
                        NotificationCompat.Builder(
                            this,
                            notification.channelId
                                ?: meta.getString(
                                    "com.google.firebase.messaging.default_notification_channel_id"
                                ) ?: "default"
                        )
                    meta.getInt("com.google.firebase.messaging.default_notification_icon", 0)
                        .takeUnless { it == 0 }
                        ?.let { builder.setSmallIcon(it) }
                    meta.getInt("com.google.firebase.messaging.default_notification_color", 0)
                        .takeUnless { it == 0 }
                        ?.let { builder.setColor(it) }
                    notification.title?.let { it -> builder.setContentTitle(it) }
                    notification.body?.let { it -> builder.setContentText(it) }
                    builder.setContentIntent(
                        PendingIntent.getActivity(
                            this,
                            0,
                            packageManager.getLaunchIntentForPackage(packageName)!!.apply {
                                this.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                for ((key, value) in message.data) {
                                    this.putExtra(key, value)
                                }
                                this.putExtra(FROM_NOTIFICATION, true)
                            },
                            PendingIntent.FLAG_ONE_SHOT
                        )
                    )
                    notification.sound?.let { Uri.parse(it) }?.let { builder.setSound(it) }
                    notification.vibrateTimings?.let { builder.setVibrate(it) }
                    notification.notificationPriority?.let { builder.setPriority(it) }
                    builder.setAutoCancel(true)
                    NotificationManagerCompat.from(this).notify(notification.tag?.hashCode() ?: message.hashCode(), builder.build())
                }
            }
        }
    }
}

