package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.layers.LayerSaddle;
import net.minecraft.entity.passive.实体Pig;
import net.minecraft.util.图像位置;

public class RenderPig extends RenderLiving<实体Pig>
{
    private static final 图像位置 pigTextures = new 图像位置("textures/entity/pig/pig.png");

    public RenderPig(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
        this.addLayer(new LayerSaddle(this));
    }

    protected 图像位置 getEntityTexture(实体Pig entity)
    {
        return pigTextures;
    }
}
