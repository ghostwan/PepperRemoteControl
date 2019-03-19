package com.ghostwan.pepperremote.ui.qrcode

import com.ghostwan.pepperremote.robot.Robot

interface QRCodeContract {
    interface View {
        fun showQRCode(content: String)
        fun showError(error: Throwable)
    }
    interface Presenter{
        fun computeQRCode(robot: Robot, focusToken: String)
    }
}