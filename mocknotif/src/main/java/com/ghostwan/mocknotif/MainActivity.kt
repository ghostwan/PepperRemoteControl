package com.ghostwan.mocknotif

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showRobotWifiNetworkInfoNoti(this, "10.0.12.123", "wan1")
    }

    fun showRobotWifiNetworkInfoNoti(context: Context, ip: String, ssid: String) {
        val largeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.robot_network_96)

        val forRobotBrowser = context.resources.getString(R.string.wsb_noti_robot_browser)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = Notification.Builder(context)
            .setLargeIcon(largeIcon)
            .setSmallIcon(R.drawable.ic_p_alpha_96)
            .setColor(Color.rgb(205, 222, 229))
            .setContentTitle("$ip ($forRobotBrowser)")
            .setContentText("$ssid (SSID)")
            .setWhen(0)

        notificationManager.notify(1011, builder.build())
    }
}
