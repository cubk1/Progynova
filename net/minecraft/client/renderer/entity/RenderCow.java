package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.图像位置;

public class RenderCow extends RenderLiving<EntityCow>
{
    private static final 图像位置 cowTextures = new 图像位置("textures/entity/cow/cow.png");

    public RenderCow(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    protected 图像位置 getEntityTexture(EntityCow entity)
    {
        return cowTextures;
    }
}
