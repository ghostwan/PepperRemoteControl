package com.ghostwan.pepperremotecontrol.ui.qrcode

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ghostwan.pepperremotecontrol.R


class QRCodeActivity : AppCompatActivity() {


    private lateinit var pepper: Pepper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
        pepper.init(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        pepper.release()
        unbindService(robotServiceConnection)
    }


}
