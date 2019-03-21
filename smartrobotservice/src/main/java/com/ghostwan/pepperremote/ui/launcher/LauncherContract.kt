package com.ghostwan.pepperremote.ui.launcher

import com.ghostwan.pepperremote.data.model.App

interface LauncherContract {
    interface View {
        fun showAppList(list: List<App>)
    }
    interface Presenter {
    }
}