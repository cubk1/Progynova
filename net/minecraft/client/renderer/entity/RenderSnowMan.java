package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSnowMan;
import net.minecraft.client.renderer.entity.layers.LayerSnowmanHead;
import net.minecraft.entity.monster.实体Snowman;
import net.minecraft.util.图像位置;

public class RenderSnowMan extends RenderLiving<实体Snowman>
{
    private static final 图像位置 snowManTextures = new 图像位置("textures/entity/snowman.png");

    public RenderSnowMan(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelSnowMan(), 0.5F);
        this.addLayer(new LayerSnowmanHead(this));
    }

    protected 图像位置 getEntityTexture(实体Snowman entity)
    {
        return snowManTextures;
    }

    public ModelSnowMan getMainModel()
    {
        return (ModelSnowMan)super.getMainModel();
    }
}
