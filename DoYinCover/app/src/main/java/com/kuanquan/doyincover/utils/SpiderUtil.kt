//package com.kuanquan.doyincover.utils
//
//import android.content.Context
//import com.kuanquan.doyincover.utils.getUserId
//import com.shuashuakan.android.spider.EventCreator
//import com.shuashuakan.android.spider.Spider
//import com.shuashuakan.android.spider.SpiderEventNames
//import com.umeng.analytics.dplus.UMADplus.track
//
///**
// * Author:  treasure_ct
// * Date:    2018/27/11
// */
//
//fun Spider.spiderEvent(eventName: String, userId: String) {
//    manuallyEvent(eventName).put("userID", userId)
//}
//
///**
// * 视频相关
// */
//fun Spider.feedEvent(eventName: String, userId: Long?, feedId: String): EventCreator {
//    return if (userId == null) {
//        manuallyEvent(eventName).put("feedID", feedId)
//    } else {
//        manuallyEvent(eventName).put("userID", userId).put("feedID", feedId)
//    }
//}
//
//
///**
// * 视频播放 相关
// */
//fun Spider.feedPlayEvent(eventName: String, userId: Long?, source: String, contentID: String,
//                         contentDuration: Long): EventCreator {
//    return if (userId == null) {
//        manuallyEvent(eventName).put("source", getChangeSource(source)).put("contentID", contentID)
//                .put("contentDuration", contentDuration)
//    } else {
//        manuallyEvent(eventName).put("userID", userId).put("source", getChangeSource(source))
//                .put("contentID", contentID).put("contentDuration", contentDuration)
//    }
//}
//
//fun Spider.findPageClickEvent(eventName: String, userId: Long?, Spide: String, targetID: String): EventCreator {
//    return if (userId == null) {
//        manuallyEvent(eventName).put("Spide", Spide).put("targetID", targetID)
//    } else {
//        manuallyEvent(eventName).put("userID", userId).put("Spide", Spide).put("targetID", targetID)
//    }
//}
//
///**
// * source 转换
// */
//fun getChangeSource(source: String): String {
//    var sour = ""
//    when (source) {
//        "homepage" -> sour = "HomePage"
//        "channel_page" -> sour = "ChannelList"
//        "explore" -> sour = "Explore"
//        "channel_scroll_list" -> sour = "Explore"
//        "fav_list" -> sour = "Fav"
//        "like_list" -> sour = "Like"
//        "feed_profile" -> sour = "Push"
//        "publish" -> sour = "Publish"
//    }
//    return sour
//}
//
///**
// * share 转换
// */
//fun getChangeShareWay(shareName: String): String {
//    var sour = "WeChat"
//    when (shareName) {
//        "wechat_session" -> sour = "WeChat"
//        "wechat_timeline" -> sour = "WeChatCircle"
//        "QQ" -> sour = "QQ"
//        "qzone" -> sour = "qqSpace"
//        "WEIXIN" -> sour = "WeChat"
//        "WEIXIN_CIRCLE" -> sour = "WeChatCircle"
//        "QZONE" -> sour = "qqSpace"
//    }
//    return sour
//}
//
///**
// * mediaType 转换
// */
//fun getChangeMediaType(commentType: String?): String {
//    var sour = ""
//    when (commentType) {
//        "LONG_IMAGE" -> sour = "img"
//        "ANIMATION" -> sour = "img"
//        "IMAGE" -> sour = "img"
//        "VIDEO" -> sour = "media"
//        "txt" -> sour = "txt"
//    }
//    return sour
//}
//
//fun Spider.likeEvent(context: Context, likeId: String, method: String, source: String) {
//    return manuallyEvent(SpiderEventNames.LIKE)
//            .put("likeID", likeId)
//            .put("userID", context.getUserId())
//            .put("method", method)
//            .put("source", source)
//            .track()
//}
//
//
//fun Spider.doubleTabUpFeedEvent(context: Context, feedId: String, isTriggerUpAction: Boolean, source: String) {
//    return manuallyEvent(SpiderEventNames.DOUBLE_LIKE_FEED)
//            .put("feedID", feedId)
//            .put("userID", context.getUserId())
//            .put("isTriggerUpAction", isTriggerUpAction)
//            .put("source", source)
//            .track()
//}
//
//fun Spider.shareClickEvent(context: Context, shareId: String?, source: String? = "") {
//    manuallyEvent(SpiderEventNames.SHARE_CLICK)
//            .put("shareID", shareId ?: "")
//            .put("userID", context.getUserId())
//            .put("source", source!!)
//            .track()
//}
//
//fun Spider.shareDetailsEvent(context: Context, shareId: String?, isSuccess: Boolean, shareWay: String) {
//    manuallyEvent(SpiderEventNames.SHARE_DETAILS)
//            .put("shareID", shareId ?: "")
//            .put("userID", context.getUserId())
//            .put("isSuccess", isSuccess)
//            .put("shareWay", shareWay)
//            .track()
//}
//
//fun Spider.shareChannelEvent(context: Context, shareId: String?, isSuccess: Boolean, shareWay: String) {
//    manuallyEvent(SpiderEventNames.CHANNEL_SHARE)
//            .put("shareID", shareId ?: "")
//            .put("userID", context.getUserId())
//            .put("isSuccess", isSuccess)
//            .put("shareWay", shareWay)
//            .track()
//}
//
//fun Spider.shareWebEvent(context: Context, url: String?, isSuccess: Boolean, shareWay: String) {
//    manuallyEvent(SpiderEventNames.WEB_PAGE_SHARE)
//            .put("url", url ?: "")
//            .put("isSuccess", isSuccess)
//            .put("shareWay", shareWay)
//            .track()
//}
//
//
//fun Spider.viewCommentsEvent(context: Context, feedId: String, source: String) {
//    manuallyEvent(SpiderEventNames.VIEW_COMMENTS)
//            .put("feedID", feedId)
//            .put("userID", context.getUserId())
//            .put("source", source)
//            .track()
//}
//
//fun Spider.shareApp(shareWay: String) {
//    manuallyEvent(SpiderEventNames.APP_SHARE)
//            .put("shareWay", getChangeShareWay(shareWay))
//            .track()
//}
//
///**
// * 分享 相关
// */
//fun Spider.shareCommentEvent(context: Context, feedID: String, commentType: String,
//                             commentID: Long, isSuccess: Boolean, shareWay: String) {
//    manuallyEvent(SpiderEventNames.COMMENT_SHARE)
//            .put("feedID", feedID)
//            .put("userID", context.getUserId())
//            .put("commentType", getChangeMediaType(commentType))
//            .put("commentID", commentID)
//            .put("isSuccess", isSuccess)
//            .put("shareWay", getChangeShareWay(shareWay))
//            .track()
//}
//
//fun Spider.commentLikeEvent(context: Context, feedId: String, commentId: String?, method: String) {
//    return manuallyEvent(SpiderEventNames.COMMENT_SUPPORT)
//            .put("feedID", feedId)
//            .put("commentID", commentId ?: "")
//            .put("userID", context.getUserId())
//            .put("method", method)
//            .track()
//}
//
//fun Spider.forceFeedGameClickEvent(context: Context, feedID: String, ssr: String) {
//    return manuallyEvent(SpiderEventNames.FORCE_FEED_GAME_CLICK)
//            .put("userID", context.getUserId())
//            .put("feedID", feedID)
//            .put("ssr", ssr)
//            .track()
//}
//
///***
// * 更多按钮点击事件
// */
//fun Spider.moreModeEvent(context: Context, feedID: String, ssr: String) {
//    return manuallyEvent(SpiderEventNames.MORE_PTION_CLICK)
//            .put("userID", context.getUserId())
//            .put("feedID", feedID)
//            .put("source", ssr)
//            .track()
//}
//
///***
// * 编辑修改button点击事件
// */
//fun Spider.editVideoEvent(context: Context, feedID: String, isSuccess: Boolean) {
//    return manuallyEvent(SpiderEventNames.FEED_EDIT_CLICK)
//            .put("userID", context.getUserId())
//            .put("feedID", feedID)
//            .put("isEnable", isSuccess)
//            .track()
//}
//
///**
// * 关注动态埋点事件
// */
//fun Spider.followTimeLineEvent(context: Context, followTimeLineEvent: String,
//                               duration: Long = 0L,
//                               feedID: String = "",
//                               channelID: String = "",
//                               targetUserID: String = "",
//                               type: String = "",
//                               source: String = "") {
//    val eventCreator = manuallyEvent(followTimeLineEvent)
//    eventCreator.put("userID", context.getUserId())
//    if (!feedID.isEmpty()) eventCreator.put("feedID", feedID)
//    if (duration != 0L) eventCreator.put("duration", duration)
//    if (!channelID.isEmpty()) eventCreator.put("channelID", channelID)
//    if (!targetUserID.isEmpty()) eventCreator.put("targetUserID", targetUserID)
//    if (!type.isEmpty()) eventCreator.put("type", type)
//    if (!source.isEmpty()) eventCreator.put("source", source)
//    return eventCreator.track()
//}
//
///**
// * 点击订阅频道事件
// */
//fun Spider.subscribeChinnalClickEvent(context: Context, shareId: String? = "", source: String? = "") {
//    manuallyEvent(SpiderEventNames.CHANNEL_SUBSCRIPTION)
//            .put("userID", context.getUserId())
//            .put("shareID", shareId!!)
//            .put("source", source!!)
//            .track()
//}
//
///**
// * 点击订阅频道事件
// */
//fun Spider.userFollowEvent(context: Context, targetUserID: String = "",
//                           source: String = "", isFollow: Boolean = false) {
//    manuallyEvent(SpiderEventNames.USER_FOLLOW)
//            .put("userID", context.getUserId())
//            .put("TargetUserID", targetUserID)
//            .put("source", source)
//            .put("method", if (isFollow) "follow" else "unfollow")
//            .track()
//}
//
//
///**
// * 优秀视频集视频曝光
// */
//fun Spider.exploreFeedLeaderBoardExposureEvent(feedID: String) {
//    manuallyEvent(SpiderEventNames.EXPLORE_FEED_LEADER_BOARD_EXPOSURE)
//            .put("feedID", feedID)
//            .track()
//}
//
///**
// * 分区入口点击
// */
//fun Spider.categoryDidSelectedEvent(categoryID: Int) {
//    manuallyEvent(SpiderEventNames.CATEGORY_DID_SELECTED)
//            .put("categoryID", categoryID)
//            .track()
//}
//
///**
// * 分区访问次数
// */
//fun Spider.categoryDetailExposureEvent(categoryID: String) {
//    manuallyEvent(SpiderEventNames.CATEGORY_DETAIL_EXPOSURE)
//            .put("categoryID", categoryID)
//            .track()
//}
//
///**
// * 	banner曝光
// */
//fun Spider.categoryBannerExposureEvent(categoryID: String, bannerId: String) {
//    manuallyEvent(SpiderEventNames.CATEGORY_BANNER_EXPOSURE)
//            .put("categoryID", categoryID)
//            .put("bannerID", bannerId)
//            .track()
//}
//
///**
// * 	banner点击
// */
//fun Spider.categoryBannerDidSelectedEvent(categoryID: String, bannerId: Int) {
//    manuallyEvent(SpiderEventNames.CATEGORY_BANNER_DID_SELECTED)
//            .put("categoryID", categoryID)
//            .put("bannerID", bannerId)
//            .track()
//}
//
///**
// * 	近期热门视频曝光
// */
//fun Spider.categoryFeedLeaderBoardExposureEvent(categoryID: String, feedID: String) {
//    manuallyEvent(SpiderEventNames.CATEGORY_FEED_LEADER_BOARD_EXPOSURE)
//            .put("categoryID", categoryID)
//            .put("feedID", feedID)
//            .track()
//}
//
///**
// * 	近期热门视频点击
// */
//fun Spider.categoryFeedLeaderboardDidSelectedEvent(categoryID: String, feedID: String) {
//    manuallyEvent(SpiderEventNames.CATEGORY_FEED_LEADER_BOARD_DID_SELECTED)
//            .put("categoryID", categoryID)
//            .put("feedID", feedID)
//            .track()
//}
//
///**
// * 	热门话题点击
// */
//fun Spider.categoryTopicsTagDidSelectedEvent(channelID: String) {
//    manuallyEvent(SpiderEventNames.CATEGORY_TOPICS_TAG_DID_SELECTED)
//            .put("channelID", channelID)
//            .track()
//}
//
///**
// * 	话题新动态视频点击
// */
//fun Spider.categoryTopicsFeedDidSelectedEvent(channelID: String, feedID: String) {
//    manuallyEvent(SpiderEventNames.CATEGORY_TOPICS_FEED_DID_SELECTED)
//            .put("channelID", channelID)
//            .put("feedID", feedID)
//            .track()
//}
//
///**
// * 	全部话题来源
// */
//fun Spider.allTopicsExposureEvent(source: String) {
//    manuallyEvent(SpiderEventNames.ALL_TOPICS_EXPOSURE)
//            .put("source", source)
//            .track()
//}
//
///**
// * 	个人页面的分区标签的点击
// */
//fun Spider.userProfileCategoryClickEvent(categoryId: Int) {
//    manuallyEvent(SpiderEventNames.USER_PROFILE_CATEGORY_CLICK)
//            .put("categoryID", categoryId)
//            .track()
//}
//
///**
// * 	分区的明星榜的点击事件
// */
//fun Spider.channelUpUserStarListEntranceClickEvent(categoryId: Int) {
//    manuallyEvent(SpiderEventNames.CHANNEL_UP_USER_STAR_LIST_ENTRANCE_CLICK)
//            .put("categoryID", categoryId)
//            .track()
//}
//
//fun Spider.userChannelUpUserStarListExposureEvent(context:Context,channelId:String?,categoryId:String?){
//    val eventCreator = manuallyEvent(SpiderEventNames.CHANNEL_UP_STAR_EXPOSURE)
//            .put("userID", context.getUserId())
//    channelId?.let {
//        eventCreator.put("channelId",it)
//    }
//    categoryId?.let {
//        eventCreator.put("categoryId",it)
//    }
//    eventCreator.track()
//}
