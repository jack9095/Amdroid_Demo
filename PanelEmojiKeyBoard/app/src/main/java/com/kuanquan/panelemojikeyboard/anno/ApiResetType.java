package com.kuanquan.panelemojikeyboard.anno;


import androidx.annotation.IntDef;

@IntDef({ApiResetType.DISABLE, ApiResetType.ENABLE, ApiResetType.ENABLE_EmptyView, ApiResetType.ENABLE_RecyclerView, ApiResetType.ENABLE_HookActionUpRecyclerview})
public @interface ApiResetType {
    int DISABLE = 0;
    int ENABLE = 1;
    int ENABLE_EmptyView = 11;
    int ENABLE_RecyclerView = 12;
    int ENABLE_HookActionUpRecyclerview = 13;
}
