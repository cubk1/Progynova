package net.optifine.shaders.uniform;

import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.optifine.expr.ExpressionType;
import net.optifine.expr.IExpressionBool;

public enum ShaderParameterBool implements IExpressionBool
{
    IS_ALIVE("is_alive"),
    IS_BURNING("is_burning"),
    IS_CHILD("is_child"),
    IS_GLOWING("is_glowing"),
    IS_HURT("is_hurt"),
    IS_IN_LAVA("is_in_lava"),
    IS_IN_WATER("is_in_water"),
    IS_INVISIBLE("is_invisible"),
    IS_ON_GROUND("is_on_ground"),
    IS_RIDDEN("is_ridden"),
    IS_RIDING("is_riding"),
    IS_SNEAKING("is_sneaking"),
    IS_SPRINTING("is_sprinting"),
    IS_WET("is_wet");

    private String name;
    private RenderManager renderManager;
    private static final ShaderParameterBool[] VALUES = values();

    private ShaderParameterBool(String name)
    {
        this.name = name;
        this.renderManager = 我的手艺.得到我的手艺().getRenderManager();
    }

    public String getName()
    {
        return this.name;
    }

    public ExpressionType getExpressionType()
    {
        return ExpressionType.BOOL;
    }

    public boolean eval()
    {
        实体 实体 = 我的手艺.得到我的手艺().getRenderViewEntity();

        if (实体 instanceof 实体LivingBase)
        {
            实体LivingBase entitylivingbase = (实体LivingBase) 实体;

            switch (this)
            {
                case IS_ALIVE:
                    return entitylivingbase.isEntityAlive();

                case IS_BURNING:
                    return entitylivingbase.isBurning();

                case IS_CHILD:
                    return entitylivingbase.isChild();

                case IS_HURT:
                    return entitylivingbase.hurtTime > 0;

                case IS_IN_LAVA:
                    return entitylivingbase.isInLava();

                case IS_IN_WATER:
                    return entitylivingbase.isInWater();

                case IS_INVISIBLE:
                    return entitylivingbase.isInvisible();

                case IS_ON_GROUND:
                    return entitylivingbase.onGround;

                case IS_RIDDEN:
                    return entitylivingbase.riddenBy实体 != null;

                case IS_RIDING:
                    return entitylivingbase.isRiding();

                case IS_SNEAKING:
                    return entitylivingbase.正在下蹲();

                case IS_SPRINTING:
                    return entitylivingbase.isSprinting();

                case IS_WET:
                    return entitylivingbase.isWet();
            }
        }

        return false;
    }

    public static ShaderParameterBool parse(String str)
    {
        if (str == null)
        {
            return null;
        }
        else
        {
            for (int i = 0; i < VALUES.length; ++i)
            {
                ShaderParameterBool shaderparameterbool = VALUES[i];

                if (shaderparameterbool.getName().equals(str))
                {
                    return shaderparameterbool;
                }
            }

            return null;
        }
    }
}
