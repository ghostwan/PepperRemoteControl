package com.ghostwan.pepperremotecontrol

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class NotificationListener : NotificationListenerService() {

    override fun onBind(intent: Intent): IBinder? {
        val action = intent.action
        info("onBind: $action")

        return if (NotificationListenerService.SERVICE_INTERFACE == action) {
            info( "Bound by system")
            super.onBind(intent)
        } else {
            info( "Bound by application")
            LocalBinder()
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startid: Int): Int {
        return Service.START_STICKY
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        info("onNotificationPosted")
        super.onNotificationPosted(sbn)
            val title = sbn.notification.extras.getString("android.title")
            val text = sbn.notification.extras.getString("android.text")
            val packageName = sbn.packageName
            info("Notification title is $title, text is $text, package is $packageName")
    }

    fun getNotifications() {
        info("getNotifications")

        activeNotifications?.apply {
            for ( notif in this) {
                val title = notif.notification.extras.getString("android.title")
                val text = notif.notification.extras.getString("android.text")
                val packageName = notif.packageName
                info("Notification title is $title, text is $text, package is $packageName")
            }
        }
    }

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): NotificationListener = this@NotificationListener
    }
}
