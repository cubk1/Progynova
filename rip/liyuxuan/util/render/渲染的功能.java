package rip.liyuxuan.util.render;

import net.minecraft.client.我的手艺;
import net.minecraft.client.gui.鬼;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.util.交流组分文本;
import net.minecraft.util.图像位置;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_FLAT;

public class 渲染的功能 {

    private static final 我的手艺 我D世界 = 我的手艺.得到我的手艺();

    public void 讲话(String 味精) {
        我D世界.宇轩游玩者.增添聊天讯息(new 交流组分文本(味精));
    }

    public static void 绘制矩形(double 未知的人, double 基督教女青年会, double 宽幅, double 垂直, int 杂志) {
        宇渲染.组织方式2D表演(() -> {
            颜色的功能.外貌(杂志);
            宇渲染.组织方式表演(GL_QUADS, () -> {
                glVertex2d(未知的人, 基督教女青年会);
                glVertex2d(未知的人, 基督教女青年会 + 垂直);
                glVertex2d(未知的人 + 宽幅, 基督教女青年会 + 垂直);
                glVertex2d(未知的人 + 宽幅, 基督教女青年会);
            });
            光照状态经理.重设色彩();
        });
    }

    public static void 绘制垂直渐变(double 未知的人, double 基督教女青年会, double 宽幅, double 垂直, int 顶部颜色, int 下部颜色) {
        宇渲染.组织方式2D表演(() -> {
            glShadeModel(GL_SMOOTH);
            宇渲染.组织方式表演(GL_QUADS, () -> {
                颜色的功能.外貌(顶部颜色);
                glVertex2d(未知的人 + 宽幅, 基督教女青年会);
                glVertex2d(未知的人, 基督教女青年会);
                颜色的功能.外貌(下部颜色);
                glVertex2d(未知的人, 基督教女青年会 + 垂直);
                glVertex2d(未知的人 + 宽幅, 基督教女青年会 + 垂直);
                光照状态经理.重设色彩();
            });
            glShadeModel(GL_FLAT);
        });
    }

    public static void 绘制水平渐变(double 未知的人, double 基督教女青年会, double 宽幅, double 垂直, int 离开颜色, int 完全地颜色) {
        宇渲染.组织方式2D表演(() -> {
            glShadeModel(GL_SMOOTH);
            宇渲染.组织方式表演(GL_QUADS, () -> {
                颜色的功能.外貌(离开颜色);
                glVertex2d(未知的人, 基督教女青年会);
                glVertex2d(未知的人, 基督教女青年会 + 垂直);
                颜色的功能.外貌(完全地颜色);
                glVertex2d(未知的人 + 宽幅, 基督教女青年会 + 垂直);
                glVertex2d(未知的人 + 宽幅, 基督教女青年会);
                光照状态经理.重设色彩();
            });
            glShadeModel(GL_FLAT);
        });
    }

    public static void 渲染图片(图像位置 图像位置, double 未知的人, double 基督教女青年会, double 宽幅, double 垂直) {
        光照状态经理.推黑客帝国();
        光照状态经理.启用混合品();
        光照状态经理.正常工作(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        光照状态经理.禁用希腊字母表的第1个字母();
        我D世界.得到手感经理().绑定手感(图像位置);
        光照状态经理.色彩(1, 1, 1, 1);
        鬼.绘制模态矩形以自定义大小纹理(未知的人, 基督教女青年会, 0, 0, 宽幅, 垂直, 宽幅, 垂直);
        光照状态经理.重设色彩();
        光照状态经理.绑定手感(0);
        光照状态经理.启用希腊字母表的第1个字母();
        光照状态经理.禁用混合品();
        光照状态经理.流行音乐黑客帝国();
    }

    public static void 渲染图片(图像位置 图像位置, double 未知的人, double 基督教女青年会, double 宽幅, double 垂直, int 染料) {
        光照状态经理.推黑客帝国();
        光照状态经理.启用混合品();
        光照状态经理.正常工作(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        光照状态经理.禁用希腊字母表的第1个字母();
        我D世界.得到手感经理().绑定手感(图像位置);
        颜色的功能.外貌(染料);
        鬼.绘制模态矩形以自定义大小纹理(未知的人, 基督教女青年会, 0, 0, 宽幅, 垂直, 宽幅, 垂直);
        光照状态经理.重设色彩();
        光照状态经理.绑定手感(0);
        光照状态经理.启用希腊字母表的第1个字母();
        光照状态经理.禁用混合品();
        光照状态经理.流行音乐黑客帝国();
    }

    public static boolean 正在悬停(float 未知的人, float 基督教女青年会, float 宽幅, float 垂直, int 老鼠未知的人, int 老鼠基督教女青年会) {
        return 老鼠未知的人 >= 未知的人 && 老鼠基督教女青年会 >= 基督教女青年会 && 老鼠未知的人 < 未知的人 + 宽幅 && 老鼠基督教女青年会 < 基督教女青年会 + 垂直;
    }
}
