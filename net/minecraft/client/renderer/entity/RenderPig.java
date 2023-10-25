package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.layers.LayerSaddle;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.图像位置;

public class RenderPig extends RenderLiving<EntityPig>
{
    private static final 图像位置 pigTextures = new 图像位置("textures/entity/pig/pig.png");

    public RenderPig(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
        this.addLayer(new LayerSaddle(this));
    }

    protected 图像位置 getEntityTexture(EntityPig entity)
    {
        return pigTextures;
    }
}
