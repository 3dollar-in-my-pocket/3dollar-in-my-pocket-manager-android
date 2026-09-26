package app.threedollars.domain.repository

import app.threedollars.common.Resource
import app.threedollars.domain.dto.CouponDto
import app.threedollars.domain.dto.CouponListDto
import app.threedollars.domain.dto.CouponRegisterDto
import app.threedollars.domain.dto.CouponStatus

interface CouponRepository {
    suspend fun getCoupons(storeId: String, statuses: List<CouponStatus>, size: Int, cursor: String?): Resource<CouponListDto>

    suspend fun createNonce(): Resource<String>

    suspend fun registerCoupon(storeId: String, nonce: String, request: CouponRegisterDto): Resource<CouponDto>

    suspend fun closeCoupon(storeId: String, couponId: String): Resource<String>
}
