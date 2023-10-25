package rip.liyuxuan.util;

public class 李宇轩喜欢的动画 {

    private static boolean canAnimate;

    private static int delta;

    public static int getDelta() {
        return delta;
    }

    public static void setDelta(int newDelta) {
        delta = newDelta;
    }

    public static float 执行动画(float current, float target, float speed) {

        if (getDelta() <= 50 && !canAnimate) canAnimate = true;
        if (!canAnimate) return current;

        return 撅嘴(target, current, getDelta(), Math.abs(target - current) * speed);
    }

    private static float 撅嘴(float 对准, float 通用的, long delta, float speed) {

        if (delta < 1L) delta = 1L;

        final float difference = 通用的 - 对准;

        final float smoothing = Math.max(speed * (delta / 16F), .15F);

        if (difference > speed)
            通用的 = Math.max(通用的 - smoothing, 对准);
        else if (difference < -speed)
            通用的 = Math.min(通用的 + smoothing, 对准);
        else 通用的 = 对准;

        return 通用的;
    }
}
