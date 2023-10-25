package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.entity.passive.实体Horse;
import net.minecraft.util.图像位置;

public class RenderHorse extends RenderLiving<实体Horse>
{
    private static final Map<String, 图像位置> field_110852_a = Maps.<String, 图像位置>newHashMap();
    private static final 图像位置 whiteHorseTextures = new 图像位置("textures/entity/horse/horse_white.png");
    private static final 图像位置 muleTextures = new 图像位置("textures/entity/horse/mule.png");
    private static final 图像位置 donkeyTextures = new 图像位置("textures/entity/horse/donkey.png");
    private static final 图像位置 zombieHorseTextures = new 图像位置("textures/entity/horse/horse_zombie.png");
    private static final 图像位置 skeletonHorseTextures = new 图像位置("textures/entity/horse/horse_skeleton.png");

    public RenderHorse(RenderManager rendermanagerIn, ModelHorse model, float shadowSizeIn)
    {
        super(rendermanagerIn, model, shadowSizeIn);
    }

    protected void preRenderCallback(实体Horse entitylivingbaseIn, float partialTickTime)
    {
        float f = 1.0F;
        int i = entitylivingbaseIn.getHorseType();

        if (i == 1)
        {
            f *= 0.87F;
        }
        else if (i == 2)
        {
            f *= 0.92F;
        }

        光照状态经理.障眼物(f, f, f);
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }

    protected 图像位置 getEntityTexture(实体Horse entity)
    {
        if (!entity.func_110239_cn())
        {
            switch (entity.getHorseType())
            {
                case 0:
                default:
                    return whiteHorseTextures;

                case 1:
                    return donkeyTextures;

                case 2:
                    return muleTextures;

                case 3:
                    return zombieHorseTextures;

                case 4:
                    return skeletonHorseTextures;
            }
        }
        else
        {
            return this.func_110848_b(entity);
        }
    }

    private 图像位置 func_110848_b(实体Horse horse)
    {
        String s = horse.getHorseTexture();

        if (!horse.func_175507_cI())
        {
            return null;
        }
        else
        {
            图像位置 resourcelocation = (图像位置)field_110852_a.get(s);

            if (resourcelocation == null)
            {
                resourcelocation = new 图像位置(s);
                我的手艺.得到我的手艺().得到手感经理().loadTexture(resourcelocation, new LayeredTexture(horse.getVariantTexturePaths()));
                field_110852_a.put(s, resourcelocation);
            }

            return resourcelocation;
        }
    }
}
