package com.ghostwan.pepperremote.data.source

import android.content.pm.PackageManager
import com.ghostwan.pepperremote.data.model.App


class AppRepository(private val packageManager: PackageManager) : AppDataSource {
    override fun fetchRoboticApps(): List<App> {
        val packages = packageManager.getPermissionInfo(PackageManager.GET_PERMISSIONS)
        return packages
            .filter {
                it.permission?.also {

                }
                false
            }
            .map { App(it.packageName) }
    }

}