package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.entity.monster.实体Creeper;
import net.minecraft.util.图像位置;

public class LayerCreeperCharge implements LayerRenderer<实体Creeper>
{
    private static final 图像位置 LIGHTNING_TEXTURE = new 图像位置("textures/entity/creeper/creeper_armor.png");
    private final RenderCreeper creeperRenderer;
    private final ModelCreeper creeperModel = new ModelCreeper(2.0F);

    public LayerCreeperCharge(RenderCreeper creeperRendererIn)
    {
        this.creeperRenderer = creeperRendererIn;
    }

    public void doRenderLayer(实体Creeper entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        if (entitylivingbaseIn.getPowered())
        {
            boolean flag = entitylivingbaseIn.isInvisible();
            光照状态经理.depthMask(!flag);
            this.creeperRenderer.bindTexture(LIGHTNING_TEXTURE);
            光照状态经理.matrixMode(5890);
            光照状态经理.loadIdentity();
            float f = (float)entitylivingbaseIn.已存在的刻度 + partialTicks;
            光照状态经理.理解(f * 0.01F, f * 0.01F, 0.0F);
            光照状态经理.matrixMode(5888);
            光照状态经理.启用混合品();
            float f1 = 0.5F;
            光照状态经理.色彩(f1, f1, f1, 1.0F);
            光照状态经理.disableLighting();
            光照状态经理.正常工作(1, 1);
            this.creeperModel.setModelAttributes(this.creeperRenderer.getMainModel());
            this.creeperModel.render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
            光照状态经理.matrixMode(5890);
            光照状态经理.loadIdentity();
            光照状态经理.matrixMode(5888);
            光照状态经理.enableLighting();
            光照状态经理.禁用混合品();
            光照状态经理.depthMask(flag);
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
