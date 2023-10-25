package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderIronGolem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.monster.实体IronGolem;
import net.minecraft.init.Blocks;

public class LayerIronGolemFlower implements LayerRenderer<实体IronGolem>
{
    private final RenderIronGolem ironGolemRenderer;

    public LayerIronGolemFlower(RenderIronGolem ironGolemRendererIn)
    {
        this.ironGolemRenderer = ironGolemRendererIn;
    }

    public void doRenderLayer(实体IronGolem entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        if (entitylivingbaseIn.getHoldRoseTick() != 0)
        {
            BlockRendererDispatcher blockrendererdispatcher = 我的手艺.得到我的手艺().getBlockRendererDispatcher();
            光照状态经理.enableRescaleNormal();
            光照状态经理.推黑客帝国();
            光照状态经理.辐射(5.0F + 180.0F * ((ModelIronGolem)this.ironGolemRenderer.getMainModel()).ironGolemRightArm.rotateAngleX / (float)Math.PI, 1.0F, 0.0F, 0.0F);
            光照状态经理.辐射(90.0F, 1.0F, 0.0F, 0.0F);
            光照状态经理.理解(-0.9375F, -0.625F, -0.9375F);
            float f = 0.5F;
            光照状态经理.障眼物(f, -f, f);
            int i = entitylivingbaseIn.getBrightnessForRender(partialTicks);
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            this.ironGolemRenderer.bindTexture(TextureMap.locationBlocksTexture);
            blockrendererdispatcher.renderBlockBrightness(Blocks.red_flower.getDefaultState(), 1.0F);
            光照状态经理.流行音乐黑客帝国();
            光照状态经理.disableRescaleNormal();
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
