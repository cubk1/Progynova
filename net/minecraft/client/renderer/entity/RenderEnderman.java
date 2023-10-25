package net.minecraft.client.renderer.entity;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.renderer.entity.layers.LayerEndermanEyes;
import net.minecraft.client.renderer.entity.layers.LayerHeldBlock;
import net.minecraft.entity.monster.实体Enderman;
import net.minecraft.util.图像位置;

public class RenderEnderman extends RenderLiving<实体Enderman>
{
    private static final 图像位置 endermanTextures = new 图像位置("textures/entity/enderman/enderman.png");
    private ModelEnderman endermanModel;
    private Random rnd = new Random();

    public RenderEnderman(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelEnderman(0.0F), 0.5F);
        this.endermanModel = (ModelEnderman)super.mainModel;
        this.addLayer(new LayerEndermanEyes(this));
        this.addLayer(new LayerHeldBlock(this));
    }

    public void doRender(实体Enderman entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.endermanModel.isCarrying = entity.getHeldBlockState().getBlock().getMaterial() != Material.air;
        this.endermanModel.isAttacking = entity.isScreaming();

        if (entity.isScreaming())
        {
            double d0 = 0.02D;
            x += this.rnd.nextGaussian() * d0;
            z += this.rnd.nextGaussian() * d0;
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected 图像位置 getEntityTexture(实体Enderman entity)
    {
        return endermanTextures;
    }
}
