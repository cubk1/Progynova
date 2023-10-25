package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.entity.RenderPlayer;

public class LayerDeadmau5Head implements LayerRenderer<AbstractClientPlayer>
{
    private final RenderPlayer playerRenderer;

    public LayerDeadmau5Head(RenderPlayer playerRendererIn)
    {
        this.playerRenderer = playerRendererIn;
    }

    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        if (entitylivingbaseIn.getName().equals("deadmau5") && entitylivingbaseIn.hasSkin() && !entitylivingbaseIn.isInvisible())
        {
            this.playerRenderer.bindTexture(entitylivingbaseIn.getLocationSkin());

            for (int i = 0; i < 2; ++i)
            {
                float f = entitylivingbaseIn.prevRotationYaw + (entitylivingbaseIn.旋转侧滑 - entitylivingbaseIn.prevRotationYaw) * partialTicks - (entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks);
                float f1 = entitylivingbaseIn.prevRotationPitch + (entitylivingbaseIn.rotationPitch - entitylivingbaseIn.prevRotationPitch) * partialTicks;
                光照状态经理.推黑客帝国();
                光照状态经理.辐射(f, 0.0F, 1.0F, 0.0F);
                光照状态经理.辐射(f1, 1.0F, 0.0F, 0.0F);
                光照状态经理.理解(0.375F * (float)(i * 2 - 1), 0.0F, 0.0F);
                光照状态经理.理解(0.0F, -0.375F, 0.0F);
                光照状态经理.辐射(-f1, 1.0F, 0.0F, 0.0F);
                光照状态经理.辐射(-f, 0.0F, 1.0F, 0.0F);
                float f2 = 1.3333334F;
                光照状态经理.障眼物(f2, f2, f2);
                this.playerRenderer.getMainModel().renderDeadmau5Head(0.0625F);
                光照状态经理.流行音乐黑客帝国();
            }
        }
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}
