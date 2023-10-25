package net.minecraft.client.audio;

import net.minecraft.entity.monster.实体Guardian;
import net.minecraft.util.图像位置;

public class GuardianSound extends MovingSound
{
    private final 实体Guardian guardian;

    public GuardianSound(实体Guardian guardian)
    {
        super(new 图像位置("minecraft:mob.guardian.attack"));
        this.guardian = guardian;
        this.attenuationType = ISound.AttenuationType.NONE;
        this.repeat = true;
        this.repeatDelay = 0;
    }

    public void update()
    {
        if (!this.guardian.isDead && this.guardian.hasTargetedEntity())
        {
            this.xPosF = (float)this.guardian.X坐标;
            this.yPosF = (float)this.guardian.Y坐标;
            this.zPosF = (float)this.guardian.Z坐标;
            float f = this.guardian.func_175477_p(0.0F);
            this.volume = 0.0F + 1.0F * f * f;
            this.pitch = 0.7F + 0.5F * f;
        }
        else
        {
            this.donePlaying = true;
        }
    }
}
