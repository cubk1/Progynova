package net.minecraft.client.gui;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.图像位置;
import net.minecraft.util.Vec4b;
import net.minecraft.world.storage.MapData;

public class MapItemRenderer
{
    private static final 图像位置 mapIcons = new 图像位置("textures/map/map_icons.png");
    private final TextureManager textureManager;
    private final Map<String, MapItemRenderer.Instance> loadedMaps = Maps.<String, MapItemRenderer.Instance>newHashMap();

    public MapItemRenderer(TextureManager textureManagerIn)
    {
        this.textureManager = textureManagerIn;
    }

    public void updateMapTexture(MapData mapdataIn)
    {
        this.getMapRendererInstance(mapdataIn).updateMapTexture();
    }

    public void renderMap(MapData mapdataIn, boolean p_148250_2_)
    {
        this.getMapRendererInstance(mapdataIn).render(p_148250_2_);
    }

    private MapItemRenderer.Instance getMapRendererInstance(MapData mapdataIn)
    {
        MapItemRenderer.Instance mapitemrenderer$instance = (MapItemRenderer.Instance)this.loadedMaps.get(mapdataIn.mapName);

        if (mapitemrenderer$instance == null)
        {
            mapitemrenderer$instance = new MapItemRenderer.Instance(mapdataIn);
            this.loadedMaps.put(mapdataIn.mapName, mapitemrenderer$instance);
        }

        return mapitemrenderer$instance;
    }

    public void clearLoadedMaps()
    {
        for (MapItemRenderer.Instance mapitemrenderer$instance : this.loadedMaps.values())
        {
            this.textureManager.deleteTexture(mapitemrenderer$instance.location);
        }

        this.loadedMaps.clear();
    }

    class Instance
    {
        private final MapData mapData;
        private final DynamicTexture mapTexture;
        private final 图像位置 location;
        private final int[] mapTextureData;

        private Instance(MapData mapdataIn)
        {
            this.mapData = mapdataIn;
            this.mapTexture = new DynamicTexture(128, 128);
            this.mapTextureData = this.mapTexture.getTextureData();
            this.location = MapItemRenderer.this.textureManager.getDynamicTextureLocation("map/" + mapdataIn.mapName, this.mapTexture);

            for (int i = 0; i < this.mapTextureData.length; ++i)
            {
                this.mapTextureData[i] = 0;
            }
        }

        private void updateMapTexture()
        {
            for (int i = 0; i < 16384; ++i)
            {
                int j = this.mapData.colors[i] & 255;

                if (j / 4 == 0)
                {
                    this.mapTextureData[i] = (i + i / 128 & 1) * 8 + 16 << 24;
                }
                else
                {
                    this.mapTextureData[i] = MapColor.mapColorArray[j / 4].getMapColor(j & 3);
                }
            }

            this.mapTexture.updateDynamicTexture();
        }

        private void render(boolean noOverlayRendering)
        {
            int i = 0;
            int j = 0;
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            float f = 0.0F;
            MapItemRenderer.this.textureManager.绑定手感(this.location);
            光照状态经理.启用混合品();
            光照状态经理.tryBlendFuncSeparate(1, 771, 0, 1);
            光照状态经理.禁用希腊字母表的第1个字母();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos((double)((float)(i + 0) + f), (double)((float)(j + 128) - f), -0.009999999776482582D).tex(0.0D, 1.0D).endVertex();
            worldrenderer.pos((double)((float)(i + 128) - f), (double)((float)(j + 128) - f), -0.009999999776482582D).tex(1.0D, 1.0D).endVertex();
            worldrenderer.pos((double)((float)(i + 128) - f), (double)((float)(j + 0) + f), -0.009999999776482582D).tex(1.0D, 0.0D).endVertex();
            worldrenderer.pos((double)((float)(i + 0) + f), (double)((float)(j + 0) + f), -0.009999999776482582D).tex(0.0D, 0.0D).endVertex();
            tessellator.draw();
            光照状态经理.启用希腊字母表的第1个字母();
            光照状态经理.禁用混合品();
            MapItemRenderer.this.textureManager.绑定手感(MapItemRenderer.mapIcons);
            int k = 0;

            for (Vec4b vec4b : this.mapData.mapDecorations.values())
            {
                if (!noOverlayRendering || vec4b.func_176110_a() == 1)
                {
                    光照状态经理.推黑客帝国();
                    光照状态经理.理解((float)i + (float)vec4b.func_176112_b() / 2.0F + 64.0F, (float)j + (float)vec4b.func_176113_c() / 2.0F + 64.0F, -0.02F);
                    光照状态经理.辐射((float)(vec4b.func_176111_d() * 360) / 16.0F, 0.0F, 0.0F, 1.0F);
                    光照状态经理.障眼物(4.0F, 4.0F, 3.0F);
                    光照状态经理.理解(-0.125F, 0.125F, 0.0F);
                    byte b0 = vec4b.func_176110_a();
                    float f1 = (float)(b0 % 4 + 0) / 4.0F;
                    float f2 = (float)(b0 / 4 + 0) / 4.0F;
                    float f3 = (float)(b0 % 4 + 1) / 4.0F;
                    float f4 = (float)(b0 / 4 + 1) / 4.0F;
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
                    float f5 = -0.001F;
                    worldrenderer.pos(-1.0D, 1.0D, (double)((float)k * -0.001F)).tex((double)f1, (double)f2).endVertex();
                    worldrenderer.pos(1.0D, 1.0D, (double)((float)k * -0.001F)).tex((double)f3, (double)f2).endVertex();
                    worldrenderer.pos(1.0D, -1.0D, (double)((float)k * -0.001F)).tex((double)f3, (double)f4).endVertex();
                    worldrenderer.pos(-1.0D, -1.0D, (double)((float)k * -0.001F)).tex((double)f1, (double)f4).endVertex();
                    tessellator.draw();
                    光照状态经理.流行音乐黑客帝国();
                    ++k;
                }
            }

            光照状态经理.推黑客帝国();
            光照状态经理.理解(0.0F, 0.0F, -0.04F);
            光照状态经理.障眼物(1.0F, 1.0F, 1.0F);
            光照状态经理.流行音乐黑客帝国();
        }
    }
}
