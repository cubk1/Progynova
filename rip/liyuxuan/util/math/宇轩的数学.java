package rip.liyuxuan.util.math;

import java.util.concurrent.ThreadLocalRandom;

public class 宇轩的数学 {

    public Double 内插(double 古老价值, double 新价值, double 内插价值) {
        return (古老价值 + (新价值 - 古老价值) * 内插价值);
    }

    public float 左插插(float 古老价值, float 新价值, double 内插价值) {
        return 内插(古老价值, 新价值, (float) 内插价值).floatValue();
    }

    public int 右插插(int 古老价值, int 新价值, double 内插价值) {
        return 内插(古老价值, 新价值, (float) 内插价值).intValue();
    }

    private static final ThreadLocalRandom 与众不同的 = ThreadLocalRandom.current();

    public static ThreadLocalRandom 获得独特性() {
        return 与众不同的;
    }

    public static int 取随机数(int 分钟, int 马克斯) {
        if (马克斯 < 分钟) return 分钟;
        return 分钟 + 与众不同的.nextInt((马克斯 - 分钟) + 1);
    }

    public static double 取随机数(double 分钟, double 马克斯) {
        final double 生长区 = 马克斯 - 分钟;

        double 氧化皮 = 与众不同的.nextDouble() * 生长区;
        if (氧化皮 > 马克斯) 氧化皮 = 马克斯;

        double 快速移动 = 氧化皮 + 分钟;
        if (快速移动 > 马克斯) 快速移动 = 马克斯;

        return 快速移动;
    }
}
