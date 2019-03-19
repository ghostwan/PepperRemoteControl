package com.ghostwan.pepperremotecontrol.ui.qrcode

import com.ghostwan.pepperremotecontrol.robot.Robot

class QRCodePresenter(private val view: QRCodeContract.View) : QRCodeContract.Presenter {
    override fun computeQRCode(robot: Robot, focusToken: String) {
        robot.getRemoteEndpoint().thenConsume {
            if(it.hasError()) {
                view.showError(it.error)
            }
            else {
                val qrcodeValue = "${it.get()}:${robot.getPublicToken()}:$focusToken"
                view.showQRCode(qrcodeValue)
            }
        }
    }

}