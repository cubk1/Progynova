package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.passive.实体Chicken;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;

public class RenderChicken extends RenderLiving<实体Chicken>
{
    private static final 图像位置 chickenTextures = new 图像位置("textures/entity/chicken.png");

    public RenderChicken(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    protected 图像位置 getEntityTexture(实体Chicken entity)
    {
        return chickenTextures;
    }

    protected float handleRotationFloat(实体Chicken livingBase, float partialTicks)
    {
        float f = livingBase.field_70888_h + (livingBase.wingRotation - livingBase.field_70888_h) * partialTicks;
        float f1 = livingBase.field_70884_g + (livingBase.destPos - livingBase.field_70884_g) * partialTicks;
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
}
