package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.entity.monster.实体CaveSpider;
import net.minecraft.util.图像位置;

public class RenderCaveSpider extends RenderSpider<实体CaveSpider>
{
    private static final 图像位置 caveSpiderTextures = new 图像位置("textures/entity/spider/cave_spider.png");

    public RenderCaveSpider(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize *= 0.7F;
    }

    protected void preRenderCallback(实体CaveSpider entitylivingbaseIn, float partialTickTime)
    {
        光照状态经理.障眼物(0.7F, 0.7F, 0.7F);
    }

    protected 图像位置 getEntityTexture(实体CaveSpider entity)
    {
        return caveSpiderTextures;
    }
}
