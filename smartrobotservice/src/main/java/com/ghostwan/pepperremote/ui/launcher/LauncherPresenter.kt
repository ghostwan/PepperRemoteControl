package com.ghostwan.pepperremote.ui.launcher

import com.ghostwan.pepperremote.data.model.App
import com.ghostwan.pepperremote.data.source.AppRepositoryContract

class LauncherPresenter(private val view: LauncherContract.View,
                        private val repository: AppRepositoryContract) : LauncherContract.Presenter {


    override fun fetchRoboticAppList(autoStart: Boolean) {
        view.showLoadingIndicator()
        val apps = repository.getRoboticApps()
        when {
            apps.isEmpty() -> view.showEmptyList()
            apps.size == 1 && autoStart -> startApplication(apps[0])
            else -> view.showAppList(apps)
        }
        view.hideLoadingIndicator()
    }

    override fun startApplication(app: App) {
        view.showApplication(app)
    }

    override fun deleteApplication(app: App) {
        view.showUninstallApplication(app)
    }
}