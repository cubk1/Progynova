package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.entity.layers.LayerHeldItemWitch;
import net.minecraft.entity.monster.实体Witch;
import net.minecraft.util.图像位置;

public class RenderWitch extends RenderLiving<实体Witch>
{
    private static final 图像位置 witchTextures = new 图像位置("textures/entity/witch.png");

    public RenderWitch(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelWitch(0.0F), 0.5F);
        this.addLayer(new LayerHeldItemWitch(this));
    }

    public void doRender(实体Witch entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        ((ModelWitch)this.mainModel).field_82900_g = entity.getHeldItem() != null;
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected 图像位置 getEntityTexture(实体Witch entity)
    {
        return witchTextures;
    }

    public void transformHeldFull3DItemLayer()
    {
        光照状态经理.理解(0.0F, 0.1875F, 0.0F);
    }

    protected void preRenderCallback(实体Witch entitylivingbaseIn, float partialTickTime)
    {
        float f = 0.9375F;
        光照状态经理.障眼物(f, f, f);
    }
}
