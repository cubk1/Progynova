package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.passive.实体Sheep;
import net.minecraft.entity.passive.实体Wolf;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.src.Config;
import net.minecraft.util.图像位置;
import net.optifine.CustomColors;

public class LayerWolfCollar implements LayerRenderer<实体Wolf>
{
    private static final 图像位置 WOLF_COLLAR = new 图像位置("textures/entity/wolf/wolf_collar.png");
    private final RenderWolf wolfRenderer;

    public LayerWolfCollar(RenderWolf wolfRendererIn)
    {
        this.wolfRenderer = wolfRendererIn;
    }

    public void doRenderLayer(实体Wolf entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        if (entitylivingbaseIn.isTamed() && !entitylivingbaseIn.isInvisible())
        {
            this.wolfRenderer.bindTexture(WOLF_COLLAR);
            EnumDyeColor enumdyecolor = EnumDyeColor.byMetadata(entitylivingbaseIn.getCollarColor().getMetadata());
            float[] afloat = 实体Sheep.getDyeRgb(enumdyecolor);

            if (Config.isCustomColors())
            {
                afloat = CustomColors.getWolfCollarColors(enumdyecolor, afloat);
            }

            光照状态经理.色彩(afloat[0], afloat[1], afloat[2]);
            this.wolfRenderer.getMainModel().render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
        }
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}
