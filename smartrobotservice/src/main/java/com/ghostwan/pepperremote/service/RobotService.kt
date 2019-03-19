package com.ghostwan.pepperremote.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.aldebaran.robotservice.IRobotService
import com.ghostwan.pepperremote.App.Companion.EXTRA_PUBLIC_TOKEN
import com.ghostwan.pepperremote.App.Companion.EXTRA_ROBOT_ENDPOINT

class RobotService : Service() {
    private val binder = RobotServiceBinder(this)
    private lateinit var publicToken: String
    private lateinit var robotEndpoint: String

    companion object {
        const val ACTION_ROBOT_SERVICE = "com.aldebaran.action.ROBOT_SERVICE"
        const val ACTION_FOCUS_PREEMPTED = "com.aldebaran.action.FOCUS_PREEMPTED"
        const val ACTION_FOCUS_REFUSED = "com.aldebaran.action.FOCUS_REFUSED"
        const val EXTRA_FOCUS_PREEMPTED_TARGET = "com.aldebaran.extra.EXTRA_FOCUS_PREEMPTED_TARGET"
        const val EXTRA_FOCUS_PREEMPTED_ID = "com.aldebaran.extra.EXTRA_FOCUS_PREEMPTED_ID"
        const val EXTRA_FOCUS_REFUSED_REASON = "com.aldebaran.extra.EXTRA_FOCUS_REFUSED_REASON"
        const val KEY_FOCUSID = "com.softbank.robot.focusid"
    }

    override fun onBind(intent: Intent): IBinder? {
        return when(intent.action) {
            ACTION_ROBOT_SERVICE -> binder
            else -> null
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        robotEndpoint = intent.getStringExtra(EXTRA_ROBOT_ENDPOINT)
        publicToken = intent.getStringExtra(EXTRA_PUBLIC_TOKEN)
        return super.onStartCommand(intent, flags, startId)
    }

    class RobotServiceBinder(val service: RobotService) : IRobotService.Stub() {
        override fun getPrivateToken(): String? = null
        override fun getPrivateEndpoint(): String? = null
        override fun getRobotState(): Int = 1
        override fun isFocusPreempted(activityName: String?): Boolean = true
        override fun isFocusPreemptedWithID(activityName: String?, activityID: String?): Boolean = true

        override fun getPublicToken(): String = service.publicToken
        override fun getPublicEndpoint(): String = service.robotEndpoint

    }


}
