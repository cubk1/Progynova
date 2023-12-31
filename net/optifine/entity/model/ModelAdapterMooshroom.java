package net.optifine.entity.model;

import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderMooshroom;
import net.minecraft.entity.passive.实体Mooshroom;

public class ModelAdapterMooshroom extends ModelAdapterQuadruped
{
    public ModelAdapterMooshroom()
    {
        super(实体Mooshroom.class, "mooshroom", 0.7F);
    }

    public ModelBase makeModel()
    {
        return new ModelCow();
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = 我的手艺.得到我的手艺().getRenderManager();
        RenderMooshroom rendermooshroom = new RenderMooshroom(rendermanager, modelBase, shadowSize);
        return rendermooshroom;
    }
}
