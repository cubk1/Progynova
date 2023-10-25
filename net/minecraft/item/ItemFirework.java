package net.minecraft.item;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.item.实体FireworkRocket;
import net.minecraft.entity.player.实体Player;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemFirework extends Item
{
    public boolean onItemUse(ItemStack stack, 实体Player playerIn, World worldIn, 阻止位置 pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            实体FireworkRocket entityfireworkrocket = new 实体FireworkRocket(worldIn, (double)((float)pos.getX() + hitX), (double)((float)pos.getY() + hitY), (double)((float)pos.getZ() + hitZ), stack);
            worldIn.spawnEntityInWorld(entityfireworkrocket);

            if (!playerIn.capabilities.isCreativeMode)
            {
                --stack.stackSize;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public void addInformation(ItemStack stack, 实体Player playerIn, List<String> tooltip, boolean advanced)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("Fireworks");

            if (nbttagcompound != null)
            {
                if (nbttagcompound.hasKey("Flight", 99))
                {
                    tooltip.add(StatCollector.translateToLocal("item.fireworks.flight") + " " + nbttagcompound.getByte("Flight"));
                }

                NBTTagList nbttaglist = nbttagcompound.getTagList("Explosions", 10);

                if (nbttaglist != null && nbttaglist.tagCount() > 0)
                {
                    for (int i = 0; i < nbttaglist.tagCount(); ++i)
                    {
                        NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                        List<String> list = Lists.<String>newArrayList();
                        ItemFireworkCharge.addExplosionInfo(nbttagcompound1, list);

                        if (list.size() > 0)
                        {
                            for (int j = 1; j < ((List)list).size(); ++j)
                            {
                                list.set(j, "  " + (String)list.get(j));
                            }

                            tooltip.addAll(list);
                        }
                    }
                }
            }
        }
    }
}
