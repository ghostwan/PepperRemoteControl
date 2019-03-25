package com.ghostwan.pepperremote.ui.qrcode

import timber.log.Timber

class QRCodeScannerPresenter(override var view: QRCodeScannerContract.View?) : QRCodeScannerContract.Presenter {

    override suspend fun computeQRCodeInformation(information: String) {
        Timber.v("QRCode content: $information")
        val data = information.split(";")
        if (data.size == 3)
            view?.showRobotInformation(data[0], data[1], data[2])
        else {
            view?.showError(CorruptedDataException())
        }
    }
}