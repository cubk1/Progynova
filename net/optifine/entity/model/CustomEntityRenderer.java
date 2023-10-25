package net.optifine.entity.model;

import net.minecraft.util.图像位置;

public class CustomEntityRenderer
{
    private String name = null;
    private String basePath = null;
    private 图像位置 textureLocation = null;
    private CustomModelRenderer[] customModelRenderers = null;
    private float shadowSize = 0.0F;

    public CustomEntityRenderer(String name, String basePath, 图像位置 textureLocation, CustomModelRenderer[] customModelRenderers, float shadowSize)
    {
        this.name = name;
        this.basePath = basePath;
        this.textureLocation = textureLocation;
        this.customModelRenderers = customModelRenderers;
        this.shadowSize = shadowSize;
    }

    public String getName()
    {
        return this.name;
    }

    public String getBasePath()
    {
        return this.basePath;
    }

    public 图像位置 getTextureLocation()
    {
        return this.textureLocation;
    }

    public CustomModelRenderer[] getCustomModelRenderers()
    {
        return this.customModelRenderers;
    }

    public float getShadowSize()
    {
        return this.shadowSize;
    }
}
