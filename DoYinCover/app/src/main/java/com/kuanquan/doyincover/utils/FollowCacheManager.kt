//package com.kuanquan.doyincover.utils
//
//import com.kuanquan.doyincover.DuckApplication
//import com.shuashuakan.android.data.RxBus
//import com.shuashuakan.android.event.VideoChainClearFollowEvent
//import com.shuashuakan.android.event.VideoChainFollowEvent
//
///**
// * @author hushiguang
// * @since 2019-06-11.
// * Copyright © 2019 SSK Technology Co.,Ltd. All rights reserved.
// */
//class FollowCacheManager {
//    companion object {
//        fun putFollowUserToCache(userId: String, isFollow: Boolean) {
//            putFollowUserToCacheWithEvent(userId, isFollow, true)
//        }
//
//        fun putFollowUserToCacheWithEvent(userId: String, isFollow: Boolean, sendEvent: Boolean = false) {
//            DuckApplication.CACHE_USER_FOLLOW_STATUS[userId] = isFollow
//            if (sendEvent) {
//                RxBus.get().post(VideoChainFollowEvent(userId, isFollow))
//            }
//        }
//
//        fun isUserFollowInCache(userId: String): Boolean? {
//            return DuckApplication.CACHE_USER_FOLLOW_STATUS[userId]
//        }
//
//        fun clearCache() {
//            DuckApplication.CACHE_USER_FOLLOW_STATUS.clear()
//            RxBus.get().post(VideoChainClearFollowEvent())
//        }
//    }
//}