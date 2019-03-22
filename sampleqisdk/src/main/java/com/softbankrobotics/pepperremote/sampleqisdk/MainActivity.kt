package com.softbankrobotics.pepperremote.sampleqisdk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.builder.SayBuilder

class MainActivity : AppCompatActivity(), RobotLifecycleCallbacks {

    override fun onRobotFocusGained(qiContext: QiContext?) {
        SayBuilder.with(qiContext).withText("Hello remote").build().run()
    }

    override fun onRobotFocusLost() {
    }

    override fun onRobotFocusRefused(reason: String?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        QiSDK.register(this, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        QiSDK.unregister(this)
    }
}
