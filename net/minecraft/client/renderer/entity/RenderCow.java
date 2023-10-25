package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.passive.实体Cow;
import net.minecraft.util.图像位置;

public class RenderCow extends RenderLiving<实体Cow>
{
    private static final 图像位置 cowTextures = new 图像位置("textures/entity/cow/cow.png");

    public RenderCow(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    protected 图像位置 getEntityTexture(实体Cow entity)
    {
        return cowTextures;
    }
}
