package rip.liyuxuan.data.client;

import java.awt.*;

public class HSB数据 {

    private float 色调 = 0, 饱和度 = 1, 亮度 = 1, 阿尔法 = 1;

    public HSB数据(float 色调, float 饱和度, float 亮度, float 阿尔法) {
        this.色调 = 色调;
        this.饱和度 = 饱和度;
        this.亮度 = 亮度;
        this.阿尔法 = 阿尔法;
    }

    public HSB数据(Color 颜色) {

        final float[] hsb颜色 = Color.RGBtoHSB(颜色.getRed(), 颜色.getGreen(), 颜色.getBlue(), null);

        this.色调 = hsb颜色[0];
        this.饱和度 = hsb颜色[1];
        this.亮度 = hsb颜色[2];
    }

    public Color 得到颜色() {
        final Color 颜色 = Color.getHSBColor(色调, 饱和度, 亮度);
        return new Color(颜色.getRed(), 颜色.getGreen(), 颜色.getBlue(), Math.round(255 * 阿尔法));
    }

    public float 得到色调() {
        return 色调;
    }

    public void 设置色调(float 色调) {
        this.色调 = 色调;
    }

    public float 得到饱和度() {
        return 饱和度;
    }

    public void 设置包和富(float 饱和度) {
        this.饱和度 = 饱和度;
    }

    public float 得到亮度() {
        return 亮度;
    }

    public void 设置亮度(float 亮度) {
        this.亮度 = 亮度;
    }

    public float 得到阿尔法() {
        return 阿尔法;
    }

    public void 设置阿尔法(float 阿尔法) {
        this.阿尔法 = 阿尔法;
    }
}
