package app.threedollars.manager.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import app.threedollars.manager.home.screen.currentPosition
import app.threedollars.manager.isGpsAvailable
import app.threedollars.manager.isLocationAvailable
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi

@OptIn(ExperimentalNaverMapApi::class)
@SuppressLint("MissingPermission")
fun moveToCurrentLocation(
    activity: AppCompatActivity?,
    cameraPositionState: CameraPositionState
) {
    try {
        val fusedLocationProviderClient = activity?.let {
            LocationServices.getFusedLocationProviderClient(it)
        }
        if (isLocationAvailable() && isGpsAvailable()) {
            val locationResult = fusedLocationProviderClient?.lastLocation
            locationResult?.addOnSuccessListener {
                if (it != null) {
                    currentPosition = LatLng(it.latitude, it.longitude)
                    currentPosition?.let { position ->
                        cameraPositionState.move(CameraUpdate.scrollTo(position))
                    }
                }
            }
        }
    } catch (e: Exception) {
        Log.e("currentLocation", e.message ?: "")
        cameraPositionState.move(CameraUpdate.scrollTo(LatLng(37.56, 126.97)))
    }
}