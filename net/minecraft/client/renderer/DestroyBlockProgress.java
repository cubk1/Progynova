package net.minecraft.client.renderer;

import net.minecraft.util.阻止位置;

public class DestroyBlockProgress
{
    private final int miningPlayerEntId;
    private final 阻止位置 position;
    private int partialBlockProgress;
    private int createdAtCloudUpdateTick;

    public DestroyBlockProgress(int miningPlayerEntIdIn, 阻止位置 positionIn)
    {
        this.miningPlayerEntId = miningPlayerEntIdIn;
        this.position = positionIn;
    }

    public 阻止位置 getPosition()
    {
        return this.position;
    }

    public void setPartialBlockDamage(int damage)
    {
        if (damage > 10)
        {
            damage = 10;
        }

        this.partialBlockProgress = damage;
    }

    public int getPartialBlockDamage()
    {
        return this.partialBlockProgress;
    }

    public void setCloudUpdateTick(int createdAtCloudUpdateTickIn)
    {
        this.createdAtCloudUpdateTick = createdAtCloudUpdateTickIn;
    }

    public int getCreationCloudUpdateTick()
    {
        return this.createdAtCloudUpdateTick;
    }
}
