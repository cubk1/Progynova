package rip.liyuxuan.module;

import java.awt.Color;

public enum 宇轩の模块种类 {
    战斗(new Color(91,205,250)),
    移动(new Color(246,168,192)),
    玩家(new Color(-1)),
    视觉(new Color(0,196,255)),
    其他(new Color(255,125,193));

    private final Color 宇轩颜色;

    宇轩の模块种类(Color 宇轩颜色){
        this.宇轩颜色 = 宇轩颜色;
    }

    public Color 获取宇轩颜色 () {
        return 宇轩颜色;
    }
}
