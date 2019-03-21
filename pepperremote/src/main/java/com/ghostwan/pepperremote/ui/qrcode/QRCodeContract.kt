package com.ghostwan.pepperremote.ui.qrcode

import com.ghostwan.pepperremote.robot.Robot

interface QRCodeContract {
    interface View {
        fun showQRCode(content: String)
        fun showWaitingForDevice()
        fun showDeviceConnected()
        fun showFocusLost()
        fun showWaitingForRobot()
        fun showError(error: Throwable)
    }
    interface Presenter{
        fun computeQRCode(robot: Robot, focusToken: String)
    }
}