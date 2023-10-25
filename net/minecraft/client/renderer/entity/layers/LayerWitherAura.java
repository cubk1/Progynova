package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelWither;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.entity.RenderWither;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class LayerWitherAura implements LayerRenderer<EntityWither>
{
    private static final ResourceLocation WITHER_ARMOR = new ResourceLocation("textures/entity/wither/wither_armor.png");
    private final RenderWither witherRenderer;
    private final ModelWither witherModel = new ModelWither(0.5F);

    public LayerWitherAura(RenderWither witherRendererIn)
    {
        this.witherRenderer = witherRendererIn;
    }

    public void doRenderLayer(EntityWither entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        if (entitylivingbaseIn.isArmored())
        {
            光照状态经理.depthMask(!entitylivingbaseIn.isInvisible());
            this.witherRenderer.bindTexture(WITHER_ARMOR);
            光照状态经理.matrixMode(5890);
            光照状态经理.loadIdentity();
            float f = (float)entitylivingbaseIn.ticksExisted + partialTicks;
            float f1 = MathHelper.cos(f * 0.02F) * 3.0F;
            float f2 = f * 0.01F;
            光照状态经理.理解(f1, f2, 0.0F);
            光照状态经理.matrixMode(5888);
            光照状态经理.启用混合品();
            float f3 = 0.5F;
            光照状态经理.色彩(f3, f3, f3, 1.0F);
            光照状态经理.disableLighting();
            光照状态经理.正常工作(1, 1);
            this.witherModel.setLivingAnimations(entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks);
            this.witherModel.setModelAttributes(this.witherRenderer.getMainModel());
            this.witherModel.render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
            光照状态经理.matrixMode(5890);
            光照状态经理.loadIdentity();
            光照状态经理.matrixMode(5888);
            光照状态经理.enableLighting();
            光照状态经理.禁用混合品();
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
