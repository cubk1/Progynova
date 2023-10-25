package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelEnderMite;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.util.图像位置;

public class RenderEndermite extends RenderLiving<EntityEndermite>
{
    private static final 图像位置 ENDERMITE_TEXTURES = new 图像位置("textures/entity/endermite.png");

    public RenderEndermite(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelEnderMite(), 0.3F);
    }

    protected float getDeathMaxRotation(EntityEndermite entityLivingBaseIn)
    {
        return 180.0F;
    }

    protected 图像位置 getEntityTexture(EntityEndermite entity)
    {
        return ENDERMITE_TEXTURES;
    }
}
