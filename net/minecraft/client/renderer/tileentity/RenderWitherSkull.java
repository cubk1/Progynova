package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.util.图像位置;

public class RenderWitherSkull extends Render<EntityWitherSkull>
{
    private static final 图像位置 invulnerableWitherTextures = new 图像位置("textures/entity/wither/wither_invulnerable.png");
    private static final 图像位置 witherTextures = new 图像位置("textures/entity/wither/wither.png");
    private final ModelSkeletonHead skeletonHeadModel = new ModelSkeletonHead();

    public RenderWitherSkull(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    private float func_82400_a(float p_82400_1_, float p_82400_2_, float p_82400_3_)
    {
        float f;

        for (f = p_82400_2_ - p_82400_1_; f < -180.0F; f += 360.0F)
        {
            ;
        }

        while (f >= 180.0F)
        {
            f -= 360.0F;
        }

        return p_82400_1_ + p_82400_3_ * f;
    }

    public void doRender(EntityWitherSkull entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        光照状态经理.推黑客帝国();
        光照状态经理.disableCull();
        float f = this.func_82400_a(entity.prevRotationYaw, entity.旋转侧滑, partialTicks);
        float f1 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        光照状态经理.理解((float)x, (float)y, (float)z);
        float f2 = 0.0625F;
        光照状态经理.enableRescaleNormal();
        光照状态经理.障眼物(-1.0F, -1.0F, 1.0F);
        光照状态经理.启用希腊字母表的第1个字母();
        this.bindEntityTexture(entity);
        this.skeletonHeadModel.render(entity, 0.0F, 0.0F, 0.0F, f, f1, f2);
        光照状态经理.流行音乐黑客帝国();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected 图像位置 getEntityTexture(EntityWitherSkull entity)
    {
        return entity.isInvulnerable() ? invulnerableWitherTextures : witherTextures;
    }
}
