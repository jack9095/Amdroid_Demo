//package com.kuanquan.doyincover.utils
//
//import com.sensorsdata.analytics.android.sdk.SensorsDataAPI
//import com.shuashuakan.android.commons.cache.Storage
//import com.shuashuakan.android.config.AppConfig
//import com.shuashuakan.android.data.api.model.account.UserAccount
//import com.shuashuakan.android.modules.account.AccountManager
//import com.shuashuakan.android.modules.profile.fragment.ProfileFragment
//import com.kuanquan.doyincover.utils.formatTime
//import org.json.JSONObject
//import javax.inject.Inject
//import javax.inject.Singleton
//
//@Singleton
//class Logout @Inject constructor(private val accountManager: AccountManager,
//                                 private val appConfig: AppConfig,
//                                 private val storage: Storage) {
//
//  fun logout() {
//    appConfig.setHasUnreadMessage(false)
//    SensorsDataAPI.sharedInstance()
//        .logout()
//    SensorsDataAPI.sharedInstance()
//        .profileSet(JSONObject().put("last_logout_time", formatTime(System.currentTimeMillis())))
//
//    storage.userCache.cacheOf<UserAccount>().remove(ProfileFragment.ACCOUNT_CACHE_KEY)
//    accountManager.logout()
//  }
//
//}