package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.entity.layers.LayerCreeperCharge;
import net.minecraft.entity.monster.实体Creeper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;

public class RenderCreeper extends RenderLiving<实体Creeper>
{
    private static final 图像位置 creeperTextures = new 图像位置("textures/entity/creeper/creeper.png");

    public RenderCreeper(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelCreeper(), 0.5F);
        this.addLayer(new LayerCreeperCharge(this));
    }

    protected void preRenderCallback(实体Creeper entitylivingbaseIn, float partialTickTime)
    {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp_float(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        光照状态经理.障眼物(f2, f3, f2);
    }

    protected int getColorMultiplier(实体Creeper entitylivingbaseIn, float lightBrightness, float partialTickTime)
    {
        float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);

        if ((int)(f * 10.0F) % 2 == 0)
        {
            return 0;
        }
        else
        {
            int i = (int)(f * 0.2F * 255.0F);
            i = MathHelper.clamp_int(i, 0, 255);
            return i << 24 | 16777215;
        }
    }

    protected 图像位置 getEntityTexture(实体Creeper entity)
    {
        return creeperTextures;
    }
}
