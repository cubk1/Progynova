package rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.module.option.implement;

import java.util.List;
import java.util.function.Supplier;

import rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.module.option.宇轩の选项;

public class 宇轩の大连击 extends 宇轩の选项<List<宇轩の布尔>> {

    private final List<宇轩の布尔> 宇轩の布尔们;

    public 宇轩の大连击(String 名字, String 介绍, List<宇轩の布尔> 设置, Supplier<Boolean> 显示) {
        super(名字, 介绍, 设置, 显示);
        this.宇轩の布尔们 = 设置;
    }

    public 宇轩の大连击(String 名字, String 介绍, List<宇轩の布尔> 设置) {
        super(名字, 介绍, 设置, () -> true);
        this.宇轩の布尔们 = 设置;
    }
}
