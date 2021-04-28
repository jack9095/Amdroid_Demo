package com.kuanquan.test.enum_p

enum class MediaType {

    TYPE_SYNCHRONIZATION,  // 同步
    TYPE_EXIT_PROJECTION,  // 退出投屏

    TYPE_MAIN,        // 主态，创建房间
    TYPE_PASSENGER,   // 客态  进来房间一起观影

    TYPE_PORTRAIT,    // 竖屏
    TYPE_LANDSCAPE,   // 横屏


    TYPE_UNKNOWN,   // 从其他APP 投屏过来的未知类型
    TYPE_VIDEO      // 从其他APP 投屏过来的视屏类型
}