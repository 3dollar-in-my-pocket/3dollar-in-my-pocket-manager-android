package app.threedollars.manager.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import app.threedollars.manager.R
import com.naver.maps.geometry.LatLng
import java.util.*

fun Context.getCurrentLocationName(location: LatLng?): String {
    val notFindMsg = getString(R.string.location_no_address)
    if (location == null) {
        return notFindMsg
    }

    val geoCoder = Geocoder(this, Locale.KOREA)
    return try {
        val addresses: List<Address> =
            geoCoder.getFromLocation(location.latitude, location.longitude, 1) as List<Address>
        if (addresses.isEmpty()) {
            notFindMsg
        } else {
            with(addresses[0]) {
                "$adminArea $subLocality"
            }
        }
    } catch (e: Exception) {
        Log.e("getCurrentLocationName", e.message ?: "")
        notFindMsg
    }
}