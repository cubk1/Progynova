package net.optifine.entity.model;

import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.entity.monster.EntitySkeleton;

public class ModelAdapterSkeleton extends ModelAdapterBiped
{
    public ModelAdapterSkeleton()
    {
        super(EntitySkeleton.class, "skeleton", 0.7F);
    }

    public ModelBase makeModel()
    {
        return new ModelSkeleton();
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = 我的手艺.得到我的手艺().getRenderManager();
        RenderSkeleton renderskeleton = new RenderSkeleton(rendermanager);
        Render.setModelBipedMain(renderskeleton, (ModelBiped)modelBase);
        renderskeleton.mainModel = modelBase;
        renderskeleton.shadowSize = shadowSize;
        return renderskeleton;
    }
}
