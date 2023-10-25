package net.minecraft.client.renderer.tileentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;

public class TileEntityBannerRenderer extends TileEntitySpecialRenderer<TileEntityBanner>
{
    private static final Map<String, TileEntityBannerRenderer.TimedBannerTexture> DESIGNS = Maps.<String, TileEntityBannerRenderer.TimedBannerTexture>newHashMap();
    private static final 图像位置 BANNERTEXTURES = new 图像位置("textures/entity/banner_base.png");
    private ModelBanner bannerModel = new ModelBanner();

    public void renderTileEntityAt(TileEntityBanner te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        boolean flag = te.getWorld() != null;
        boolean flag1 = !flag || te.getBlockType() == Blocks.standing_banner;
        int i = flag ? te.getBlockMetadata() : 0;
        long j = flag ? te.getWorld().getTotalWorldTime() : 0L;
        光照状态经理.推黑客帝国();
        float f = 0.6666667F;

        if (flag1)
        {
            光照状态经理.理解((float)x + 0.5F, (float)y + 0.75F * f, (float)z + 0.5F);
            float f1 = (float)(i * 360) / 16.0F;
            光照状态经理.辐射(-f1, 0.0F, 1.0F, 0.0F);
            this.bannerModel.bannerStand.showModel = true;
        }
        else
        {
            float f2 = 0.0F;

            if (i == 2)
            {
                f2 = 180.0F;
            }

            if (i == 4)
            {
                f2 = 90.0F;
            }

            if (i == 5)
            {
                f2 = -90.0F;
            }

            光照状态经理.理解((float)x + 0.5F, (float)y - 0.25F * f, (float)z + 0.5F);
            光照状态经理.辐射(-f2, 0.0F, 1.0F, 0.0F);
            光照状态经理.理解(0.0F, -0.3125F, -0.4375F);
            this.bannerModel.bannerStand.showModel = false;
        }

        BlockPos blockpos = te.getPos();
        float f3 = (float)(blockpos.getX() * 7 + blockpos.getY() * 9 + blockpos.getZ() * 13) + (float)j + partialTicks;
        this.bannerModel.bannerSlate.rotateAngleX = (-0.0125F + 0.01F * MathHelper.cos(f3 * (float)Math.PI * 0.02F)) * (float)Math.PI;
        光照状态经理.enableRescaleNormal();
        图像位置 resourcelocation = this.func_178463_a(te);

        if (resourcelocation != null)
        {
            this.bindTexture(resourcelocation);
            光照状态经理.推黑客帝国();
            光照状态经理.障眼物(f, -f, -f);
            this.bannerModel.renderBanner();
            光照状态经理.流行音乐黑客帝国();
        }

        光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
        光照状态经理.流行音乐黑客帝国();
    }

    private 图像位置 func_178463_a(TileEntityBanner bannerObj)
    {
        String s = bannerObj.getPatternResourceLocation();

        if (s.isEmpty())
        {
            return null;
        }
        else
        {
            TileEntityBannerRenderer.TimedBannerTexture tileentitybannerrenderer$timedbannertexture = (TileEntityBannerRenderer.TimedBannerTexture)DESIGNS.get(s);

            if (tileentitybannerrenderer$timedbannertexture == null)
            {
                if (DESIGNS.size() >= 256)
                {
                    long i = System.currentTimeMillis();
                    Iterator<String> iterator = DESIGNS.keySet().iterator();

                    while (iterator.hasNext())
                    {
                        String s1 = (String)iterator.next();
                        TileEntityBannerRenderer.TimedBannerTexture tileentitybannerrenderer$timedbannertexture1 = (TileEntityBannerRenderer.TimedBannerTexture)DESIGNS.get(s1);

                        if (i - tileentitybannerrenderer$timedbannertexture1.systemTime > 60000L)
                        {
                            我的手艺.得到我的手艺().得到手感经理().deleteTexture(tileentitybannerrenderer$timedbannertexture1.bannerTexture);
                            iterator.remove();
                        }
                    }

                    if (DESIGNS.size() >= 256)
                    {
                        return null;
                    }
                }

                List<TileEntityBanner.EnumBannerPattern> list1 = bannerObj.getPatternList();
                List<EnumDyeColor> list = bannerObj.getColorList();
                List<String> list2 = Lists.<String>newArrayList();

                for (TileEntityBanner.EnumBannerPattern tileentitybanner$enumbannerpattern : list1)
                {
                    list2.add("textures/entity/banner/" + tileentitybanner$enumbannerpattern.getPatternName() + ".png");
                }

                tileentitybannerrenderer$timedbannertexture = new TileEntityBannerRenderer.TimedBannerTexture();
                tileentitybannerrenderer$timedbannertexture.bannerTexture = new 图像位置(s);
                我的手艺.得到我的手艺().得到手感经理().loadTexture(tileentitybannerrenderer$timedbannertexture.bannerTexture, new LayeredColorMaskTexture(BANNERTEXTURES, list2, list));
                DESIGNS.put(s, tileentitybannerrenderer$timedbannertexture);
            }

            tileentitybannerrenderer$timedbannertexture.systemTime = System.currentTimeMillis();
            return tileentitybannerrenderer$timedbannertexture.bannerTexture;
        }
    }

    static class TimedBannerTexture
    {
        public long systemTime;
        public 图像位置 bannerTexture;

        private TimedBannerTexture()
        {
        }
    }
}
