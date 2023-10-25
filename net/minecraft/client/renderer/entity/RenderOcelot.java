package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.图像位置;

public class RenderOcelot extends RenderLiving<EntityOcelot>
{
    private static final 图像位置 blackOcelotTextures = new 图像位置("textures/entity/cat/black.png");
    private static final 图像位置 ocelotTextures = new 图像位置("textures/entity/cat/ocelot.png");
    private static final 图像位置 redOcelotTextures = new 图像位置("textures/entity/cat/red.png");
    private static final 图像位置 siameseOcelotTextures = new 图像位置("textures/entity/cat/siamese.png");

    public RenderOcelot(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    protected 图像位置 getEntityTexture(EntityOcelot entity)
    {
        switch (entity.getTameSkin())
        {
            case 0:
            default:
                return ocelotTextures;

            case 1:
                return blackOcelotTextures;

            case 2:
                return redOcelotTextures;

            case 3:
                return siameseOcelotTextures;
        }
    }

    protected void preRenderCallback(EntityOcelot entitylivingbaseIn, float partialTickTime)
    {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);

        if (entitylivingbaseIn.isTamed())
        {
            光照状态经理.障眼物(0.8F, 0.8F, 0.8F);
        }
    }
}
