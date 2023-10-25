package net.minecraft.client.audio;

import net.minecraft.entity.item.实体Minecart;
import net.minecraft.entity.player.实体Player;
import net.minecraft.util.MathHelper;
import net.minecraft.util.图像位置;

public class MovingSoundMinecartRiding extends MovingSound
{
    private final 实体Player player;
    private final 实体Minecart minecart;

    public MovingSoundMinecartRiding(实体Player playerRiding, 实体Minecart minecart)
    {
        super(new 图像位置("minecraft:minecart.inside"));
        this.player = playerRiding;
        this.minecart = minecart;
        this.attenuationType = ISound.AttenuationType.NONE;
        this.repeat = true;
        this.repeatDelay = 0;
    }

    public void update()
    {
        if (!this.minecart.isDead && this.player.isRiding() && this.player.riding实体 == this.minecart)
        {
            float f = MathHelper.sqrt_double(this.minecart.通便X * this.minecart.通便X + this.minecart.通便Z * this.minecart.通便Z);

            if ((double)f >= 0.01D)
            {
                this.volume = 0.0F + MathHelper.clamp_float(f, 0.0F, 1.0F) * 0.75F;
            }
            else
            {
                this.volume = 0.0F;
            }
        }
        else
        {
            this.donePlaying = true;
        }
    }
}
