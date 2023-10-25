package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderEntity extends Render<Entity>
{
    public RenderEntity(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        光照状态经理.推黑客帝国();
        renderOffsetAABB(entity.getEntityBoundingBox(), x - entity.lastTickPosX, y - entity.lastTickPosY, z - entity.lastTickPosZ);
        光照状态经理.流行音乐黑客帝国();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return null;
    }
}
