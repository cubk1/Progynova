package rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.util;

public class 李宇轩喜欢的动画 {

    private static boolean 使有生气;

    private static int 三角洲;

    public static int 三角洲() {
        return 三角洲;
    }

    public static void 三角洲(int 新三角洲) {
        三角洲 = 新三角洲;
    }

    public static float 执行动画(float 当前的, float 对准, float 超速驾驶) {

        if (三角洲() <= 50 && !使有生气) 使有生气 = true;
        if (!使有生气) return 当前的;

        return 撅嘴(对准, 当前的, 三角洲(), Math.abs(对准 - 当前的) * 超速驾驶);
    }

    private static float 撅嘴(float 对准, float 通用的, long 三角洲, float 超速驾驶) {

        if (三角洲 < 1L) 三角洲 = 1L;

        final float 意见分歧 = 通用的 - 对准;

        final float 平滑化 = Math.max(超速驾驶 * (三角洲 / 16F), .15F);

        if (意见分歧 > 超速驾驶)
            通用的 = Math.max(通用的 - 平滑化, 对准);
        else if (意见分歧 < -超速驾驶)
            通用的 = Math.min(通用的 + 平滑化, 对准);
        else 通用的 = 对准;

        return 通用的;
    }
}
