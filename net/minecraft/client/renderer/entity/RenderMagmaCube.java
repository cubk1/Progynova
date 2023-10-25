package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelMagmaCube;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.entity.monster.实体MagmaCube;
import net.minecraft.util.图像位置;

public class RenderMagmaCube extends RenderLiving<实体MagmaCube>
{
    private static final 图像位置 magmaCubeTextures = new 图像位置("textures/entity/slime/magmacube.png");

    public RenderMagmaCube(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelMagmaCube(), 0.25F);
    }

    protected 图像位置 getEntityTexture(实体MagmaCube entity)
    {
        return magmaCubeTextures;
    }

    protected void preRenderCallback(实体MagmaCube entitylivingbaseIn, float partialTickTime)
    {
        int i = entitylivingbaseIn.getSlimeSize();
        float f = (entitylivingbaseIn.prevSquishFactor + (entitylivingbaseIn.squishFactor - entitylivingbaseIn.prevSquishFactor) * partialTickTime) / ((float)i * 0.5F + 1.0F);
        float f1 = 1.0F / (f + 1.0F);
        float f2 = (float)i;
        光照状态经理.障眼物(f1 * f2, 1.0F / f1 * f2, f1 * f2);
    }
}
