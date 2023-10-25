package net.minecraft.client.audio;

import net.minecraft.entity.item.实体Minecart;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;

public class MovingSoundMinecart extends MovingSound
{
    private final 实体Minecart minecart;
    private float distance = 0.0F;

    public MovingSoundMinecart(实体Minecart minecartIn)
    {
        super(new 图像位置("minecraft:minecart.base"));
        this.minecart = minecartIn;
        this.repeat = true;
        this.repeatDelay = 0;
    }

    public void update()
    {
        if (this.minecart.isDead)
        {
            this.donePlaying = true;
        }
        else
        {
            this.xPosF = (float)this.minecart.X坐标;
            this.yPosF = (float)this.minecart.Y坐标;
            this.zPosF = (float)this.minecart.Z坐标;
            float f = MathHelper.sqrt_double(this.minecart.通便X * this.minecart.通便X + this.minecart.通便Z * this.minecart.通便Z);

            if ((double)f >= 0.01D)
            {
                this.distance = MathHelper.clamp_float(this.distance + 0.0025F, 0.0F, 1.0F);
                this.volume = 0.0F + MathHelper.clamp_float(f, 0.0F, 0.5F) * 0.7F;
            }
            else
            {
                this.distance = 0.0F;
                this.volume = 0.0F;
            }
        }
    }
}
