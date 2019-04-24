package com.ghostwan.pepperremote.ui.qrcode

import com.ghostwan.pepperremote.robot.Robot

class QRCodePresenter(private val view: QRCodeContract.View) : QRCodeContract.Presenter {
    override fun computeQRCode(robot: Robot, focusToken: String) {
        robot.getRemoteEndpoint().thenConsume {
            if(it.hasError()) {
                view.showError(it.error)
            }
            else {
                val qrcodeValue = "${it.get()}$SEPARATOR${robot.getPublicToken()}$SEPARATOR$focusToken"
                view.showQRCode(qrcodeValue)
            }
        }
        robot.watchFocusLost {
            view.showFocusLost()
        }
    }

    companion object {
        private const val SEPARATOR: String = ";"
    }

}