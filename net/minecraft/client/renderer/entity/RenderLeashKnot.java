package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelLeashKnot;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.util.图像位置;

public class RenderLeashKnot extends Render<EntityLeashKnot>
{
    private static final 图像位置 leashKnotTextures = new 图像位置("textures/entity/lead_knot.png");
    private ModelLeashKnot leashKnotModel = new ModelLeashKnot();

    public RenderLeashKnot(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    public void doRender(EntityLeashKnot entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        光照状态经理.推黑客帝国();
        光照状态经理.disableCull();
        光照状态经理.理解((float)x, (float)y, (float)z);
        float f = 0.0625F;
        光照状态经理.enableRescaleNormal();
        光照状态经理.障眼物(-1.0F, -1.0F, 1.0F);
        光照状态经理.启用希腊字母表的第1个字母();
        this.bindEntityTexture(entity);
        this.leashKnotModel.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f);
        光照状态经理.流行音乐黑客帝国();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected 图像位置 getEntityTexture(EntityLeashKnot entity)
    {
        return leashKnotTextures;
    }
}
