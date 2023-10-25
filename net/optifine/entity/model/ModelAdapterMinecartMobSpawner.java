package net.optifine.entity.model;

import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderMinecartMobSpawner;
import net.minecraft.entity.ai.实体MinecartMobSpawner;
import net.minecraft.src.Config;
import net.optifine.reflect.Reflector;

public class ModelAdapterMinecartMobSpawner extends ModelAdapterMinecart
{
    public ModelAdapterMinecartMobSpawner()
    {
        super(实体MinecartMobSpawner.class, "spawner_minecart", 0.5F);
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = 我的手艺.得到我的手艺().getRenderManager();
        RenderMinecartMobSpawner renderminecartmobspawner = new RenderMinecartMobSpawner(rendermanager);

        if (!Reflector.RenderMinecart_modelMinecart.exists())
        {
            Config.warn("Field not found: RenderMinecart.modelMinecart");
            return null;
        }
        else
        {
            Reflector.setFieldValue(renderminecartmobspawner, Reflector.RenderMinecart_modelMinecart, modelBase);
            renderminecartmobspawner.shadowSize = shadowSize;
            return renderminecartmobspawner;
        }
    }
}
