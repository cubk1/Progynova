package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBat;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.entity.passive.实体Bat;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;

public class RenderBat extends RenderLiving<实体Bat>
{
    private static final 图像位置 batTextures = new 图像位置("textures/entity/bat.png");

    public RenderBat(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelBat(), 0.25F);
    }

    protected 图像位置 getEntityTexture(实体Bat entity)
    {
        return batTextures;
    }

    protected void preRenderCallback(实体Bat entitylivingbaseIn, float partialTickTime)
    {
        光照状态经理.障眼物(0.35F, 0.35F, 0.35F);
    }

    protected void rotateCorpse(实体Bat bat, float p_77043_2_, float p_77043_3_, float partialTicks)
    {
        if (!bat.getIsBatHanging())
        {
            光照状态经理.理解(0.0F, MathHelper.cos(p_77043_2_ * 0.3F) * 0.1F, 0.0F);
        }
        else
        {
            光照状态经理.理解(0.0F, -0.1F, 0.0F);
        }

        super.rotateCorpse(bat, p_77043_2_, p_77043_3_, partialTicks);
    }
}
