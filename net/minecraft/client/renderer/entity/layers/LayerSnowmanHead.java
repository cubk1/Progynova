package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderSnowMan;
import net.minecraft.entity.monster.实体Snowman;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class LayerSnowmanHead implements LayerRenderer<实体Snowman>
{
    private final RenderSnowMan snowManRenderer;

    public LayerSnowmanHead(RenderSnowMan snowManRendererIn)
    {
        this.snowManRenderer = snowManRendererIn;
    }

    public void doRenderLayer(实体Snowman entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        if (!entitylivingbaseIn.isInvisible())
        {
            光照状态经理.推黑客帝国();
            this.snowManRenderer.getMainModel().head.postRender(0.0625F);
            float f = 0.625F;
            光照状态经理.理解(0.0F, -0.34375F, 0.0F);
            光照状态经理.辐射(180.0F, 0.0F, 1.0F, 0.0F);
            光照状态经理.障眼物(f, -f, -f);
            我的手艺.得到我的手艺().getItemRenderer().renderItem(entitylivingbaseIn, new ItemStack(Blocks.pumpkin, 1), ItemCameraTransforms.TransformType.HEAD);
            光照状态经理.流行音乐黑客帝国();
        }
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}
