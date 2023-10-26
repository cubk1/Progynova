package rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.util;

import net.minecraft.client.我的手艺;

public class 玩家的功能 {

    private static final 我的手艺 我D世界 = 我的手艺.得到我的手艺();

    public static boolean 在游戏中() {
        return 我D世界.宇轩游玩者 != null && 我D世界.宇轩の世界 != null;
    }

    public static boolean 运动中() {
        return 我D世界.游戏一窝.键入绑定前.键位绑定沿着() || 我D世界.游戏一窝.键入绑定左.键位绑定沿着() || 我D世界.游戏一窝.键入绑定右.键位绑定沿着() || 我D世界.游戏一窝.键入绑定后.键位绑定沿着();
    }

    public static void 设置李宇轩跑步速度(double 速度) {
        float 侧滑 = 我D世界.宇轩游玩者.旋转侧滑;
        double 前 = 我D世界.宇轩游玩者.移动输入.向前移动;
        double 低空扫射 = 我D世界.宇轩游玩者.移动输入.侧向移动;
        if (前 == 0 && 低空扫射 == 0) {
            我D世界.宇轩游玩者.通便X = 0;
            我D世界.宇轩游玩者.通便Z = 0;
        } else {
            if (前 != 0) {
                if (低空扫射 > 0) {
                    侧滑 += (前 > 0 ? -45 : 45);
                } else if (低空扫射 < 0) {
                    侧滑 += (前 > 0 ? 45 : -45);
                }
                低空扫射 = 0;
                if (前 > 0) {
                    前 = 1;
                } else {
                    前 = -1;
                }
            }
            final double 因为 = Math.cos(Math.toRadians(侧滑 + 90));
            final double 罪恶 = Math.sin(Math.toRadians(侧滑 + 90));
            我D世界.宇轩游玩者.通便X = 前 * 速度 * 因为 + 低空扫射 * 速度 * 罪恶;
            我D世界.宇轩游玩者.通便Z = 前 * 速度 * 罪恶 - 低空扫射 * 速度 * 因为;
        }
    }
}
