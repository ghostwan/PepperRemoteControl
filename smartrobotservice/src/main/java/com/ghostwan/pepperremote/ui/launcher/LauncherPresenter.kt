package com.ghostwan.pepperremote.ui.launcher

import com.ghostwan.pepperremote.data.model.App
import com.ghostwan.pepperremote.data.source.AppRepositoryContract
import com.ghostwan.pepperremote.tools.toBase64
import kotlinx.coroutines.launch

class LauncherPresenter(override var view: LauncherContract.View?,
                        private val appRepository: AppRepositoryContract) : LauncherContract.Presenter {


    override suspend fun fetchRoboticAppList(autoStart: Boolean) {
        launch {
            view?.showLoadingIndicator()
            val apps = appRepository.getRoboticApps()
            when {
                apps.isEmpty() -> view?.showEmptyList()
                apps.size == 1 && autoStart -> startApplication(apps[0])
                else -> view?.showAppList(apps)
            }
            view?.hideLoadingIndicator()
        }
    }

    override suspend fun startApplication(app: App) {
        view?.showApplication(app)
        launch {
            val base64Image = app.drawable.toBase64()
        }
        /*TODO After starting application send to the app server the name and the icon of the application
        *  launch {
        *     drawable to base 64
        *     https://stackoverflow.com/questions/38739244/convert-image-to-base64-string-android
        *     appRepository.send
        *  }
        * */

    }

    override suspend fun deleteApplication(app: App) {
        view?.showUninstallApplication(app)
    }
}