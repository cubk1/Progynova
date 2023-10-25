package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;

public class RenderBoat extends Render<EntityBoat>
{
    private static final 图像位置 boatTextures = new 图像位置("textures/entity/boat.png");
    protected ModelBase modelBoat = new ModelBoat();

    public RenderBoat(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
    }

    public void doRender(EntityBoat entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        光照状态经理.推黑客帝国();
        光照状态经理.理解((float)x, (float)y + 0.25F, (float)z);
        光照状态经理.辐射(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
        float f = (float)entity.getTimeSinceHit() - partialTicks;
        float f1 = entity.getDamageTaken() - partialTicks;

        if (f1 < 0.0F)
        {
            f1 = 0.0F;
        }

        if (f > 0.0F)
        {
            光照状态经理.辐射(MathHelper.sin(f) * f * f1 / 10.0F * (float)entity.getForwardDirection(), 1.0F, 0.0F, 0.0F);
        }

        float f2 = 0.75F;
        光照状态经理.障眼物(f2, f2, f2);
        光照状态经理.障眼物(1.0F / f2, 1.0F / f2, 1.0F / f2);
        this.bindEntityTexture(entity);
        光照状态经理.障眼物(-1.0F, -1.0F, 1.0F);
        this.modelBoat.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        光照状态经理.流行音乐黑客帝国();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected 图像位置 getEntityTexture(EntityBoat entity)
    {
        return boatTextures;
    }
}
