package com.ghostwan.pepperremote.ui.launcher

import com.ghostwan.pepperremote.data.model.App
import com.ghostwan.pepperremote.data.source.AppRepositoryContract
import kotlinx.coroutines.launch

class LauncherPresenter(override var view: LauncherContract.View?,
                        private val repository: AppRepositoryContract) : LauncherContract.Presenter {


    override suspend fun fetchRoboticAppList(autoStart: Boolean) {
        launch {
            view?.showLoadingIndicator()
            val apps = repository.getRoboticApps()
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
    }

    override suspend fun deleteApplication(app: App) {
        view?.showUninstallApplication(app)
    }
}