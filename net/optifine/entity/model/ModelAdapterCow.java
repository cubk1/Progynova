package net.optifine.entity.model;

import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.renderer.entity.RenderCow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityCow;

public class ModelAdapterCow extends ModelAdapterQuadruped
{
    public ModelAdapterCow()
    {
        super(EntityCow.class, "cow", 0.7F);
    }

    public ModelBase makeModel()
    {
        return new ModelCow();
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = 我的手艺.得到我的手艺().getRenderManager();
        return new RenderCow(rendermanager, modelBase, shadowSize);
    }
}
