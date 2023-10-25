package net.minecraft.client.renderer.entity;

import net.minecraft.entity.projectile.实体Potion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RenderPotion extends RenderSnowball<实体Potion>
{
    public RenderPotion(RenderManager renderManagerIn, RenderItem itemRendererIn)
    {
        super(renderManagerIn, Items.potionitem, itemRendererIn);
    }

    public ItemStack func_177082_d(实体Potion entityIn)
    {
        return new ItemStack(this.field_177084_a, 1, entityIn.getPotionDamage());
    }
}
