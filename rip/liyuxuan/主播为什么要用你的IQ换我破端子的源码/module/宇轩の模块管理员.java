package rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.module;

import java.util.ArrayList;
import java.util.List;

import rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.module.implement.combat.释放剑气;
import rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.module.implement.movement.宇轩腿脚不好;
import rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.module.implement.movement.宇轩自动跑动;
import rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.module.implement.visual.宇轩的头部显示;

public class 宇轩の模块管理员 {

    public static final List<宇轩の模块> 宇轩の模块们 = new ArrayList<>();

    public 宇轩の模块管理员() {
        宇轩の模块们.add(new 宇轩自动跑动());
        宇轩の模块们.add(new 宇轩的头部显示());
        宇轩の模块们.add(new 宇轩腿脚不好());
        宇轩の模块们.add(new 释放剑气());
    }

    public static List<宇轩の模块> 获取宇轩的模块们 () {
        return 宇轩の模块们;
    }

    public static void 按键事件(int k) {
        for (宇轩の模块 m : 宇轩の模块们) {
            if (m.获取按键() == k) m.开关();
        }
    }
}
