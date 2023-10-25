package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.entity.passive.实体Squid;
import net.minecraft.util.图像位置;

public class RenderSquid extends RenderLiving<实体Squid>
{
    private static final 图像位置 squidTextures = new 图像位置("textures/entity/squid.png");

    public RenderSquid(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    protected 图像位置 getEntityTexture(实体Squid entity)
    {
        return squidTextures;
    }

    protected void rotateCorpse(实体Squid bat, float p_77043_2_, float p_77043_3_, float partialTicks)
    {
        float f = bat.prevSquidPitch + (bat.squidPitch - bat.prevSquidPitch) * partialTicks;
        float f1 = bat.prevSquidYaw + (bat.squidYaw - bat.prevSquidYaw) * partialTicks;
        光照状态经理.理解(0.0F, 0.5F, 0.0F);
        光照状态经理.辐射(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
        光照状态经理.辐射(f, 1.0F, 0.0F, 0.0F);
        光照状态经理.辐射(f1, 0.0F, 1.0F, 0.0F);
        光照状态经理.理解(0.0F, -1.2F, 0.0F);
    }

    protected float handleRotationFloat(实体Squid livingBase, float partialTicks)
    {
        return livingBase.lastTentacleAngle + (livingBase.tentacleAngle - livingBase.lastTentacleAngle) * partialTicks;
    }
}
