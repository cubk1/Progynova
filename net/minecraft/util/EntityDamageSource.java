package net.minecraft.util;

import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.player.实体Player;
import net.minecraft.item.ItemStack;

public class EntityDamageSource extends DamageSource
{
    protected 实体 damageSource实体;
    private boolean isThornsDamage = false;

    public EntityDamageSource(String damageTypeIn, 实体 damageSource实体In)
    {
        super(damageTypeIn);
        this.damageSource实体 = damageSource实体In;
    }

    public EntityDamageSource setIsThornsDamage()
    {
        this.isThornsDamage = true;
        return this;
    }

    public boolean getIsThornsDamage()
    {
        return this.isThornsDamage;
    }

    public 实体 getEntity()
    {
        return this.damageSource实体;
    }

    public IChatComponent getDeathMessage(实体LivingBase entityLivingBaseIn)
    {
        ItemStack itemstack = this.damageSource实体 instanceof 实体LivingBase ? ((实体LivingBase)this.damageSource实体).getHeldItem() : null;
        String s = "death.attack." + this.damageType;
        String s1 = s + ".item";
        return itemstack != null && itemstack.hasDisplayName() && StatCollector.canTranslate(s1) ? new ChatComponentTranslation(s1, new Object[] {entityLivingBaseIn.getDisplayName(), this.damageSource实体.getDisplayName(), itemstack.getChatComponent()}): new ChatComponentTranslation(s, new Object[] {entityLivingBaseIn.getDisplayName(), this.damageSource实体.getDisplayName()});
    }

    public boolean isDifficultyScaled()
    {
        return this.damageSource实体 != null && this.damageSource实体 instanceof 实体LivingBase && !(this.damageSource实体 instanceof 实体Player);
    }
}
