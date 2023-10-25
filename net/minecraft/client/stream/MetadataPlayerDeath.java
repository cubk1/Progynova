package net.minecraft.client.stream;

import net.minecraft.entity.实体LivingBase;

public class MetadataPlayerDeath extends Metadata
{
    public MetadataPlayerDeath(实体LivingBase p_i46066_1_, 实体LivingBase p_i46066_2_)
    {
        super("player_death");

        if (p_i46066_1_ != null)
        {
            this.func_152808_a("player", p_i46066_1_.getName());
        }

        if (p_i46066_2_ != null)
        {
            this.func_152808_a("killer", p_i46066_2_.getName());
        }
    }
}
