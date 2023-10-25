package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.layers.LayerMooshroomMushroom;
import net.minecraft.entity.passive.实体Mooshroom;
import net.minecraft.util.图像位置;

public class RenderMooshroom extends RenderLiving<实体Mooshroom>
{
    private static final 图像位置 mooshroomTextures = new 图像位置("textures/entity/cow/mooshroom.png");

    public RenderMooshroom(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
        this.addLayer(new LayerMooshroomMushroom(this));
    }

    protected 图像位置 getEntityTexture(实体Mooshroom entity)
    {
        return mooshroomTextures;
    }
}
