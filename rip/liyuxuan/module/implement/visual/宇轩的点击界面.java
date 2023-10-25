package rip.liyuxuan.module.implement.visual;

import rip.liyuxuan.module.宇轩の模块;
import rip.liyuxuan.module.宇轩の模块种类;

public class 宇轩的点击界面 extends 宇轩の模块 {

    public 宇轩的点击界面() {
        super("宇轩的点击界面", "打开宇轩的点击界面", 宇轩の模块种类.视觉);
    }

    @Override
    public void 开启时() {
        super.开启时();
        开关();
    }

    @Override
    public boolean 是否为高雅的李宇轩功能() {
        return 假;
    }
}
