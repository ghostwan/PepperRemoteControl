package com.softbankrobotics.pepperremote.sampleqisdk

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.aldebaran.qi.Session
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.builder.SayBuilder
import com.aldebaran.qi.sdk.core.FocusManager
import com.aldebaran.qi.sdk.core.SessionManager


class MainActivity : AppCompatActivity(), SessionManager.Callback {

    override fun onRobotAbsent() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRobotReady(session: Session?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRobotLost() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sessionManager = SessionManager(false)
        sessionManager.register(this, this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
