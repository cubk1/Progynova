package net.optifine.entity.model;

import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.实体Enderman;

public class ModelAdapterEnderman extends ModelAdapterBiped
{
    public ModelAdapterEnderman()
    {
        super(实体Enderman.class, "enderman", 0.5F);
    }

    public ModelBase makeModel()
    {
        return new ModelEnderman(0.0F);
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = 我的手艺.得到我的手艺().getRenderManager();
        RenderEnderman renderenderman = new RenderEnderman(rendermanager);
        renderenderman.mainModel = modelBase;
        renderenderman.shadowSize = shadowSize;
        return renderenderman;
    }
}
