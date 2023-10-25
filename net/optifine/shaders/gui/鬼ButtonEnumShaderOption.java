package net.optifine.shaders.gui;

import net.minecraft.client.gui.鬼Button;
import net.minecraft.client.resources.I18n;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.config.EnumShaderOption;

public class 鬼ButtonEnumShaderOption extends 鬼Button
{
    private EnumShaderOption enumShaderOption = null;

    public 鬼ButtonEnumShaderOption(EnumShaderOption enumShaderOption, int x, int y, int widthIn, int heightIn)
    {
        super(enumShaderOption.ordinal(), x, y, widthIn, heightIn, getButtonText(enumShaderOption));
        this.enumShaderOption = enumShaderOption;
    }

    public EnumShaderOption getEnumShaderOption()
    {
        return this.enumShaderOption;
    }

    private static String getButtonText(EnumShaderOption eso)
    {
        String s = I18n.format(eso.getResourceKey(), new Object[0]) + ": ";

        switch (eso)
        {
            case ANTIALIASING:
                return s + 鬼Shaders.toStringAa(Shaders.configAntialiasingLevel);

            case NORMAL_MAP:
                return s + 鬼Shaders.toStringOnOff(Shaders.configNormalMap);

            case SPECULAR_MAP:
                return s + 鬼Shaders.toStringOnOff(Shaders.configSpecularMap);

            case RENDER_RES_MUL:
                return s + 鬼Shaders.toStringQuality(Shaders.configRenderResMul);

            case SHADOW_RES_MUL:
                return s + 鬼Shaders.toStringQuality(Shaders.configShadowResMul);

            case HAND_DEPTH_MUL:
                return s + 鬼Shaders.toStringHandDepth(Shaders.configHandDepthMul);

            case CLOUD_SHADOW:
                return s + 鬼Shaders.toStringOnOff(Shaders.configCloudShadow);

            case OLD_HAND_LIGHT:
                return s + Shaders.configOldHandLight.getUserValue();

            case OLD_LIGHTING:
                return s + Shaders.configOldLighting.getUserValue();

            case SHADOW_CLIP_FRUSTRUM:
                return s + 鬼Shaders.toStringOnOff(Shaders.configShadowClipFrustrum);

            case TWEAK_BLOCK_DAMAGE:
                return s + 鬼Shaders.toStringOnOff(Shaders.configTweakBlockDamage);

            default:
                return s + Shaders.getEnumShaderOption(eso);
        }
    }

    public void updateButtonText()
    {
        this.displayString = getButtonText(this.enumShaderOption);
    }
}
