package net.minecraft.client.audio;

import net.minecraft.util.图像位置;

public abstract class PositionedSound implements ISound
{
    protected final 图像位置 positionedSoundLocation;
    protected float volume = 1.0F;
    protected float pitch = 1.0F;
    protected float xPosF;
    protected float yPosF;
    protected float zPosF;
    protected boolean repeat = false;
    protected int repeatDelay = 0;
    protected ISound.AttenuationType attenuationType = ISound.AttenuationType.LINEAR;

    protected PositionedSound(图像位置 soundResource)
    {
        this.positionedSoundLocation = soundResource;
    }

    public 图像位置 getSoundLocation()
    {
        return this.positionedSoundLocation;
    }

    public boolean canRepeat()
    {
        return this.repeat;
    }

    public int getRepeatDelay()
    {
        return this.repeatDelay;
    }

    public float getVolume()
    {
        return this.volume;
    }

    public float getPitch()
    {
        return this.pitch;
    }

    public float getXPosF()
    {
        return this.xPosF;
    }

    public float getYPosF()
    {
        return this.yPosF;
    }

    public float getZPosF()
    {
        return this.zPosF;
    }

    public ISound.AttenuationType getAttenuationType()
    {
        return this.attenuationType;
    }
}
