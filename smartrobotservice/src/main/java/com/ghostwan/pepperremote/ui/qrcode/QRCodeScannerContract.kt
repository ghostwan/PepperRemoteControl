package com.ghostwan.pepperremote.ui.qrcode

import com.ghostwan.pepperremote.ui.CoroutineContract

interface QRCodeScannerContract  {
    interface View : CoroutineContract.View {
        suspend fun showRobotInformation(endpoint: String, publicToken: String, focusToken: String)
        suspend fun showError(error: Throwable)
    }
    interface Presenter :CoroutineContract.Presenter<View> {
        suspend fun computeQRCodeInformation(information: String)
    }
}