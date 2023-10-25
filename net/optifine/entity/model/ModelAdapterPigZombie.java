package net.optifine.entity.model;

import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPigZombie;
import net.minecraft.entity.monster.实体PigZombie;

public class ModelAdapterPigZombie extends ModelAdapterBiped
{
    public ModelAdapterPigZombie()
    {
        super(实体PigZombie.class, "zombie_pigman", 0.5F);
    }

    public ModelBase makeModel()
    {
        return new ModelZombie();
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = 我的手艺.得到我的手艺().getRenderManager();
        RenderPigZombie renderpigzombie = new RenderPigZombie(rendermanager);
        Render.setModelBipedMain(renderpigzombie, (ModelBiped)modelBase);
        renderpigzombie.mainModel = modelBase;
        renderpigzombie.shadowSize = shadowSize;
        return renderpigzombie;
    }
}
