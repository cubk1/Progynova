package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.entity.layers.LayerSlimeGel;
import net.minecraft.entity.monster.实体Slime;
import net.minecraft.util.图像位置;

public class RenderSlime extends RenderLiving<实体Slime>
{
    private static final 图像位置 slimeTextures = new 图像位置("textures/entity/slime/slime.png");

    public RenderSlime(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
        this.addLayer(new LayerSlimeGel(this));
    }

    public void doRender(实体Slime entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.shadowSize = 0.25F * (float)entity.getSlimeSize();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected void preRenderCallback(实体Slime entitylivingbaseIn, float partialTickTime)
    {
        float f = (float)entitylivingbaseIn.getSlimeSize();
        float f1 = (entitylivingbaseIn.prevSquishFactor + (entitylivingbaseIn.squishFactor - entitylivingbaseIn.prevSquishFactor) * partialTickTime) / (f * 0.5F + 1.0F);
        float f2 = 1.0F / (f1 + 1.0F);
        光照状态经理.障眼物(f2 * f, 1.0F / f2 * f, f2 * f);
    }

    protected 图像位置 getEntityTexture(实体Slime entity)
    {
        return slimeTextures;
    }
}
