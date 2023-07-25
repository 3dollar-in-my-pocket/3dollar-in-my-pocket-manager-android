package app.threedollars.manager.storeManagement.data

import okhttp3.RequestBody

data class MenuModel(
    var imageRequestBody: RequestBody? = null,
    var imageUrl: String? = null,
    var name: String? = null,
    var price: Int? = null,
    var isEmpty : Boolean = false
)