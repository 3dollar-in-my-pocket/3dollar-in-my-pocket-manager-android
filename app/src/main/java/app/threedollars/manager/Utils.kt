package app.threedollars.manager

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.threedollars.data.db.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

fun Activity.requestPermissionIfNeeds(permission: String = ACCESS_FINE_LOCATION) {
    when {
        isLocationAvailable() -> {
            return
        }
        ActivityCompat.shouldShowRequestPermissionRationale(this, permission) -> {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 0)
        }
        else -> {
            // TODO: 사용법 알아야함
            flow<Boolean> {
                isFirstPermissionCheck().collect { isFirstPermissionCheck ->
                    if (isFirstPermissionCheck) {
                        ActivityCompat.requestPermissions(
                            this@requestPermissionIfNeeds,
                            arrayOf(permission),
                            0
                        )
                    } else {
                        openPermissionSettingPage()
                    }
                }
            }
        }
    }
}

fun Context.getActivity(): AppCompatActivity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is AppCompatActivity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}

fun isLocationAvailable(): Boolean =
    ContextCompat.checkSelfPermission(
        MainApplication.context(),
        ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

fun isGpsAvailable(): Boolean {
    val locationManager =
        MainApplication.context().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun isFirstPermissionCheck(): Flow<Boolean> = flow {
    DataStoreManager(MainApplication.context()).getBooleanData("FIRST_PERMISSION_CHECK")
        .collect { isFirstPermissionCheck ->
            if (isFirstPermissionCheck) {
                DataStoreManager(MainApplication.context()).saveBooleanData(
                    "FIRST_PERMISSION_CHECK",
                    false
                )
            }
        }
}

private fun Activity.openPermissionSettingPage() {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    intent.data = Uri.fromParts("package", packageName, null)
    startActivity(intent)
}