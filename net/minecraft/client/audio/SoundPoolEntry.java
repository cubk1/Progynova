package net.minecraft.client.audio;

import net.minecraft.util.图像位置;

public class SoundPoolEntry
{
    private final 图像位置 location;
    private final boolean streamingSound;
    private double pitch;
    private double volume;

    public SoundPoolEntry(图像位置 locationIn, double pitchIn, double volumeIn, boolean streamingSoundIn)
    {
        this.location = locationIn;
        this.pitch = pitchIn;
        this.volume = volumeIn;
        this.streamingSound = streamingSoundIn;
    }

    public SoundPoolEntry(SoundPoolEntry locationIn)
    {
        this.location = locationIn.location;
        this.pitch = locationIn.pitch;
        this.volume = locationIn.volume;
        this.streamingSound = locationIn.streamingSound;
    }

    public 图像位置 getSoundPoolEntryLocation()
    {
        return this.location;
    }

    public double getPitch()
    {
        return this.pitch;
    }

    public void setPitch(double pitchIn)
    {
        this.pitch = pitchIn;
    }

    public double getVolume()
    {
        return this.volume;
    }

    public void setVolume(double volumeIn)
    {
        this.volume = volumeIn;
    }

    public boolean isStreamingSound()
    {
        return this.streamingSound;
    }
}
