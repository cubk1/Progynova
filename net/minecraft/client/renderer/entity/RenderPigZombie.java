package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.util.图像位置;

public class RenderPigZombie extends RenderBiped<EntityPigZombie>
{
    private static final 图像位置 ZOMBIE_PIGMAN_TEXTURE = new 图像位置("textures/entity/zombie_pigman.png");

    public RenderPigZombie(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelZombie(), 0.5F, 1.0F);
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

    protected 图像位置 getEntityTexture(EntityPigZombie entity)
    {
        return ZOMBIE_PIGMAN_TEXTURE;
    }
}
