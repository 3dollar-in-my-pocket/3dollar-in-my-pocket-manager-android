package app.threedollars.manager.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.core.app.ActivityCompat
import app.threedollars.manager.R
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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
fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}

inline fun Modifier.noRippleClickable(crossinline onClick: ()->Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

fun Context.hasPermissions(permissions: List<String>): Boolean = permissions.all {
    ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
}

suspend fun convertImageUrlToRequestBody(imageUrl: String): RequestBody? {
    return withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(imageUrl)
                .build()
            val response = client.newCall(request).execute()
            val body = response.body?.bytes()
            body?.toRequestBody("image/*".toMediaTypeOrNull())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}