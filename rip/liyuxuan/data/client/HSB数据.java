package rip.liyuxuan.data.client;

import java.awt.*;

public class HSB数据 {

    private float hue = 0, 饱和度 = 1, 亮度 = 1, alpha = 1;

    public HSB数据(float hue, float 饱和度, float 亮度, float alpha) {
        this.hue = hue;
        this.饱和度 = 饱和度;
        this.亮度 = 亮度;
        this.alpha = alpha;
    }

    public HSB数据(Color color) {

        final float[] hsbColor = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

        this.hue = hsbColor[0];
        this.饱和度 = hsbColor[1];
        this.亮度 = hsbColor[2];
    }

    public Color getAsColor() {
        final Color beforeReAlpha = Color.getHSBColor(hue, 饱和度, 亮度);
        return new Color(beforeReAlpha.getRed(), beforeReAlpha.getGreen(), beforeReAlpha.getBlue(), Math.round(255 * alpha));
    }

    public float getHue() {
        return hue;
    }

    public void setHue(float hue) {
        this.hue = hue;
    }

    public float get饱和度() {
        return 饱和度;
    }

    public void set饱和度(float 饱和度) {
        this.饱和度 = 饱和度;
    }

    public float get亮度() {
        return 亮度;
    }

    public void set亮度(float 亮度) {
        this.亮度 = 亮度;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}
