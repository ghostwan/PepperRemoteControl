package com.ghostwan.pepperremote.data.source

import com.ghostwan.pepperremote.data.model.App

interface AppRepositoryContract {

    fun getRoboticApps(): List<App>
}