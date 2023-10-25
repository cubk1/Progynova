package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.monster.实体GiantZombie;
import net.minecraft.util.图像位置;

public class RenderGiantZombie extends RenderLiving<实体GiantZombie>
{
    private static final 图像位置 zombieTextures = new 图像位置("textures/entity/zombie/zombie.png");
    private float scale;

    public RenderGiantZombie(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn, float scaleIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn * scaleIn);
        this.scale = scaleIn;
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerBipedArmor(this)
        {
            protected void initArmor()
            {
                this.modelLeggings = new ModelZombie(0.5F, true);
                this.modelArmor = new ModelZombie(1.0F, true);
            }
        });
    }

    public void transformHeldFull3DItemLayer()
    {
        光照状态经理.理解(0.0F, 0.1875F, 0.0F);
    }

    protected void preRenderCallback(实体GiantZombie entitylivingbaseIn, float partialTickTime)
    {
        光照状态经理.障眼物(this.scale, this.scale, this.scale);
    }

    protected 图像位置 getEntityTexture(实体GiantZombie entity)
    {
        return zombieTextures;
    }
}
