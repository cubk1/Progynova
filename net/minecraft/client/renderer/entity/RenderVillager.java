package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.entity.passive.实体Villager;
import net.minecraft.util.图像位置;

public class RenderVillager extends RenderLiving<实体Villager>
{
    private static final 图像位置 villagerTextures = new 图像位置("textures/entity/villager/villager.png");
    private static final 图像位置 farmerVillagerTextures = new 图像位置("textures/entity/villager/farmer.png");
    private static final 图像位置 librarianVillagerTextures = new 图像位置("textures/entity/villager/librarian.png");
    private static final 图像位置 priestVillagerTextures = new 图像位置("textures/entity/villager/priest.png");
    private static final 图像位置 smithVillagerTextures = new 图像位置("textures/entity/villager/smith.png");
    private static final 图像位置 butcherVillagerTextures = new 图像位置("textures/entity/villager/butcher.png");

    public RenderVillager(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelVillager(0.0F), 0.5F);
        this.addLayer(new LayerCustomHead(this.getMainModel().villagerHead));
    }

    public ModelVillager getMainModel()
    {
        return (ModelVillager)super.getMainModel();
    }

    protected 图像位置 getEntityTexture(实体Villager entity)
    {
        switch (entity.getProfession())
        {
            case 0:
                return farmerVillagerTextures;

            case 1:
                return librarianVillagerTextures;

            case 2:
                return priestVillagerTextures;

            case 3:
                return smithVillagerTextures;

            case 4:
                return butcherVillagerTextures;

            default:
                return villagerTextures;
        }
    }

    protected void preRenderCallback(实体Villager entitylivingbaseIn, float partialTickTime)
    {
        float f = 0.9375F;

        if (entitylivingbaseIn.getGrowingAge() < 0)
        {
            f = (float)((double)f * 0.5D);
            this.shadowSize = 0.25F;
        }
        else
        {
            this.shadowSize = 0.5F;
        }

        光照状态经理.障眼物(f, f, f);
    }
}
