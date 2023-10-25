package net.minecraft.util;

import com.google.common.base.Predicate;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体Living;
import net.minecraft.entity.item.实体ArmorStand;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public final class EntitySelectors
{
    public static final Predicate<实体> selectAnything = new Predicate<实体>()
    {
        public boolean apply(实体 p_apply_1_)
        {
            return p_apply_1_.isEntityAlive();
        }
    };
    public static final Predicate<实体> IS_STANDALONE = new Predicate<实体>()
    {
        public boolean apply(实体 p_apply_1_)
        {
            return p_apply_1_.isEntityAlive() && p_apply_1_.riddenBy实体 == null && p_apply_1_.riding实体 == null;
        }
    };
    public static final Predicate<实体> selectInventories = new Predicate<实体>()
    {
        public boolean apply(实体 p_apply_1_)
        {
            return p_apply_1_ instanceof IInventory && p_apply_1_.isEntityAlive();
        }
    };
    public static final Predicate<实体> NOT_SPECTATING = new Predicate<实体>()
    {
        public boolean apply(实体 p_apply_1_)
        {
            return !(p_apply_1_ instanceof 实体Player) || !((实体Player)p_apply_1_).isSpectator();
        }
    };

    public static class ArmoredMob implements Predicate<实体>
    {
        private final ItemStack armor;

        public ArmoredMob(ItemStack armor)
        {
            this.armor = armor;
        }

        public boolean apply(实体 p_apply_1_)
        {
            if (!p_apply_1_.isEntityAlive())
            {
                return false;
            }
            else if (!(p_apply_1_ instanceof 实体LivingBase))
            {
                return false;
            }
            else
            {
                实体LivingBase entitylivingbase = (实体LivingBase)p_apply_1_;
                return entitylivingbase.getEquipmentInSlot(实体Living.getArmorPosition(this.armor)) != null ? false : (entitylivingbase instanceof 实体Living ? ((实体Living)entitylivingbase).canPickUpLoot() : (entitylivingbase instanceof 实体ArmorStand ? true : entitylivingbase instanceof 实体Player));
            }
        }
    }
}
