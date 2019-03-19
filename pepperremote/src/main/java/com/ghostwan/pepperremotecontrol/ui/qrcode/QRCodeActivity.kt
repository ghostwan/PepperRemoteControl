package com.ghostwan.pepperremotecontrol.ui.qrcode

import android.content.*
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.aldebaran.robotservice.FocusUtil
import com.aldebaran.robotservice.IRobotService
import com.aldebaran.robotservice.RobotServiceUtil
import com.ghostwan.pepperremotecontrol.R
import com.ghostwan.pepperremotecontrol.robot.Pepper
import com.ghostwan.pepperremotecontrol.robot.Robot
import com.ghostwan.pepperremotecontrol.util.hideSystemBars
import com.ghostwan.pepperremotecontrol.util.logError
import com.ghostwan.pepperremotecontrol.util.logInfo
import com.ghostwan.pepperremotecontrol.util.start
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.activity_qrcode.*


class QRCodeActivity : AppCompatActivity(), QRCodeContract.View {

    private var pepper: Robot? = null
    private var presenter: QRCodePresenter = QRCodePresenter(this)

    private val robotServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val iRobotService = IRobotService.Stub.asInterface(service)
            pepper = Pepper(iRobotService.publicEndpoint, iRobotService.publicToken)
            if (FocusUtil.isFocusPreempted(this@QRCodeActivity, iRobotService)) {
                presenter.computeQRCode(pepper as Robot, intent.getStringExtra(FocusUtil.KEY_FOCUSID))
            } else {
                registerReceiver(focusReceiver,
                    IntentFilter(FocusUtil.ACTION_FOCUS_PREEMPTED)
                        .also { it.addAction(FocusUtil.ACTION_FOCUS_REFUSED) })
            }
        }
    }

    private val focusReceiver = object : BroadcastReceiver() {
        override fun onReceive(content: Context, intent: Intent) {
            if (intent.action == FocusUtil.ACTION_FOCUS_PREEMPTED) {
                presenter.computeQRCode(pepper as Robot, this@QRCodeActivity.intent.getStringExtra(FocusUtil.KEY_FOCUSID))
            } else {
                showError(FocusRefusedException())
            }
            unregisterReceiver(this)
        }
    }

    class FocusRefusedException : Throwable()

    private lateinit var animation: AnimatedVectorDrawableCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ghostwan.pepperremotecontrol.R.layout.activity_qrcode)
    }

    override fun onResume() {
        super.onResume()
        hideSystemBars()
        qrcodeImage.setImageResource(R.drawable.robot)
        connectionImage.visibility = View.INVISIBLE
        info.setText(R.string.wait)
        bindService(RobotServiceUtil.getRobotServiceIntent(), robotServiceConnection, Context.BIND_AUTO_CREATE)
    }


    override fun showQRCode(content: String) {
        runOnUiThread {
            logInfo(content)
            val writer = QRCodeWriter()
            try {
                val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512)
                with(bitMatrix) {
                    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                    for (x in 0 until width) {
                        for (y in 0 until height) {
                            bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                        }
                    }
                    info.setText(R.string.instruction)
                    qrcodeImage.setImageBitmap(bmp)
                    animation = connectionImage.start(true ) {
                        connectionImage.visibility = View.VISIBLE
                    }
                }

            } catch (e: WriterException) {
                qrcodeImage.setImageResource(R.drawable.ic_error_red)
                showError(e)
            }
        }
    }

    override fun showError(error: Throwable) {
        runOnUiThread {
            logError(error)
            val errorID = when (error) {
                is Pepper.NoEndpointFoundException -> R.string.error_no_endpoint_found
                else -> R.string.unknown_error
            }
            Snackbar.make(mainLayout, errorID, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        hideSystemBars()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideSystemBars()
    }

    override fun onPause() {
        super.onPause()
        animation.stop()
        unbindService(robotServiceConnection)
    }



}
