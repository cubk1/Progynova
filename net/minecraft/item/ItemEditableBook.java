package net.minecraft.item;

import java.util.List;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.player.实体PlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.交流组分文本;
import net.minecraft.util.枚举聊天格式;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class ItemEditableBook extends Item
{
    public ItemEditableBook()
    {
        this.setMaxStackSize(1);
    }

    public static boolean validBookTagContents(NBTTagCompound nbt)
    {
        if (!ItemWritableBook.isNBTValid(nbt))
        {
            return false;
        }
        else if (!nbt.hasKey("title", 8))
        {
            return false;
        }
        else
        {
            String s = nbt.getString("title");
            return s != null && s.length() <= 32 ? nbt.hasKey("author", 8) : false;
        }
    }

    public static int getGeneration(ItemStack book)
    {
        return book.getTagCompound().getInteger("generation");
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound nbttagcompound = stack.getTagCompound();
            String s = nbttagcompound.getString("title");

            if (!StringUtils.isNullOrEmpty(s))
            {
                return s;
            }
        }

        return super.getItemStackDisplayName(stack);
    }

    public void addInformation(ItemStack stack, 实体Player playerIn, List<String> tooltip, boolean advanced)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound nbttagcompound = stack.getTagCompound();
            String s = nbttagcompound.getString("author");

            if (!StringUtils.isNullOrEmpty(s))
            {
                tooltip.add(枚举聊天格式.GRAY + StatCollector.translateToLocalFormatted("book.byAuthor", new Object[] {s}));
            }

            tooltip.add(枚举聊天格式.GRAY + StatCollector.translateToLocal("book.generation." + nbttagcompound.getInteger("generation")));
        }
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, 实体Player playerIn)
    {
        if (!worldIn.isRemote)
        {
            this.resolveContents(itemStackIn, playerIn);
        }

        playerIn.displayGUIBook(itemStackIn);
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStackIn;
    }

    private void resolveContents(ItemStack stack, 实体Player player)
    {
        if (stack != null && stack.getTagCompound() != null)
        {
            NBTTagCompound nbttagcompound = stack.getTagCompound();

            if (!nbttagcompound.getBoolean("resolved"))
            {
                nbttagcompound.setBoolean("resolved", true);

                if (validBookTagContents(nbttagcompound))
                {
                    NBTTagList nbttaglist = nbttagcompound.getTagList("pages", 8);

                    for (int i = 0; i < nbttaglist.tagCount(); ++i)
                    {
                        String s = nbttaglist.getStringTagAt(i);
                        IChatComponent ichatcomponent;

                        try
                        {
                            ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
                            ichatcomponent = ChatComponentProcessor.processComponent(player, ichatcomponent, player);
                        }
                        catch (Exception var9)
                        {
                            ichatcomponent = new 交流组分文本(s);
                        }

                        nbttaglist.set(i, new NBTTagString(IChatComponent.Serializer.componentToJson(ichatcomponent)));
                    }

                    nbttagcompound.setTag("pages", nbttaglist);

                    if (player instanceof 实体PlayerMP && player.getCurrentEquippedItem() == stack)
                    {
                        Slot slot = player.openContainer.getSlotFromInventory(player.inventory, player.inventory.currentItem);
                        ((实体PlayerMP)player).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(0, slot.slotNumber, stack));
                    }
                }
            }
        }
    }

    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }
}
