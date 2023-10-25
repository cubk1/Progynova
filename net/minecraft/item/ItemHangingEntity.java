package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.实体ItemFrame;
import net.minecraft.entity.item.实体Painting;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体Hanging;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemHangingEntity extends Item
{
    private final Class <? extends 实体Hanging> hangingEntityClass;

    public ItemHangingEntity(Class <? extends 实体Hanging> entityClass)
    {
        this.hangingEntityClass = entityClass;
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public boolean onItemUse(ItemStack stack, 实体Player playerIn, World worldIn, 阻止位置 pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (side == EnumFacing.DOWN)
        {
            return false;
        }
        else if (side == EnumFacing.UP)
        {
            return false;
        }
        else
        {
            阻止位置 blockpos = pos.offset(side);

            if (!playerIn.canPlayerEdit(blockpos, side, stack))
            {
                return false;
            }
            else
            {
                实体Hanging entityhanging = this.createEntity(worldIn, blockpos, side);

                if (entityhanging != null && entityhanging.onValidSurface())
                {
                    if (!worldIn.isRemote)
                    {
                        worldIn.spawnEntityInWorld(entityhanging);
                    }

                    --stack.stackSize;
                }

                return true;
            }
        }
    }

    private 实体Hanging createEntity(World worldIn, 阻止位置 pos, EnumFacing clickedSide)
    {
        return (实体Hanging)(this.hangingEntityClass == 实体Painting.class ? new 实体Painting(worldIn, pos, clickedSide) : (this.hangingEntityClass == 实体ItemFrame.class ? new 实体ItemFrame(worldIn, pos, clickedSide) : null));
    }
}
