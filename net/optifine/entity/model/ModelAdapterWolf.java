package net.optifine.entity.model;

import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.passive.实体Wolf;
import net.optifine.reflect.Reflector;

public class ModelAdapterWolf extends ModelAdapter
{
    public ModelAdapterWolf()
    {
        super(实体Wolf.class, "wolf", 0.5F);
    }

    public ModelBase makeModel()
    {
        return new ModelWolf();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelWolf))
        {
            return null;
        }
        else
        {
            ModelWolf modelwolf = (ModelWolf)model;
            return modelPart.equals("head") ? modelwolf.wolfHeadMain : (modelPart.equals("body") ? modelwolf.wolfBody : (modelPart.equals("leg1") ? modelwolf.wolfLeg1 : (modelPart.equals("leg2") ? modelwolf.wolfLeg2 : (modelPart.equals("leg3") ? modelwolf.wolfLeg3 : (modelPart.equals("leg4") ? modelwolf.wolfLeg4 : (modelPart.equals("tail") ? (ModelRenderer)Reflector.getFieldValue(modelwolf, Reflector.ModelWolf_tail) : (modelPart.equals("mane") ? (ModelRenderer)Reflector.getFieldValue(modelwolf, Reflector.ModelWolf_mane) : null)))))));
        }
    }

    public String[] getModelRendererNames()
    {
        return new String[] {"head", "body", "leg1", "leg2", "leg3", "leg4", "tail", "mane"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = 我的手艺.得到我的手艺().getRenderManager();
        RenderWolf renderwolf = new RenderWolf(rendermanager, modelBase, shadowSize);
        return renderwolf;
    }
}
