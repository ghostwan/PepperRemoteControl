package com.ghostwan.pepperremote.data.source

import android.content.pm.PackageManager
import com.ghostwan.pepperremote.data.model.App


class AppRepository(private val packageManager: PackageManager) : AppRepositoryContract {

    override fun getRoboticApps(): List<App> {
        val applications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        return applications
            .map { Pair(packageManager.getPackageInfo(it.packageName, PackageManager.GET_PERMISSIONS), it) }
            .filter {it.first.requestedPermissions != null}
            .filter { it.first.requestedPermissions.contains("com.aldebaran.permission.ROBOT") }
            .map { App(it.first.packageName, it.second.loadLabel(packageManager) as String, it.second.loadIcon(packageManager) ) }
    }

}