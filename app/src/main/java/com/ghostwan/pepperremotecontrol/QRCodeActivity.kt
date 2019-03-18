package com.ghostwan.pepperremotecontrol

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_qrcode.*


class QRCodeActivity : AppCompatActivity() {

    var binder: NotificationListener.LocalBinder? = null

    private val connection =  object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
        }

        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            binder = service as NotificationListener.LocalBinder
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
        click.setOnClickListener { binder?.getService()?.getNotifications() }

        if(!Settings.Secure.getString(contentResolver, "enabled_notification_listeners").contains(applicationContext.packageName))
            startActivityForResult(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), 1)
        else {
            val result = bindService(Intent(this, NotificationListener::class.java), connection, Context.BIND_AUTO_CREATE)
            info("Binding service: $result")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        bindService(Intent(this, NotificationListener::class.java), connection, 0)
    }


}
