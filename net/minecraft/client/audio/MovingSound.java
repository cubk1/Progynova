package net.minecraft.client.audio;

import net.minecraft.util.图像位置;

public abstract class MovingSound extends PositionedSound implements ITickableSound
{
    protected boolean donePlaying = false;

    protected MovingSound(图像位置 location)
    {
        super(location);
    }

    public boolean isDonePlaying()
    {
        return this.donePlaying;
    }
}
