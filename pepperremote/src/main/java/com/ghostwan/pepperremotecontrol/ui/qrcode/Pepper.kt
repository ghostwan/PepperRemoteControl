package com.ghostwan.pepperremotecontrol.ui.qrcode

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import com.aldebaran.qi.AnyObject
import com.aldebaran.qi.Session
import com.aldebaran.qi.Tuple
import com.aldebaran.qi.sdk.core.FocusManager
import com.aldebaran.qi.sdk.core.SessionManager
import com.aldebaran.robotservice.IRobotService
import com.aldebaran.robotservice.RobotServiceUtil

class Pepper : SessionManager.Callback, FocusManager.Callback {

    private lateinit var sessionManager: SessionManager
    private lateinit var focusManager: FocusManager

    fun init(activity: Activity, robotReady: () -> Unit){
        sessionManager = SessionManager(false).also{ it.register(activity, this) }
        focusManager = FocusManager(activity, sessionManager).also { it.register(this) }
        activity.bindService(RobotServiceUtil.getRobotServiceIntent(), robotServiceConnection, Context.BIND_AUTO_CREATE)
    }

    private val robotServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val iRobotService = IRobotService.Stub.asInterface(service)
            publicTokenText.text = iRobotService.publicToken
        }

    }

    override fun onRobotReady(session: Session) {
        val sd = session.service("ServiceDirectory").get()
        val info = sd.call<Tuple>("service", "Actuation").get()
        if (info.size() >= 5) {
            val list = info.get(4) as List<String>
            val address = list.find { !it.contains("127.0.0.1") && !it.contains("198.18.0.1") }
            runOnUiThread {
                robotIPText.text = address
                focusIDText.text = intent.getStringExtra("com.softbank.robot.focusid")
            }
        }
    }

    override fun onRobotLost() {
    }

    override fun onFocusLost() {
    }

    override fun onFocusGained(robotContext: AnyObject?) {
    }

    override fun onFocusRefused(reason: String?) {
    }

    override fun onRobotAbsent() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRobotReady(session: Session?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onFocusLost() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onFocusRefused(reason: String?) {
    }

    fun release() {
    }
}