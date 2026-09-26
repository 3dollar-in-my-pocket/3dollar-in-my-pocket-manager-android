package app.threedollars.repository

import app.threedollars.common.Resource
import app.threedollars.common.ext.toStringDefault
import app.threedollars.data.request.CouponRegisterRequest
import app.threedollars.domain.dto.CouponDto
import app.threedollars.domain.dto.CouponListDto
import app.threedollars.domain.dto.CouponRegisterDto
import app.threedollars.domain.dto.CouponStatus
import app.threedollars.domain.repository.CouponRepository
import app.threedollars.source.RemoteDataSource
import javax.inject.Inject

internal class CouponRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : CouponRepository {

    override suspend fun getCoupons(
        storeId: String,
        statuses: List<CouponStatus>,
        size: Int,
        cursor: String?,
    ): Resource<CouponListDto> = runCatching {
        remoteDataSource.getCoupons(storeId = storeId, statuses = statuses.map { it.name }, size = size, cursor = cursor)
    }.fold(
        onSuccess = {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto(), code = it.code)
            } else {
                Resource.Error(errorMessage = it.errorMessage, code = it.code)
            }
        },
        onFailure = { Resource.Error(errorMessage = it.message, code = "700") }
    )

    override suspend fun createNonce(): Resource<String> = runCatching {
        remoteDataSource.postNonce()
    }.fold(
        onSuccess = {
            if (it.data != null) {
                Resource.Success(data = it.data!!.nonce, code = it.code)
            } else {
                Resource.Error(errorMessage = it.errorMessage, code = it.code)
            }
        },
        onFailure = { Resource.Error(errorMessage = it.message, code = "700") }
    )

    override suspend fun registerCoupon(storeId: String, nonce: String, request: CouponRegisterDto): Resource<CouponDto> =
        runCatching {
            remoteDataSource.postCoupon(storeId = storeId, nonce = nonce, request = CouponRegisterRequest.from(request))
        }.fold(
            onSuccess = {
                if (it.data != null) {
                    Resource.Success(data = it.data!!.toDto(), code = it.code)
                } else {
                    Resource.Error(errorMessage = it.errorMessage, code = it.code)
                }
            },
            onFailure = { Resource.Error(errorMessage = it.message, code = "700") }
        )

    override suspend fun closeCoupon(storeId: String, couponId: String): Resource<String> = runCatching {
        remoteDataSource.putCouponClose(storeId = storeId, couponId = couponId)
    }.fold(
        onSuccess = {
            if (it.data != null) {
                Resource.Success(data = it.data.toStringDefault(), code = it.code)
            } else {
                Resource.Error(errorMessage = it.errorMessage, code = it.code)
            }
        },
        onFailure = { Resource.Error(errorMessage = it.message, code = "700") }
    )
}
