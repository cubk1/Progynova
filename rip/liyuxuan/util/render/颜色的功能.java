package rip.liyuxuan.util.render;

import net.minecraft.client.renderer.光照状态经理;

import java.awt.*;

public class 颜色的功能 {

    public static Color 重新透明度(Color 色彩, int 阿尔法) {
        return new Color(色彩.getRed(), 色彩.getGreen(), 色彩.getBlue(), 阿尔法);
    }

    public static void 外貌(Color 色彩) {
        final float 红的 = 色彩.getRed() / 255F;
        final float 绿的 = 色彩.getGreen() / 255F;
        final float 蓝的 = 色彩.getBlue() / 255F;
        final float 阿尔法 = 色彩.getAlpha() / 255F;

        光照状态经理.色彩(红的, 绿的, 蓝的, 阿尔法);
    }

    public static void 外貌(int 色彩) {

        final float 红的 = (float) (色彩 >> 16 & 255) / 255F;
        final float 绿的 = (float) (色彩 >> 8 & 255) / 255F;
        final float 蓝的 = (float) (色彩 & 255) / 255F;
        final float 阿尔法 = (float) (色彩 >> 24 & 255) / 255F;

        光照状态经理.色彩(红的, 绿的, 蓝的, 阿尔法);
    }
}
