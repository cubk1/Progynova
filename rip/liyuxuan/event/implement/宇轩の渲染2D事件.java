package rip.liyuxuan.event.implement;

import net.minecraft.client.gui.ScaledResolution;
import rip.liyuxuan.event.misc.宇轩の事件;

public class 宇轩の渲染2D事件 extends 宇轩の事件 {

    private final ScaledResolution scaledResolution;

    private final float renderPartialTicks;

    public 宇轩の渲染2D事件(ScaledResolution scaledResolution, float renderPartialTicks) {
        this.scaledResolution = scaledResolution;
        this.renderPartialTicks = renderPartialTicks;
    }

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

    public float getRenderPartialTicks() {
        return renderPartialTicks;
    }
}
