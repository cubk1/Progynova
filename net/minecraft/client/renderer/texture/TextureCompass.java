package net.minecraft.client.renderer.texture;

import net.minecraft.client.我的手艺;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TextureCompass extends TextureAtlasSprite
{
    public double currentAngle;
    public double angleDelta;
    public static String locationSprite;

    public TextureCompass(String iconName)
    {
        super(iconName);
        locationSprite = iconName;
    }

    public void updateAnimation()
    {
        我的手艺 宇轩的世界 = 我的手艺.得到我的手艺();

        if (宇轩的世界.宇轩の世界 != null && 宇轩的世界.宇轩游玩者 != null)
        {
            this.updateCompass(宇轩的世界.宇轩の世界, 宇轩的世界.宇轩游玩者.posX, 宇轩的世界.宇轩游玩者.posZ, (double) 宇轩的世界.宇轩游玩者.旋转侧滑, false, false);
        }
        else
        {
            this.updateCompass((World)null, 0.0D, 0.0D, 0.0D, true, false);
        }
    }

    public void updateCompass(World worldIn, double p_94241_2_, double p_94241_4_, double p_94241_6_, boolean p_94241_8_, boolean p_94241_9_)
    {
        if (!this.framesTextureData.isEmpty())
        {
            double d0 = 0.0D;

            if (worldIn != null && !p_94241_8_)
            {
                BlockPos blockpos = worldIn.getSpawnPoint();
                double d1 = (double)blockpos.getX() - p_94241_2_;
                double d2 = (double)blockpos.getZ() - p_94241_4_;
                p_94241_6_ = p_94241_6_ % 360.0D;
                d0 = -((p_94241_6_ - 90.0D) * Math.PI / 180.0D - Math.atan2(d2, d1));

                if (!worldIn.provider.isSurfaceWorld())
                {
                    d0 = Math.random() * Math.PI * 2.0D;
                }
            }

            if (p_94241_9_)
            {
                this.currentAngle = d0;
            }
            else
            {
                double d3;

                for (d3 = d0 - this.currentAngle; d3 < -Math.PI; d3 += (Math.PI * 2D))
                {
                    ;
                }

                while (d3 >= Math.PI)
                {
                    d3 -= (Math.PI * 2D);
                }

                d3 = MathHelper.clamp_double(d3, -1.0D, 1.0D);
                this.angleDelta += d3 * 0.1D;
                this.angleDelta *= 0.8D;
                this.currentAngle += this.angleDelta;
            }

            int i;

            for (i = (int)((this.currentAngle / (Math.PI * 2D) + 1.0D) * (double)this.framesTextureData.size()) % this.framesTextureData.size(); i < 0; i = (i + this.framesTextureData.size()) % this.framesTextureData.size())
            {
                ;
            }

            if (i != this.frameCounter)
            {
                this.frameCounter = i;
                TextureUtil.uploadTextureMipmap((int[][])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
            }
        }
    }
}