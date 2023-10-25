package net.optifine.entity.model;

import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.entity.passive.实体Pig;

public class ModelAdapterPig extends ModelAdapterQuadruped
{
    public ModelAdapterPig()
    {
        super(实体Pig.class, "pig", 0.7F);
    }

    public ModelBase makeModel()
    {
        return new ModelPig();
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = 我的手艺.得到我的手艺().getRenderManager();
        return new RenderPig(rendermanager, modelBase, shadowSize);
    }
}
