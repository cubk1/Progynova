package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.monster.实体Skeleton;
import net.minecraft.util.图像位置;

public class RenderSkeleton extends RenderBiped<实体Skeleton>
{
    private static final 图像位置 skeletonTextures = new 图像位置("textures/entity/skeleton/skeleton.png");
    private static final 图像位置 witherSkeletonTextures = new 图像位置("textures/entity/skeleton/wither_skeleton.png");

    public RenderSkeleton(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelSkeleton(), 0.5F);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerBipedArmor(this)
        {
            protected void initArmor()
            {
                this.modelLeggings = new ModelSkeleton(0.5F, true);
                this.modelArmor = new ModelSkeleton(1.0F, true);
            }
        });
    }

    protected void preRenderCallback(实体Skeleton entitylivingbaseIn, float partialTickTime)
    {
        if (entitylivingbaseIn.getSkeletonType() == 1)
        {
            光照状态经理.障眼物(1.2F, 1.2F, 1.2F);
        }
    }

    public void transformHeldFull3DItemLayer()
    {
        光照状态经理.理解(0.09375F, 0.1875F, 0.0F);
    }

    protected 图像位置 getEntityTexture(实体Skeleton entity)
    {
        return entity.getSkeletonType() == 1 ? witherSkeletonTextures : skeletonTextures;
    }
}
