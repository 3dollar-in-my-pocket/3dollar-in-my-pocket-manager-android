package app.threedollars.source

import app.threedollars.db.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val dataStoreManager: DataStoreManager) : LocalDataSource {
    override suspend fun saveSocialAccessToken(token: String) = flow {
        emit(dataStoreManager.saveStringData(SOCIAL_ACCESS_TOKEN, token))
    }

    override suspend fun saveAccessToken(token: String) = flow {
        emit(dataStoreManager.saveStringData(ACCESS_TOKEN, token))
    }

    override suspend fun saveVersionName(version: String) = flow {
        emit(dataStoreManager.saveStringData(VERSION_NAME, version))
    }

    override suspend fun saveApplicationId(applicationId: String) = flow {
        emit(dataStoreManager.saveStringData(APPLICATION_ID, applicationId))
    }

    override suspend fun saveDemoCode(code: String) = flow {
        emit(dataStoreManager.saveStringData(DEMO_CODE, code))
    }

    override fun getSocialAccessToken(): Flow<String> = dataStoreManager.getStringData(SOCIAL_ACCESS_TOKEN)
    override fun getAccessToken(): Flow<String> = dataStoreManager.getStringData(ACCESS_TOKEN)


    override fun getVersionName(): Flow<String> = dataStoreManager.getStringData(VERSION_NAME)

    override fun getApplicationId(): Flow<String> = dataStoreManager.getStringData(APPLICATION_ID)

    override fun getDemoCode(): Flow<String> = dataStoreManager.getStringData(DEMO_CODE)

    override fun isCouponNewBadgeShown(): Flow<Boolean> = dataStoreManager.getBooleanData(COUPON_NEW_BADGE_SHOWN)

    override suspend fun saveCouponNewBadgeShown() = dataStoreManager.saveBooleanData(COUPON_NEW_BADGE_SHOWN, true)

    override fun isCouponTooltipShown(): Flow<Boolean> = dataStoreManager.getBooleanData(COUPON_TOOLTIP_SHOWN)

    override suspend fun saveCouponTooltipShown() = dataStoreManager.saveBooleanData(COUPON_TOOLTIP_SHOWN, true)

    override fun isMessageMainTabTooltipShown(): Flow<Boolean> = dataStoreManager.getBooleanData(MESSAGE_MAIN_TAB_TOOLTIP_SHOWN)

    override suspend fun saveMessageMainTabTooltipShown() = dataStoreManager.saveBooleanData(MESSAGE_MAIN_TAB_TOOLTIP_SHOWN, true)

    override fun isMessageSubTabTooltipShown(): Flow<Boolean> = dataStoreManager.getBooleanData(MESSAGE_SUB_TAB_TOOLTIP_SHOWN)

    override suspend fun saveMessageSubTabTooltipShown() = dataStoreManager.saveBooleanData(MESSAGE_SUB_TAB_TOOLTIP_SHOWN, true)

    override suspend fun clearMessageTooltipShown() {
        dataStoreManager.saveBooleanData(MESSAGE_MAIN_TAB_TOOLTIP_SHOWN, false)
        dataStoreManager.saveBooleanData(MESSAGE_SUB_TAB_TOOLTIP_SHOWN, false)
    }

    companion object {
        const val SOCIAL_ACCESS_TOKEN = "social_access_token"
        const val ACCESS_TOKEN = "access_token"
        const val VERSION_NAME = "version_name"
        const val APPLICATION_ID = "application_id"
        const val DEMO_CODE = "demo_code"
        const val COUPON_NEW_BADGE_SHOWN = "coupon_new_badge_shown"
        const val COUPON_TOOLTIP_SHOWN = "coupon_tooltip_shown"
        const val MESSAGE_MAIN_TAB_TOOLTIP_SHOWN = "message_main_tab_tooltip_shown"
        const val MESSAGE_SUB_TAB_TOOLTIP_SHOWN = "message_sub_tab_tooltip_shown"
    }
}