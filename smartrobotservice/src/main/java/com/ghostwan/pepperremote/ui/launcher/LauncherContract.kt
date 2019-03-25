package com.ghostwan.pepperremote.ui.launcher

import com.ghostwan.pepperremote.data.model.App
import com.ghostwan.pepperremote.ui.CoroutineContract

interface LauncherContract {
    interface View : CoroutineContract.View{
        suspend fun showLoadingIndicator()
        suspend fun hideLoadingIndicator()
        suspend fun showAppList(apps: List<App>)
        suspend fun showEmptyList()
        suspend fun showApplication(app: App)
        suspend fun showUninstallApplication(app: App)
        suspend fun showError(throwable: Throwable)
    }
    interface Presenter : CoroutineContract.Presenter<View>{
        suspend fun fetchRoboticAppList(autoStart: Boolean=false)
        suspend fun startApplication(app: App)
        suspend fun deleteApplication(app: App)
    }
}