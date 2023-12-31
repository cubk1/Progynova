package net.optifine.entity.model;

import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraft.entity.passive.实体Villager;

public class ModelAdapterVillager extends ModelAdapter
{
    public ModelAdapterVillager()
    {
        super(实体Villager.class, "villager", 0.5F);
    }

    public ModelBase makeModel()
    {
        return new ModelVillager(0.0F);
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelVillager))
        {
            return null;
        }
        else
        {
            ModelVillager modelvillager = (ModelVillager)model;
            return modelPart.equals("head") ? modelvillager.villagerHead : (modelPart.equals("body") ? modelvillager.villagerBody : (modelPart.equals("arms") ? modelvillager.villagerArms : (modelPart.equals("left_leg") ? modelvillager.leftVillagerLeg : (modelPart.equals("right_leg") ? modelvillager.rightVillagerLeg : (modelPart.equals("nose") ? modelvillager.villagerNose : null)))));
        }
    }

    public String[] getModelRendererNames()
    {
        return new String[] {"head", "body", "arms", "right_leg", "left_leg", "nose"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = 我的手艺.得到我的手艺().getRenderManager();
        RenderVillager rendervillager = new RenderVillager(rendermanager);
        rendervillager.mainModel = modelBase;
        rendervillager.shadowSize = shadowSize;
        return rendervillager;
    }
}
