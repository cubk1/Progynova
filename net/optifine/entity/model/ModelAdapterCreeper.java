package net.optifine.entity.model;

import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.实体Creeper;

public class ModelAdapterCreeper extends ModelAdapter
{
    public ModelAdapterCreeper()
    {
        super(实体Creeper.class, "creeper", 0.5F);
    }

    public ModelBase makeModel()
    {
        return new ModelCreeper();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelCreeper))
        {
            return null;
        }
        else
        {
            ModelCreeper modelcreeper = (ModelCreeper)model;
            return modelPart.equals("head") ? modelcreeper.head : (modelPart.equals("armor") ? modelcreeper.creeperArmor : (modelPart.equals("body") ? modelcreeper.body : (modelPart.equals("leg1") ? modelcreeper.leg1 : (modelPart.equals("leg2") ? modelcreeper.leg2 : (modelPart.equals("leg3") ? modelcreeper.leg3 : (modelPart.equals("leg4") ? modelcreeper.leg4 : null))))));
        }
    }

    public String[] getModelRendererNames()
    {
        return new String[] {"head", "armor", "body", "leg1", "leg2", "leg3", "leg4"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = 我的手艺.得到我的手艺().getRenderManager();
        RenderCreeper rendercreeper = new RenderCreeper(rendermanager);
        rendercreeper.mainModel = modelBase;
        rendercreeper.shadowSize = shadowSize;
        return rendercreeper;
    }
}
