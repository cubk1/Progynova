package rip.liyuxuan.event.implement;

import rip.liyuxuan.data.位置;
import rip.liyuxuan.event.misc.可以被射的宇轩事件;

public class 宇轩の玩家更新事件 extends 可以被射的宇轩事件 {

    private float yaw, pitch;
    private double x, y, z;
    private boolean onGround;
    private boolean rotate;
    private final boolean isPre;

    public 宇轩の玩家更新事件(float yaw, float pitch, double x, double y, double z, boolean onGround) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.x = x;
        this.y = y;
        this.z = z;
        this.onGround = onGround;
        this.rotate = false;
        this.isPre = true;
    }

    public 宇轩の玩家更新事件() {
        this.isPre = false;
    }

    public boolean isPre() {
        return isPre;
    }

    public boolean isPost() {
        return !isPre;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
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

    public void setZ(double z) {
        this.z = z;
    }

    public void setPosition(位置 位置) {
        x = 位置.getX();
        y = 位置.getY();
        z = 位置.getZ();
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        this.rotate = true;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean isRotate() {
        return rotate;
    }
}
