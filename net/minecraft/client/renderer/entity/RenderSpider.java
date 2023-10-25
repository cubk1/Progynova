package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.图像位置;

public class RenderSpider<T extends EntitySpider> extends RenderLiving<T>
{
    private static final 图像位置 spiderTextures = new 图像位置("textures/entity/spider/spider.png");

    public RenderSpider(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelSpider(), 1.0F);
        this.addLayer(new LayerSpiderEyes(this));
    }

    protected float getDeathMaxRotation(T entityLivingBaseIn)
    {
        return 180.0F;
    }

    protected 图像位置 getEntityTexture(T entity)
    {
        return spiderTextures;
    }
}
