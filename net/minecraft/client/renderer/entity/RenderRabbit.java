package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.passive.实体Rabbit;
import net.minecraft.util.枚举聊天格式;
import net.minecraft.util.图像位置;

public class RenderRabbit extends RenderLiving<实体Rabbit>
{
    private static final 图像位置 BROWN = new 图像位置("textures/entity/rabbit/brown.png");
    private static final 图像位置 WHITE = new 图像位置("textures/entity/rabbit/white.png");
    private static final 图像位置 BLACK = new 图像位置("textures/entity/rabbit/black.png");
    private static final 图像位置 GOLD = new 图像位置("textures/entity/rabbit/gold.png");
    private static final 图像位置 SALT = new 图像位置("textures/entity/rabbit/salt.png");
    private static final 图像位置 WHITE_SPLOTCHED = new 图像位置("textures/entity/rabbit/white_splotched.png");
    private static final 图像位置 TOAST = new 图像位置("textures/entity/rabbit/toast.png");
    private static final 图像位置 CAERBANNOG = new 图像位置("textures/entity/rabbit/caerbannog.png");

    public RenderRabbit(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    protected 图像位置 getEntityTexture(实体Rabbit entity)
    {
        String s = 枚举聊天格式.getTextWithoutFormattingCodes(entity.getName());

        if (s != null && s.equals("Toast"))
        {
            return TOAST;
        }
        else
        {
            switch (entity.getRabbitType())
            {
                case 0:
                default:
                    return BROWN;

                case 1:
                    return WHITE;

                case 2:
                    return BLACK;

                case 3:
                    return WHITE_SPLOTCHED;

                case 4:
                    return GOLD;

                case 5:
                    return SALT;

                case 99:
                    return CAERBANNOG;
            }
        }
    }
}
