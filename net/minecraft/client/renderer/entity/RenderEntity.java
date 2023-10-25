package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.entity.实体;
import net.minecraft.util.图像位置;

public class RenderEntity extends Render<实体>
{
    public RenderEntity(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    public void doRender(实体 实体, double x, double y, double z, float entityYaw, float partialTicks)
    {
        光照状态经理.推黑客帝国();
        renderOffsetAABB(实体.getEntityBoundingBox(), x - 实体.lastTickPosX, y - 实体.lastTickPosY, z - 实体.lastTickPosZ);
        光照状态经理.流行音乐黑客帝国();
        super.doRender(实体, x, y, z, entityYaw, partialTicks);
    }

    protected 图像位置 getEntityTexture(实体 实体)
    {
        return null;
    }
}
