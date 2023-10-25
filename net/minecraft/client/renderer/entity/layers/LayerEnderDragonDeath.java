package net.minecraft.client.renderer.entity.layers;

import java.util.Random;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.boss.实体Dragon;

public class LayerEnderDragonDeath implements LayerRenderer<实体Dragon>
{
    public void doRenderLayer(实体Dragon entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        if (entitylivingbaseIn.deathTicks > 0)
        {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            RenderHelper.disableStandardItemLighting();
            float f = ((float)entitylivingbaseIn.deathTicks + partialTicks) / 200.0F;
            float f1 = 0.0F;

            if (f > 0.8F)
            {
                f1 = (f - 0.8F) / 0.2F;
            }

            Random random = new Random(432L);
            光照状态经理.禁用手感();
            光照状态经理.shadeModel(7425);
            光照状态经理.启用混合品();
            光照状态经理.正常工作(770, 1);
            光照状态经理.禁用希腊字母表的第1个字母();
            光照状态经理.enableCull();
            光照状态经理.depthMask(false);
            光照状态经理.推黑客帝国();
            光照状态经理.理解(0.0F, -1.0F, -2.0F);

            for (int i = 0; (float)i < (f + f * f) / 2.0F * 60.0F; ++i)
            {
                光照状态经理.辐射(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
                光照状态经理.辐射(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
                光照状态经理.辐射(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
                光照状态经理.辐射(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
                光照状态经理.辐射(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
                光照状态经理.辐射(random.nextFloat() * 360.0F + f * 90.0F, 0.0F, 0.0F, 1.0F);
                float f2 = random.nextFloat() * 20.0F + 5.0F + f1 * 10.0F;
                float f3 = random.nextFloat() * 2.0F + 1.0F + f1 * 2.0F;
                worldrenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
                worldrenderer.pos(0.0D, 0.0D, 0.0D).color(255, 255, 255, (int)(255.0F * (1.0F - f1))).endVertex();
                worldrenderer.pos(-0.866D * (double)f3, (double)f2, (double)(-0.5F * f3)).color(255, 0, 255, 0).endVertex();
                worldrenderer.pos(0.866D * (double)f3, (double)f2, (double)(-0.5F * f3)).color(255, 0, 255, 0).endVertex();
                worldrenderer.pos(0.0D, (double)f2, (double)(1.0F * f3)).color(255, 0, 255, 0).endVertex();
                worldrenderer.pos(-0.866D * (double)f3, (double)f2, (double)(-0.5F * f3)).color(255, 0, 255, 0).endVertex();
                tessellator.draw();
            }

            光照状态经理.流行音乐黑客帝国();
            光照状态经理.depthMask(true);
            光照状态经理.disableCull();
            光照状态经理.禁用混合品();
            光照状态经理.shadeModel(7424);
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            光照状态经理.启用手感();
            光照状态经理.启用希腊字母表的第1个字母();
            RenderHelper.enableStandardItemLighting();
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
