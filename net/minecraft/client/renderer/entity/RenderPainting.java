package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.实体Painting;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;

public class RenderPainting extends Render<实体Painting>
{
    private static final 图像位置 KRISTOFFER_PAINTING_TEXTURE = new 图像位置("textures/painting/paintings_kristoffer_zetterstrand.png");

    public RenderPainting(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    public void doRender(实体Painting entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        光照状态经理.推黑客帝国();
        光照状态经理.理解(x, y, z);
        光照状态经理.辐射(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
        光照状态经理.enableRescaleNormal();
        this.bindEntityTexture(entity);
        实体Painting.EnumArt entitypainting$enumart = entity.art;
        float f = 0.0625F;
        光照状态经理.障眼物(f, f, f);
        this.renderPainting(entity, entitypainting$enumart.sizeX, entitypainting$enumart.sizeY, entitypainting$enumart.offsetX, entitypainting$enumart.offsetY);
        光照状态经理.disableRescaleNormal();
        光照状态经理.流行音乐黑客帝国();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected 图像位置 getEntityTexture(实体Painting entity)
    {
        return KRISTOFFER_PAINTING_TEXTURE;
    }

    private void renderPainting(实体Painting painting, int width, int height, int textureU, int textureV)
    {
        float f = (float)(-width) / 2.0F;
        float f1 = (float)(-height) / 2.0F;
        float f2 = 0.5F;
        float f3 = 0.75F;
        float f4 = 0.8125F;
        float f5 = 0.0F;
        float f6 = 0.0625F;
        float f7 = 0.75F;
        float f8 = 0.8125F;
        float f9 = 0.001953125F;
        float f10 = 0.001953125F;
        float f11 = 0.7519531F;
        float f12 = 0.7519531F;
        float f13 = 0.0F;
        float f14 = 0.0625F;

        for (int i = 0; i < width / 16; ++i)
        {
            for (int j = 0; j < height / 16; ++j)
            {
                float f15 = f + (float)((i + 1) * 16);
                float f16 = f + (float)(i * 16);
                float f17 = f1 + (float)((j + 1) * 16);
                float f18 = f1 + (float)(j * 16);
                this.setLightmap(painting, (f15 + f16) / 2.0F, (f17 + f18) / 2.0F);
                float f19 = (float)(textureU + width - i * 16) / 256.0F;
                float f20 = (float)(textureU + width - (i + 1) * 16) / 256.0F;
                float f21 = (float)(textureV + height - j * 16) / 256.0F;
                float f22 = (float)(textureV + height - (j + 1) * 16) / 256.0F;
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
                worldrenderer.pos((double)f15, (double)f18, (double)(-f2)).tex((double)f20, (double)f21).normal(0.0F, 0.0F, -1.0F).endVertex();
                worldrenderer.pos((double)f16, (double)f18, (double)(-f2)).tex((double)f19, (double)f21).normal(0.0F, 0.0F, -1.0F).endVertex();
                worldrenderer.pos((double)f16, (double)f17, (double)(-f2)).tex((double)f19, (double)f22).normal(0.0F, 0.0F, -1.0F).endVertex();
                worldrenderer.pos((double)f15, (double)f17, (double)(-f2)).tex((double)f20, (double)f22).normal(0.0F, 0.0F, -1.0F).endVertex();
                worldrenderer.pos((double)f15, (double)f17, (double)f2).tex((double)f3, (double)f5).normal(0.0F, 0.0F, 1.0F).endVertex();
                worldrenderer.pos((double)f16, (double)f17, (double)f2).tex((double)f4, (double)f5).normal(0.0F, 0.0F, 1.0F).endVertex();
                worldrenderer.pos((double)f16, (double)f18, (double)f2).tex((double)f4, (double)f6).normal(0.0F, 0.0F, 1.0F).endVertex();
                worldrenderer.pos((double)f15, (double)f18, (double)f2).tex((double)f3, (double)f6).normal(0.0F, 0.0F, 1.0F).endVertex();
                worldrenderer.pos((double)f15, (double)f17, (double)(-f2)).tex((double)f7, (double)f9).normal(0.0F, 1.0F, 0.0F).endVertex();
                worldrenderer.pos((double)f16, (double)f17, (double)(-f2)).tex((double)f8, (double)f9).normal(0.0F, 1.0F, 0.0F).endVertex();
                worldrenderer.pos((double)f16, (double)f17, (double)f2).tex((double)f8, (double)f10).normal(0.0F, 1.0F, 0.0F).endVertex();
                worldrenderer.pos((double)f15, (double)f17, (double)f2).tex((double)f7, (double)f10).normal(0.0F, 1.0F, 0.0F).endVertex();
                worldrenderer.pos((double)f15, (double)f18, (double)f2).tex((double)f7, (double)f9).normal(0.0F, -1.0F, 0.0F).endVertex();
                worldrenderer.pos((double)f16, (double)f18, (double)f2).tex((double)f8, (double)f9).normal(0.0F, -1.0F, 0.0F).endVertex();
                worldrenderer.pos((double)f16, (double)f18, (double)(-f2)).tex((double)f8, (double)f10).normal(0.0F, -1.0F, 0.0F).endVertex();
                worldrenderer.pos((double)f15, (double)f18, (double)(-f2)).tex((double)f7, (double)f10).normal(0.0F, -1.0F, 0.0F).endVertex();
                worldrenderer.pos((double)f15, (double)f17, (double)f2).tex((double)f12, (double)f13).normal(-1.0F, 0.0F, 0.0F).endVertex();
                worldrenderer.pos((double)f15, (double)f18, (double)f2).tex((double)f12, (double)f14).normal(-1.0F, 0.0F, 0.0F).endVertex();
                worldrenderer.pos((double)f15, (double)f18, (double)(-f2)).tex((double)f11, (double)f14).normal(-1.0F, 0.0F, 0.0F).endVertex();
                worldrenderer.pos((double)f15, (double)f17, (double)(-f2)).tex((double)f11, (double)f13).normal(-1.0F, 0.0F, 0.0F).endVertex();
                worldrenderer.pos((double)f16, (double)f17, (double)(-f2)).tex((double)f12, (double)f13).normal(1.0F, 0.0F, 0.0F).endVertex();
                worldrenderer.pos((double)f16, (double)f18, (double)(-f2)).tex((double)f12, (double)f14).normal(1.0F, 0.0F, 0.0F).endVertex();
                worldrenderer.pos((double)f16, (double)f18, (double)f2).tex((double)f11, (double)f14).normal(1.0F, 0.0F, 0.0F).endVertex();
                worldrenderer.pos((double)f16, (double)f17, (double)f2).tex((double)f11, (double)f13).normal(1.0F, 0.0F, 0.0F).endVertex();
                tessellator.draw();
            }
        }
    }

    private void setLightmap(实体Painting painting, float p_77008_2_, float p_77008_3_)
    {
        int i = MathHelper.floor_double(painting.X坐标);
        int j = MathHelper.floor_double(painting.Y坐标 + (double)(p_77008_3_ / 16.0F));
        int k = MathHelper.floor_double(painting.Z坐标);
        EnumFacing enumfacing = painting.facingDirection;

        if (enumfacing == EnumFacing.NORTH)
        {
            i = MathHelper.floor_double(painting.X坐标 + (double)(p_77008_2_ / 16.0F));
        }

        if (enumfacing == EnumFacing.WEST)
        {
            k = MathHelper.floor_double(painting.Z坐标 - (double)(p_77008_2_ / 16.0F));
        }

        if (enumfacing == EnumFacing.SOUTH)
        {
            i = MathHelper.floor_double(painting.X坐标 - (double)(p_77008_2_ / 16.0F));
        }

        if (enumfacing == EnumFacing.EAST)
        {
            k = MathHelper.floor_double(painting.Z坐标 + (double)(p_77008_2_ / 16.0F));
        }

        int l = this.renderManager.worldObj.getCombinedLight(new 阻止位置(i, j, k), 0);
        int i1 = l % 65536;
        int j1 = l / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)i1, (float)j1);
        光照状态经理.色彩(1.0F, 1.0F, 1.0F);
    }
}
