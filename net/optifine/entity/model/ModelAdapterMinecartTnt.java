package net.optifine.entity.model;

import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderTntMinecart;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;

public class ModelAdapterMinecartTnt extends ModelAdapterMinecart
{
    public ModelAdapterMinecartTnt()
    {
        super(EntityMinecartTNT.class, "tnt_minecart", 0.5F);
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = 我的手艺.得到我的手艺().getRenderManager();
        RenderTntMinecart rendertntminecart = new RenderTntMinecart(rendermanager);

        if (!Reflector.RenderMinecart_modelMinecart.exists())
        {
            Config.warn("Field not found: RenderMinecart.modelMinecart");
            return null;
        }
        else
        {
            Reflector.setFieldValue(rendertntminecart, Reflector.RenderMinecart_modelMinecart, modelBase);
            rendertntminecart.shadowSize = shadowSize;
            return rendertntminecart;
        }
    }
}
