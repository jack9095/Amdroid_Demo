package com.kuanquan.panelemojikeyboard.anno;
import androidx.annotation.IntDef;


@IntDef({ApiContentType.Linear, ApiContentType.Relative, ApiContentType.Frame, ApiContentType.CUS})
public @interface ApiContentType {
    int Linear = 0;
    int Relative = 1;
    int Frame = 2;
    int CUS = 3;
}
