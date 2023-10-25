package rip.liyuxuan.event.implement;

import rip.liyuxuan.event.misc.宇轩の事件;

public class 宇轩の移动事件 extends 宇轩の事件 {

    private double 插, 歪, 贼;

    public 宇轩の移动事件(double 插, double 歪, double 贼) {
        this.插 = 插;
        this.歪 = 歪;
        this.贼 = 贼;
    }

    public double 得到插() {
        return 插;
    }

    public void 设置插(double x) {
        this.插 = x;
    }

    public double 得到歪() {
        return 歪;
    }

    public void 设置歪(double 歪) {
        this.歪 = 歪;
    }

    public double 得到贼() {
        return 贼;
    }

    public void 设置炸(double z) {
        this.贼 = z;
    }
}
