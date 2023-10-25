package net.minecraft.util;

import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.item.ItemStack;

public class EntityDamageSourceIndirect extends EntityDamageSource
{
    private 实体 indirect实体;

    public EntityDamageSourceIndirect(String damageTypeIn, 实体 source, 实体 indirect实体In)
    {
        super(damageTypeIn, source);
        this.indirect实体 = indirect实体In;
    }

    public 实体 getSourceOfDamage()
    {
        return this.damageSource实体;
    }

    public 实体 getEntity()
    {
        return this.indirect实体;
    }

    public IChatComponent getDeathMessage(实体LivingBase entityLivingBaseIn)
    {
        IChatComponent ichatcomponent = this.indirect实体 == null ? this.damageSource实体.getDisplayName() : this.indirect实体.getDisplayName();
        ItemStack itemstack = this.indirect实体 instanceof 实体LivingBase ? ((实体LivingBase)this.indirect实体).getHeldItem() : null;
        String s = "death.attack." + this.damageType;
        String s1 = s + ".item";
        return itemstack != null && itemstack.hasDisplayName() && StatCollector.canTranslate(s1) ? new ChatComponentTranslation(s1, new Object[] {entityLivingBaseIn.getDisplayName(), ichatcomponent, itemstack.getChatComponent()}): new ChatComponentTranslation(s, new Object[] {entityLivingBaseIn.getDisplayName(), ichatcomponent});
    }
}
