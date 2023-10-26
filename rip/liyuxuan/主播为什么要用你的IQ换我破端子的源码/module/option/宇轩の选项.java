package rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.module.option;

import java.util.function.Supplier;

public class 宇轩の选项<宇轩> {

    private final String 名字, 介绍;

    private 宇轩 设置;

    private final Supplier<Boolean> 显示;

    public 宇轩の选项(String 名字, String 介绍, 宇轩 设置, Supplier<Boolean> 显示) {
        this.名字 = 名字;
        this.介绍 = 介绍;
        this.设置 = 设置;
        this.显示 = 显示;
    }

    public String 获取名字() {
        return 名字;
    }

    public String 获取简单の名字() {
        return 名字.replace(" ", "");
    }

    public String 获取介绍() {
        return 介绍;
    }

    public 宇轩 获取设置() {
        return 设置;
    }

    public boolean 显示宇轩的鸡巴吗() {
        return 显示.get();
    }

    public void 进行一个宇轩设置(宇轩 设置) {
        this.设置 = 设置;

        // TODO 保存文件
    }
}
