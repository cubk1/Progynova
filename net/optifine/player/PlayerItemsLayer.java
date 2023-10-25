package net.optifine.player;

import java.util.Map;
import java.util.Set;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.src.Config;

public class PlayerItemsLayer implements LayerRenderer
{
    private RenderPlayer renderPlayer = null;

    public PlayerItemsLayer(RenderPlayer renderPlayer)
    {
        this.renderPlayer = renderPlayer;
    }

    public void doRenderLayer(实体LivingBase entityLiving, float limbSwing, float limbSwingAmount, float partialTicks, float ticksExisted, float headYaw, float rotationPitch, float scale)
    {
        this.renderEquippedItems(entityLiving, scale, partialTicks);
    }

    protected void renderEquippedItems(实体LivingBase entityLiving, float scale, float partialTicks)
    {
        if (Config.isShowCapes())
        {
            if (!entityLiving.isInvisible())
            {
                if (entityLiving instanceof AbstractClientPlayer)
                {
                    AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)entityLiving;
                    光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
                    光照状态经理.disableRescaleNormal();
                    光照状态经理.enableCull();
                    ModelBiped modelbiped = this.renderPlayer.getMainModel();
                    PlayerConfigurations.renderPlayerItems(modelbiped, abstractclientplayer, scale, partialTicks);
                    光照状态经理.disableCull();
                }
            }
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }

    public static void register(Map renderPlayerMap)
    {
        Set set = renderPlayerMap.keySet();
        boolean flag = false;

        for (Object object : set)
        {
            Object object1 = renderPlayerMap.get(object);

            if (object1 instanceof RenderPlayer)
            {
                RenderPlayer renderplayer = (RenderPlayer)object1;
                renderplayer.addLayer(new PlayerItemsLayer(renderplayer));
                flag = true;
            }
        }

        if (!flag)
        {
            Config.warn("PlayerItemsLayer not registered");
        }
    }
}
