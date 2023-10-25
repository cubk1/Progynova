package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.util.图像位置;

public class RenderSilverfish extends RenderLiving<EntitySilverfish>
{
    private static final 图像位置 silverfishTextures = new 图像位置("textures/entity/silverfish.png");

    public RenderSilverfish(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelSilverfish(), 0.3F);
    }

    protected float getDeathMaxRotation(EntitySilverfish entityLivingBaseIn)
    {
        return 180.0F;
    }

    protected 图像位置 getEntityTexture(EntitySilverfish entity)
    {
        return silverfishTextures;
    }
}
