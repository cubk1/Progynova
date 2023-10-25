package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.entity.layers.LayerWolfCollar;
import net.minecraft.entity.passive.实体Wolf;
import net.minecraft.util.图像位置;

public class RenderWolf extends RenderLiving<实体Wolf>
{
    private static final 图像位置 wolfTextures = new 图像位置("textures/entity/wolf/wolf.png");
    private static final 图像位置 tamedWolfTextures = new 图像位置("textures/entity/wolf/wolf_tame.png");
    private static final 图像位置 anrgyWolfTextures = new 图像位置("textures/entity/wolf/wolf_angry.png");

    public RenderWolf(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
        this.addLayer(new LayerWolfCollar(this));
    }

    protected float handleRotationFloat(实体Wolf livingBase, float partialTicks)
    {
        return livingBase.getTailRotation();
    }

    public void doRender(实体Wolf entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if (entity.isWolfWet())
        {
            float f = entity.getBrightness(partialTicks) * entity.getShadingWhileWet(partialTicks);
            光照状态经理.色彩(f, f, f);
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected 图像位置 getEntityTexture(实体Wolf entity)
    {
        return entity.isTamed() ? tamedWolfTextures : (entity.isAngry() ? anrgyWolfTextures : wolfTextures);
    }
}
