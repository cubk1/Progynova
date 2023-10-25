package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelEnderMite;
import net.minecraft.entity.monster.实体Endermite;
import net.minecraft.util.图像位置;

public class RenderEndermite extends RenderLiving<实体Endermite>
{
    private static final 图像位置 ENDERMITE_TEXTURES = new 图像位置("textures/entity/endermite.png");

    public RenderEndermite(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelEnderMite(), 0.3F);
    }

    protected float getDeathMaxRotation(实体Endermite entityLivingBaseIn)
    {
        return 180.0F;
    }

    protected 图像位置 getEntityTexture(实体Endermite entity)
    {
        return ENDERMITE_TEXTURES;
    }
}
