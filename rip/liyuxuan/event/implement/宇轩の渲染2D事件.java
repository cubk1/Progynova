package rip.liyuxuan.event.implement;

import net.minecraft.client.gui.比例解析;
import rip.liyuxuan.event.misc.宇轩の事件;

public class 宇轩の渲染2D事件 extends 宇轩の事件 {

    private final 比例解析 比例解析;

    private final float 渲染部分刻度;

    public 宇轩の渲染2D事件(比例解析 比例解析, float 渲染部分刻度) {
        this.比例解析 = 比例解析;
        this.渲染部分刻度 = 渲染部分刻度;
    }

    public 比例解析 得到比例解析() {
        return 比例解析;
    }

    public float 得到渲染部分刻度() {
        return 渲染部分刻度;
    }
}
