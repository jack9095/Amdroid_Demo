package com.kuanquan.panelemojikeyboard.anno;


import androidx.annotation.IntDef;


@IntDef({ChatPageType.DEFAULT, ChatPageType.TITLE_BAR, ChatPageType.COLOR_STATUS_BAR, ChatPageType.CUS_TITLE_BAR, ChatPageType.TRANSPARENT_STATUS_BAR, ChatPageType.TRANSPARENT_STATUS_BAR_DRAW_UNDER})
public @interface ChatPageType {
    int DEFAULT = 0;
    int TITLE_BAR = 1;
    int CUS_TITLE_BAR = 2;
    int COLOR_STATUS_BAR = 3;
    int TRANSPARENT_STATUS_BAR = 4;
    int TRANSPARENT_STATUS_BAR_DRAW_UNDER = 5;
}
