package com.ghostwan.pepperremote.ui.info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ghostwan.pepperremote.R
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ROBOT_ENDPOINT = "EXTRA_ROBOT_ENDPOINT"
        const val EXTRA_PUBLIC_TOKEN = "EXTRA_PUBLIC_TOKEN"
        const val EXTRA_FOCUS_TOKEN = "EXTRA_FOCUS_TOKEN"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
    }

    override fun onStart() {
        super.onStart()
        robotEndpoint.text = intent.getStringExtra(EXTRA_ROBOT_ENDPOINT)
        publicToken.text = intent.getStringExtra(EXTRA_PUBLIC_TOKEN)
        focusToken.text = intent.getStringExtra(EXTRA_FOCUS_TOKEN)
    }
}
