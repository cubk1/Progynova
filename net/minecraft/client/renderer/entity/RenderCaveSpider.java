package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.util.ResourceLocation;

public class RenderCaveSpider extends RenderSpider<EntityCaveSpider>
{
    private static final ResourceLocation caveSpiderTextures = new ResourceLocation("textures/entity/spider/cave_spider.png");

    public RenderCaveSpider(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize *= 0.7F;
    }

    protected void preRenderCallback(EntityCaveSpider entitylivingbaseIn, float partialTickTime)
    {
        光照状态经理.障眼物(0.7F, 0.7F, 0.7F);
    }

    protected ResourceLocation getEntityTexture(EntityCaveSpider entity)
    {
        return caveSpiderTextures;
    }
}
