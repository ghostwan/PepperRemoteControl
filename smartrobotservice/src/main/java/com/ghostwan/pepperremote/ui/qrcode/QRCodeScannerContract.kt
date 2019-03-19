package com.ghostwan.pepperremote.ui.qrcode

interface QRCodeScannerContract {
    interface View {
        fun showRobotInformation(endpoint: String, publicToken: String, focusToken: String)
        fun showError(error: Throwable)
    }
    interface Presenter {
        fun computeQRCodeInformation(information: String)
    }
}