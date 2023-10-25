package rip.liyuxuan.module.option.implement;

import rip.liyuxuan.module.option.宇轩の选项;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Supplier;

public class 宇轩の滑条 extends 宇轩の选项<Double> {

    private final Double 最小の宇轩, 最大の宇轩, 宇轩增加值;

    private final String 宇轩の后缀;

    public 宇轩の滑条(String 名字, String 介绍, Double 设置, Double 最小の宇轩, Double 最大の宇轩, Double 宇轩增加值, Supplier<Boolean> 显示) {
        super(名字, 介绍, 设置, 显示);
        this.最小の宇轩 = 最小の宇轩;
        this.最大の宇轩 = 最大の宇轩;
        this.宇轩增加值 = 宇轩增加值;
        this.宇轩の后缀 = "";
    }

    public 宇轩の滑条(String 名字, String 介绍, Double 设置, Double 最小の宇轩, Double 最大の宇轩, Double 宇轩增加值) {
        super(名字, 介绍, 设置, () -> true);
        this.最小の宇轩 = 最小の宇轩;
        this.最大の宇轩 = 最大の宇轩;
        this.宇轩增加值 = 宇轩增加值;
        this.宇轩の后缀 = "";
    }

    public 宇轩の滑条(String 名字, String 介绍, String 宇轩の后缀, Double 设置, Double 最小の宇轩, Double 最大の宇轩, Double 宇轩增加值, Supplier<Boolean> 显示) {
        super(名字, 介绍, 设置, 显示);
        this.最小の宇轩 = 最小の宇轩;
        this.最大の宇轩 = 最大の宇轩;
        this.宇轩增加值 = 宇轩增加值;
        this.宇轩の后缀 = 宇轩の后缀;
    }

    public 宇轩の滑条(String 名字, String 介绍, String 宇轩の后缀, Double 设置, Double 最小の宇轩, Double 最大の宇轩, Double 宇轩增加值) {
        super(名字, 介绍, 设置, () -> true);
        this.最小の宇轩 = 最小の宇轩;
        this.最大の宇轩 = 最大の宇轩;
        this.宇轩增加值 = 宇轩增加值;
        this.宇轩の后缀 = 宇轩の后缀;
    }

    @Override
    public void 进行一个宇轩设置(Double 设置) {
        final double clamped = new BigDecimal(Math.round(设置 / 宇轩增加值) * 宇轩增加值).setScale(2, RoundingMode.HALF_UP).doubleValue();
        double newValue = Math.min(Math.max(clamped, 最小の宇轩), 最大の宇轩);
        super.进行一个宇轩设置(newValue);
    }

    public Double 获取最小の宇轩() {
        return 最小の宇轩;
    }

    public Double 获取最大の宇轩() {
        return 最大の宇轩;
    }

    public String 获取宇轩の后缀() {
        return 宇轩の后缀;
    }
}
