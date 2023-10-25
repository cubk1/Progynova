package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.entity.monster.实体Slime;

public class LayerSlimeGel implements LayerRenderer<实体Slime>
{
    private final RenderSlime slimeRenderer;
    private final ModelBase slimeModel = new ModelSlime(0);

    public LayerSlimeGel(RenderSlime slimeRendererIn)
    {
        this.slimeRenderer = slimeRendererIn;
    }

    public void doRenderLayer(实体Slime entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        if (!entitylivingbaseIn.isInvisible())
        {
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            光照状态经理.enableNormalize();
            光照状态经理.启用混合品();
            光照状态经理.正常工作(770, 771);
            this.slimeModel.setModelAttributes(this.slimeRenderer.getMainModel());
            this.slimeModel.render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
            光照状态经理.禁用混合品();
            光照状态经理.disableNormalize();
        }
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}
