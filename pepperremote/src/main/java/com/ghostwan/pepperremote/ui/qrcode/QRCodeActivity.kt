package com.ghostwan.pepperremote.ui.qrcode

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
import com.ghostwan.pepperremote.R
import com.ghostwan.pepperremote.robot.Pepper
import com.ghostwan.pepperremote.robot.Robot
import com.ghostwan.pepperremote.util.hideSystemBars
import com.ghostwan.pepperremote.util.runAfter
import com.ghostwan.pepperremote.util.start
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.activity_qrcode.*
import timber.log.Timber


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

    private lateinit var animation: AnimatedVectorDrawableCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ghostwan.pepperremote.R.layout.activity_qrcode)
    }

    override fun onResume() {
        super.onResume()
        hideSystemBars()
        showWaitingForRobot()
        bindService(RobotServiceUtil.getRobotServiceIntent(), robotServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun showQRCode(content: String) {
        runOnUiThread {
            Timber.i(content)
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
                    statusImage.setImageBitmap(bmp)
                    showWaitingForDevice()
                }

            } catch (e: WriterException) {
                showError(e)
            }
        }
    }

    override fun showError(error: Throwable) {
        runOnUiThread {
            Timber.e(error)
            statusImage.setImageResource(R.drawable.ic_error_red)
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

    override fun showWaitingForDevice() {
        runOnUiThread {
            info.setText(R.string.instruction)
            animation = connectionImage.start(true ) {
                connectionImage.visibility = View.VISIBLE
            }
        }
    }

    override fun showDeviceConnected() {
        runOnUiThread {
            animation.stop()
        }
    }

    override fun showFocusLost() {
        runOnUiThread {
            connectionImage.visibility = View.INVISIBLE
            statusImage.setImageResource(R.drawable.lost)
            info.setText(R.string.focus_lost)
            runAfter(2000) {
                finish()
            }
        }
    }

    override fun showWaitingForRobot() {
        runOnUiThread {
            connectionImage.visibility = View.INVISIBLE
            statusImage.setImageResource(R.drawable.waiting)
            info.setText(R.string.wait)
        }
    }

    class FocusRefusedException : Throwable()

}
