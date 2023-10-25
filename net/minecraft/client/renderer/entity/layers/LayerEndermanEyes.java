package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.src.Config;
import net.minecraft.util.图像位置;
import net.optifine.shaders.Shaders;

public class LayerEndermanEyes implements LayerRenderer<EntityEnderman>
{
    private static final 图像位置 RES_ENDERMAN_EYES = new 图像位置("textures/entity/enderman/enderman_eyes.png");
    private final RenderEnderman endermanRenderer;

    public LayerEndermanEyes(RenderEnderman endermanRendererIn)
    {
        this.endermanRenderer = endermanRendererIn;
    }

    public void doRenderLayer(EntityEnderman entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        this.endermanRenderer.bindTexture(RES_ENDERMAN_EYES);
        光照状态经理.启用混合品();
        光照状态经理.禁用希腊字母表的第1个字母();
        光照状态经理.正常工作(1, 1);
        光照状态经理.disableLighting();
        光照状态经理.depthMask(!entitylivingbaseIn.isInvisible());
        int i = 61680;
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
        光照状态经理.enableLighting();
        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);

        if (Config.isShaders())
        {
            Shaders.beginSpiderEyes();
        }

        Config.getRenderGlobal().renderOverlayEyes = true;
        this.endermanRenderer.getMainModel().render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
        Config.getRenderGlobal().renderOverlayEyes = false;

        if (Config.isShaders())
        {
            Shaders.endSpiderEyes();
        }

        this.endermanRenderer.setLightmap(entitylivingbaseIn, partialTicks);
        光照状态经理.depthMask(true);
        光照状态经理.禁用混合品();
        光照状态经理.启用希腊字母表的第1个字母();
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
