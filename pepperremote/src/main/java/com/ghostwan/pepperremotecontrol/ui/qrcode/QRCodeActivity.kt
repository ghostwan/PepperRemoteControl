package com.ghostwan.pepperremotecontrol.ui.qrcode

import android.content.*
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import com.aldebaran.robotservice.FocusUtil
import com.aldebaran.robotservice.IRobotService
import com.aldebaran.robotservice.RobotServiceUtil
import com.ghostwan.pepperremotecontrol.R
import com.ghostwan.pepperremotecontrol.util.logError
import com.ghostwan.pepperremotecontrol.robot.Pepper
import com.ghostwan.pepperremotecontrol.robot.Robot
import com.ghostwan.pepperremotecontrol.util.logInfo
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
            pepper = Pepper(iRobotService.publicEndpoint, password = iRobotService.publicToken)
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
                presenter.computeQRCode(pepper as Robot, intent.getStringExtra(FocusUtil.KEY_FOCUSID))
            } else {
                showError(FocusRefusedException())
            }
            unregisterReceiver(this)
        }
    }

    class FocusRefusedException : Throwable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ghostwan.pepperremotecontrol.R.layout.activity_qrcode)
        bindService(RobotServiceUtil.getRobotServiceIntent(), robotServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(robotServiceConnection)
    }

    override fun showQRCode(content: String) {
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
                qrcodeImage.setImageBitmap(bmp)
            }

        } catch (e: WriterException) {
            qrcodeImage.setImageResource(R.drawable.ic_error_red)
            showError(e)
        }
    }

    override fun showError(error: Throwable) {
        logError(error)
        val errorID = when (error) {
            is Pepper.NoEndpointFoundException -> R.string.error_no_endpoint_found
            else -> R.string.unknown_error
        }
        Snackbar.make(mainLayout, errorID, Snackbar.LENGTH_SHORT).show()
    }

}
