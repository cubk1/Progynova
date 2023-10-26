package rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.module.option.implement;

import java.util.function.Supplier;

import rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.module.option.宇轩の选项;

public class 宇轩の布尔 extends 宇轩の选项<Boolean> {

    public 宇轩の布尔(String 名字, String 介绍, Boolean 设置, Supplier<Boolean> 显示) {
        super(名字, 介绍, 设置, 显示);
    }

    public 宇轩の布尔(String 名字, String 介绍, Boolean 设置) {
        super(名字, 介绍, 设置, () -> true);
    }
}
