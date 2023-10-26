package rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.module.implement.combat;

import org.lwjgl.input.Keyboard;

import rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.module.宇轩の模块;
import rip.liyuxuan.主播为什么要用你的IQ换我破端子的源码.module.宇轩の模块种类;

@SuppressWarnings("unused")
public class 释放剑气 extends 宇轩の模块 {

    public 释放剑气 () {
        super("释放剑气", "马上就要迎来2022年了，2022年会是什么样呢" ,宇轩の模块种类.战斗);
        设置按键(Keyboard.KEY_Q);
    }

    @Override
    public void 开启时 () {
        for (int 宇轩 = 0; 宇轩 < 36; ++宇轩) {
            不要了(宇轩);
        }
        开关();
        super.开启时();
    }

    public void 不要了(int 格) {
        我D世界.玩家控制者.视窗点击(0, 格, 1, 4, 我D世界.宇轩游玩者);
    }

    @Override
    public boolean 是否为高雅的李宇轩功能() {
        return 真;
    }
}
