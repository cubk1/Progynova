package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;

public class RenderChicken extends RenderLiving<EntityChicken>
{
    private static final 图像位置 chickenTextures = new 图像位置("textures/entity/chicken.png");

    public RenderChicken(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    protected 图像位置 getEntityTexture(EntityChicken entity)
    {
        return chickenTextures;
    }

    protected float handleRotationFloat(EntityChicken livingBase, float partialTicks)
    {
        float f = livingBase.field_70888_h + (livingBase.wingRotation - livingBase.field_70888_h) * partialTicks;
        float f1 = livingBase.field_70884_g + (livingBase.destPos - livingBase.field_70884_g) * partialTicks;
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
}
