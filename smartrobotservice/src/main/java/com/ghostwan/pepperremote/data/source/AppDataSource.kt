package com.ghostwan.pepperremote.data.source

import com.ghostwan.pepperremote.data.model.App

interface AppDataSource {

    fun fetchRoboticApps(): List<App>
}