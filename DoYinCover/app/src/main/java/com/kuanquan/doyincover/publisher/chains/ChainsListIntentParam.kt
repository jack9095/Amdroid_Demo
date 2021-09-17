//package com.kuanquan.doyincover.publisher.chains
//
//import java.io.Serializable
//import java.util.*
//
///**
// * Author:  Chenglong.Lu
// * Email:   1053998178@qq.com | w490576578@gmail.com
// * Date:    2018/12/11
// * Description:
// */
//class ChainsListIntentParam(
//        val feedSource: ChainFeedSource,
//        val id: String? = null,
//        val position: Int? = 0,
//        val feedList: List<Feed>,
//        var page: Int = 0,
//        val floorFeedId: String? = null,
//        val childFeedList: List<Feed>? = null,
//        val childEnterPosition: Int = RecyclerView.NO_POSITION,
//        val channelId: Long? = null,
//        var nextId: String? = null,
//        var currentFloorFeed: Feed? = null,
//        val fromMark: Int = FeedTransportManager.MARK_FROM_UNDEFINE
//) : Serializable