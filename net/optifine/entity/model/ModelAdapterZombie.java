package net.optifine.entity.model;

import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.entity.monster.实体Zombie;

public class ModelAdapterZombie extends ModelAdapterBiped
{
    public ModelAdapterZombie()
    {
        super(实体Zombie.class, "zombie", 0.5F);
    }

    public ModelBase makeModel()
    {
        return new ModelZombie();
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = 我的手艺.得到我的手艺().getRenderManager();
        RenderZombie renderzombie = new RenderZombie(rendermanager);
        Render.setModelBipedMain(renderzombie, (ModelBiped)modelBase);
        renderzombie.mainModel = modelBase;
        renderzombie.shadowSize = shadowSize;
        return renderzombie;
    }
}
