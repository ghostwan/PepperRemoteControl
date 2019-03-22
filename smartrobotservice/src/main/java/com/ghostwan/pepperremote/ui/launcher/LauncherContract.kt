package com.ghostwan.pepperremote.ui.launcher

import com.ghostwan.pepperremote.data.model.App

interface LauncherContract {
    interface View {
        fun showLoadingIndicator()
        fun hideLoadingIndicator()
        fun showAppList(apps: List<App>)
        fun showEmptyList()
        fun showApplication(app: App)
        fun showUninstallApplication(app: App)
        fun showError(throwable: Throwable)
    }
    interface Presenter {
        fun fetchRoboticAppList(autoStart: Boolean=false)
        fun startApplication(app: App)
        fun deleteApplication(app: App)
    }
}