package com.cidra.hologram_beta.ui

import android.app.Activity
import android.util.Log
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isImmediateUpdateAllowed

class AppUpdateService() {
    fun runAppUpdate(activity: Activity) {
        val appUpdateManager = AppUpdateManagerFactory.create(activity)
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                info.isImmediateUpdateAllowed
            ) {
                try {
                    val options = AppUpdateOptions.defaultOptions(AppUpdateType.IMMEDIATE)
                    appUpdateManager.startUpdateFlow(info, activity, options)
                } catch (e: Exception) {
                    Log.i("", "")
                }
            }

        }

    }
}