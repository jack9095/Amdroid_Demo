package com.kuanquan.annotationandeventbusdemo.eventbus;

public enum ThreadMode {
    MAIN, // 主 -> 主，子 -> 主
    BACKGROUND, // 主 -> 子，子 -> 子
    POSTING; // 主 -> 主，子 -> 子
}
