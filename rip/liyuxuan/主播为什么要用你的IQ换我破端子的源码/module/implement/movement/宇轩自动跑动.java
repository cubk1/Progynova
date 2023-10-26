package rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.module.implement.movement;

import org.lwjgl.input.Keyboard;

import rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.module.宇轩の模块;
import rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.module.宇轩の模块种类;
import rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.module.option.implement.宇轩の布尔;
import rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.util.玩家的功能;

@SuppressWarnings("unused")
public class 宇轩自动跑动 extends 宇轩の模块 {

    public static 宇轩の布尔 全方向 = new 宇轩の布尔("全方向", "全方向疾跑", 真);

    public 宇轩自动跑动() {
        super("宇轩自动跑动", "自动帮李宇轩跑步, 快打开试试吧", 宇轩の模块种类.移动);
        设置按键(Keyboard.KEY_I);
        添加宇轩の选项(全方向);
    }

    public static void 宇轩玩家更新(boolean pre) {
        if (玩家的功能.在游戏中() && 进行一个宇轩跑动()) 我D世界.宇轩游玩者.设置宇轩的疾跑状态(真);
    }

    public static boolean 进行一个宇轩跑动() {
        return (全方向.获取设置() ? 玩家的功能.运动中() : 我D世界.宇轩游玩者.移动输入.向前移动 > 0) && !我D世界.宇轩游玩者.正在下蹲() && (我D世界.宇轩游玩者.获取饥饿值().获取饥饿等级() > 6 || 我D世界.玩家控制者.是创造模式吗());
    }

    @Override
    public boolean 是否为高雅的李宇轩功能() {
        return 真;
    }
}
