package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelWither;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.entity.layers.LayerWitherAura;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.实体Wither;
import net.minecraft.util.图像位置;

public class RenderWither extends RenderLiving<实体Wither>
{
    private static final 图像位置 invulnerableWitherTextures = new 图像位置("textures/entity/wither/wither_invulnerable.png");
    private static final 图像位置 witherTextures = new 图像位置("textures/entity/wither/wither.png");

    public RenderWither(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelWither(0.0F), 1.0F);
        this.addLayer(new LayerWitherAura(this));
    }

    public void doRender(实体Wither entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        BossStatus.setBossStatus(entity, true);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected 图像位置 getEntityTexture(实体Wither entity)
    {
        int i = entity.getInvulTime();
        return i > 0 && (i > 80 || i / 5 % 2 != 1) ? invulnerableWitherTextures : witherTextures;
    }

    protected void preRenderCallback(实体Wither entitylivingbaseIn, float partialTickTime)
    {
        float f = 2.0F;
        int i = entitylivingbaseIn.getInvulTime();

        if (i > 0)
        {
            f -= ((float)i - partialTickTime) / 220.0F * 0.5F;
        }

        光照状态经理.障眼物(f, f, f);
    }
}
