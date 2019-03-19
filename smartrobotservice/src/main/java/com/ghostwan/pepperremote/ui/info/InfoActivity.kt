package com.ghostwan.pepperremote.ui.info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ghostwan.pepperremote.App.Companion.EXTRA_FOCUS_TOKEN
import com.ghostwan.pepperremote.App.Companion.EXTRA_PUBLIC_TOKEN
import com.ghostwan.pepperremote.App.Companion.EXTRA_ROBOT_ENDPOINT
import com.ghostwan.pepperremote.R
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {

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
