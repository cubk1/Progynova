package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.entity.monster.实体Silverfish;
import net.minecraft.util.图像位置;

public class RenderSilverfish extends RenderLiving<实体Silverfish>
{
    private static final 图像位置 silverfishTextures = new 图像位置("textures/entity/silverfish.png");

    public RenderSilverfish(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelSilverfish(), 0.3F);
    }

    protected float getDeathMaxRotation(实体Silverfish entityLivingBaseIn)
    {
        return 180.0F;
    }

    protected 图像位置 getEntityTexture(实体Silverfish entity)
    {
        return silverfishTextures;
    }
}
