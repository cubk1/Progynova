package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBlaze;
import net.minecraft.entity.monster.实体Blaze;
import net.minecraft.util.图像位置;

public class RenderBlaze extends RenderLiving<实体Blaze>
{
    private static final 图像位置 blazeTextures = new 图像位置("textures/entity/blaze.png");

    public RenderBlaze(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelBlaze(), 0.5F);
    }

    protected 图像位置 getEntityTexture(实体Blaze entity)
    {
        return blazeTextures;
    }
}
