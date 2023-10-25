package rip.liyuxuan.event.implement;

import rip.liyuxuan.event.misc.宇轩の事件;

public class 宇轩の移动事件 extends 宇轩の事件 {

    private double x, y, z;

    public 宇轩の移动事件(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public void 设置插(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void 设置炸(double z) {
        this.z = z;
    }
}
