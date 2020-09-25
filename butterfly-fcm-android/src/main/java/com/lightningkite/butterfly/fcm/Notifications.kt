package com.lightningkite.butterfly.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.lightningkite.butterfly.net.HttpClient
import com.lightningkite.butterfly.observables.StandardObservableProperty

object Notifications {
    var notificationToken = StandardObservableProperty<String?>(null)
    @Deprecated("Use 'request' instead", ReplaceWith("this.request"))
    fun configure() = request()
    fun request() {
        if (Build.VERSION.SDK_INT >= 26) {
            (HttpClient.appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                NotificationChannel(HttpClient.appContext.getString(R.string.default_notification_channel_id), "Default", NotificationManager.IMPORTANCE_DEFAULT)
            )
        }
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            notificationToken.value = it.result?.token
        }
    }
}
