package rip.liyuxuan.module.option.implement;

import rip.liyuxuan.module.option.宇轩の选项;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

public class 宇轩の小连击 extends 宇轩の选项<String> {

    private final String[] 设置;

    public 宇轩の小连击(String 名字, String 介绍, Supplier<Boolean> 显示, String... 设置) {
        super(名字, 介绍, 设置[0], 显示);
        this.设置 = 设置;
    }

    public 宇轩の小连击(String 名字, String 介绍, String... 设置) {
        super(名字, 介绍, 设置[0], () -> true);
        this.设置 = 设置;
    }

    public ArrayList<String> 用ArrayList获取设置() {
        return new ArrayList<>(Arrays.asList(设置));
    }

    public boolean 这合理吗(String name) {
        for (String val : 设置) if (val.equalsIgnoreCase(name)) return true;
        return false;
    }

    public boolean 是这个吗(String value) {
        return 获取设置().equalsIgnoreCase(value);
    }

    @Override
    public void 进行一个宇轩设置(String 设置) {
        if (这合理吗(设置)) for (String 模式 : this.设置) if (模式.equalsIgnoreCase(设置)) super.进行一个宇轩设置(模式);
    }
}
