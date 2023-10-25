package rip.liyuxuan.event.implement;

import net.minecraft.client.gui.比例解析;
import rip.liyuxuan.event.misc.宇轩の事件;

public class 宇轩の渲染2D事件 extends 宇轩の事件 {

    private final 比例解析 比例解析;

    private final float renderPartialTicks;

    public 宇轩の渲染2D事件(比例解析 比例解析, float renderPartialTicks) {
        this.比例解析 = 比例解析;
        this.renderPartialTicks = renderPartialTicks;
    }

    public 比例解析 getScaledResolution() {
        return 比例解析;
    }

    public float getRenderPartialTicks() {
        return renderPartialTicks;
    }
}
