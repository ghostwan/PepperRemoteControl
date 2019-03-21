package com.ghostwan.pepperremote.ui.qrcode

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ghostwan.pepperremote.SmartRobotServiceApplication.Companion.EXTRA_FOCUS_TOKEN
import com.ghostwan.pepperremote.SmartRobotServiceApplication.Companion.EXTRA_PUBLIC_TOKEN
import com.ghostwan.pepperremote.SmartRobotServiceApplication.Companion.EXTRA_ROBOT_ENDPOINT
import com.ghostwan.pepperremote.R
import com.ghostwan.pepperremote.service.RobotService
import com.ghostwan.pepperremote.service.RobotService.Companion.KEY_FOCUSID
import com.ghostwan.pepperremote.ui.launcher.LauncherActivity
import com.google.android.material.snackbar.Snackbar
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView
import timber.log.Timber



class QRCodeScannerActivity : AppCompatActivity(), QRCodeScannerContract.View, ZBarScannerView.ResultHandler {


    private lateinit var scannerView: ZBarScannerView
    private lateinit var presenter:  QRCodeScannerContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerView = ZBarScannerView(this)
        setContentView(scannerView)
        presenter = QRCodeScannerPresenter(this)
    }

    override fun onResume() {
        super.onResume()
        val permissions = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permissions != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }
        else {
            scannerView.setResultHandler(this)
            scannerView.startCamera()
        }
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    override fun handleResult(result: Result) {
        presenter.computeQRCodeInformation(result.contents)
    }

    override fun showRobotInformation(endpoint: String, publicToken: String, focusToken: String) {
        val activityIntent = Intent(this, LauncherActivity::class.java).apply {
            putExtra(EXTRA_ROBOT_ENDPOINT, endpoint)
            putExtra(EXTRA_PUBLIC_TOKEN, publicToken)
            putExtra(EXTRA_FOCUS_TOKEN, focusToken)
        }
//        startActivity(activityIntent)
        val serviceIntent = Intent(this, RobotService::class.java).apply {
            putExtra(EXTRA_ROBOT_ENDPOINT, endpoint)
            putExtra(EXTRA_PUBLIC_TOKEN, publicToken)
        }
        startService(serviceIntent)

        val launchIntent = packageManager.getLaunchIntentForPackage("com.softbankrobotics.pepperremote.sampleqisdk")
        launchIntent?.apply {
            putExtra(KEY_FOCUSID, focusToken )
        }
        startActivity(launchIntent)//null pointer check in case package name was not found
    }

    override fun showError(error: Throwable) {
        runOnUiThread {
            Timber.e(error)
            val errorID = when (error) {
                is CorruptedDataException -> R.string.error_qrcode_corrupted
                else -> R.string.unknown_error
            }
            Snackbar.make(scannerView, errorID, Snackbar.LENGTH_LONG).show()
            scannerView.setResultHandler(this)
            scannerView.startCamera()
        }
    }

}
