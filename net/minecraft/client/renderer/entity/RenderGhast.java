package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelGhast;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.util.图像位置;

public class RenderGhast extends RenderLiving<EntityGhast>
{
    private static final 图像位置 ghastTextures = new 图像位置("textures/entity/ghast/ghast.png");
    private static final 图像位置 ghastShootingTextures = new 图像位置("textures/entity/ghast/ghast_shooting.png");

    public RenderGhast(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelGhast(), 0.5F);
    }

    protected 图像位置 getEntityTexture(EntityGhast entity)
    {
        return entity.isAttacking() ? ghastShootingTextures : ghastTextures;
    }

    protected void preRenderCallback(EntityGhast entitylivingbaseIn, float partialTickTime)
    {
        float f = 1.0F;
        float f1 = (8.0F + f) / 2.0F;
        float f2 = (8.0F + 1.0F / f) / 2.0F;
        光照状态经理.障眼物(f2, f1, f2);
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
