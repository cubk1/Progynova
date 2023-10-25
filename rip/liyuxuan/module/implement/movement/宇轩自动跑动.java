package rip.liyuxuan.module.implement.movement;

import org.lwjgl.input.Keyboard;
import rip.liyuxuan.event.implement.宇轩の玩家更新事件;
import rip.liyuxuan.event.李宇轩1337;
import rip.liyuxuan.module.option.implement.宇轩の布尔;
import rip.liyuxuan.module.宇轩の模块;
import rip.liyuxuan.module.宇轩の模块种类;
import rip.liyuxuan.util.玩家的功能;

@SuppressWarnings("unused")
public class 宇轩自动跑动 extends 宇轩の模块 {

    private final 宇轩の布尔 全方向 = new 宇轩の布尔("全方向", "全方向疾跑", 真);

    public 宇轩自动跑动() {
        super("宇轩自动跑动", "自动帮李宇轩跑步, 快打开试试吧", 宇轩の模块种类.移动);
        设置按键(Keyboard.KEY_I);
        添加宇轩の选项(全方向);
    }

    @李宇轩1337
    public void 宇轩玩家更新(宇轩の玩家更新事件 事件) {
        if (玩家的功能.在游戏中() && 进行一个宇轩跑动()) 我D世界.宇轩游玩者.设置宇轩的疾跑状态(真);
    }

    public boolean 进行一个宇轩跑动() {
        return (全方向.获取设置() ? 玩家的功能.运动中() : 我D世界.宇轩游玩者.移动输入.向前移动 > 0) && !我D世界.宇轩游玩者.正在下蹲() && (我D世界.宇轩游玩者.获取饥饿值().获取饥饿等级() > 6 || 我D世界.玩家控制者.是创造模式吗());
    }

    @Override
    public boolean 是否为高雅的李宇轩功能() {
        return 真;
    }
}
