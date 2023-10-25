package net.optifine.entity.model;

import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderCaveSpider;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.实体CaveSpider;

public class ModelAdapterCaveSpider extends ModelAdapterSpider
{
    public ModelAdapterCaveSpider()
    {
        super(实体CaveSpider.class, "cave_spider", 0.7F);
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = 我的手艺.得到我的手艺().getRenderManager();
        RenderCaveSpider rendercavespider = new RenderCaveSpider(rendermanager);
        rendercavespider.mainModel = modelBase;
        rendercavespider.shadowSize = shadowSize;
        return rendercavespider;
    }
}
