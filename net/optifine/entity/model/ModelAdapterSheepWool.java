package net.optifine.entity.model;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerSheepWool;
import net.minecraft.entity.passive.实体Sheep;
import net.minecraft.src.Config;

public class ModelAdapterSheepWool extends ModelAdapterQuadruped
{
    public ModelAdapterSheepWool()
    {
        super(实体Sheep.class, "sheep_wool", 0.7F);
    }

    public ModelBase makeModel()
    {
        return new ModelSheep1();
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = 我的手艺.得到我的手艺().getRenderManager();
        Render render = (Render)rendermanager.getEntityRenderMap().get(实体Sheep.class);

        if (!(render instanceof RenderSheep))
        {
            Config.warn("Not a RenderSheep: " + render);
            return null;
        }
        else
        {
            if (render.getEntityClass() == null)
            {
                render = new RenderSheep(rendermanager, new ModelSheep2(), 0.7F);
            }

            RenderSheep rendersheep = (RenderSheep)render;
            List<LayerRenderer<实体Sheep>> list = rendersheep.getLayerRenderers();
            Iterator iterator = list.iterator();

            while (iterator.hasNext())
            {
                LayerRenderer layerrenderer = (LayerRenderer)iterator.next();

                if (layerrenderer instanceof LayerSheepWool)
                {
                    iterator.remove();
                }
            }

            LayerSheepWool layersheepwool = new LayerSheepWool(rendersheep);
            layersheepwool.sheepModel = (ModelSheep1)modelBase;
            rendersheep.addLayer(layersheepwool);
            return rendersheep;
        }
    }
}
