package net.minecraft.item;

import java.util.List;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.*;
import net.minecraft.entity.实体;
import net.minecraft.entity.player.实体Player;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemMonsterPlacer extends Item
{
    public ItemMonsterPlacer()
    {
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        String s = ("" + StatCollector.translateToLocal(this.getUnlocalizedName() + ".name")).trim();
        String s1 = EntityList.getStringFromID(stack.getMetadata());

        if (s1 != null)
        {
            s = s + " " + StatCollector.translateToLocal("entity." + s1 + ".name");
        }

        return s;
    }

    public int getColorFromItemStack(ItemStack stack, int renderPass)
    {
        EntityList.EntityEggInfo entitylist$entityegginfo = (EntityList.EntityEggInfo)EntityList.entityEggs.get(Integer.valueOf(stack.getMetadata()));
        return entitylist$entityegginfo != null ? (renderPass == 0 ? entitylist$entityegginfo.primaryColor : entitylist$entityegginfo.secondaryColor) : 16777215;
    }

    public boolean onItemUse(ItemStack stack, 实体Player playerIn, World worldIn, 阻止位置 pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
        {
            return false;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(pos);

            if (iblockstate.getBlock() == Blocks.mob_spawner)
            {
                TileEntity tileentity = worldIn.getTileEntity(pos);

                if (tileentity instanceof TileEntityMobSpawner)
                {
                    MobSpawnerBaseLogic mobspawnerbaselogic = ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic();
                    mobspawnerbaselogic.setEntityName(EntityList.getStringFromID(stack.getMetadata()));
                    tileentity.markDirty();
                    worldIn.markBlockForUpdate(pos);

                    if (!playerIn.capabilities.isCreativeMode)
                    {
                        --stack.stackSize;
                    }

                    return true;
                }
            }

            pos = pos.offset(side);
            double d0 = 0.0D;

            if (side == EnumFacing.UP && iblockstate instanceof BlockFence)
            {
                d0 = 0.5D;
            }

            实体 实体 = spawnCreature(worldIn, stack.getMetadata(), (double)pos.getX() + 0.5D, (double)pos.getY() + d0, (double)pos.getZ() + 0.5D);

            if (实体 != null)
            {
                if (实体 instanceof 实体LivingBase && stack.hasDisplayName())
                {
                    实体.setCustomNameTag(stack.getDisplayName());
                }

                if (!playerIn.capabilities.isCreativeMode)
                {
                    --stack.stackSize;
                }
            }

            return true;
        }
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, 实体Player playerIn)
    {
        if (worldIn.isRemote)
        {
            return itemStackIn;
        }
        else
        {
            MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, true);

            if (movingobjectposition == null)
            {
                return itemStackIn;
            }
            else
            {
                if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    阻止位置 blockpos = movingobjectposition.getBlockPos();

                    if (!worldIn.isBlockModifiable(playerIn, blockpos))
                    {
                        return itemStackIn;
                    }

                    if (!playerIn.canPlayerEdit(blockpos, movingobjectposition.sideHit, itemStackIn))
                    {
                        return itemStackIn;
                    }

                    if (worldIn.getBlockState(blockpos).getBlock() instanceof BlockLiquid)
                    {
                        实体 实体 = spawnCreature(worldIn, itemStackIn.getMetadata(), (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D);

                        if (实体 != null)
                        {
                            if (实体 instanceof 实体LivingBase && itemStackIn.hasDisplayName())
                            {
                                ((实体Living) 实体).setCustomNameTag(itemStackIn.getDisplayName());
                            }

                            if (!playerIn.capabilities.isCreativeMode)
                            {
                                --itemStackIn.stackSize;
                            }

                            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                        }
                    }
                }

                return itemStackIn;
            }
        }
    }

    public static 实体 spawnCreature(World worldIn, int entityID, double x, double y, double z)
    {
        if (!EntityList.entityEggs.containsKey(Integer.valueOf(entityID)))
        {
            return null;
        }
        else
        {
            实体 实体 = null;

            for (int i = 0; i < 1; ++i)
            {
                实体 = EntityList.createEntityByID(entityID, worldIn);

                if (实体 instanceof 实体LivingBase)
                {
                    实体Living entityliving = (实体Living) 实体;
                    实体.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(worldIn.rand.nextFloat() * 360.0F), 0.0F);
                    entityliving.rotationYawHead = entityliving.旋转侧滑;
                    entityliving.renderYawOffset = entityliving.旋转侧滑;
                    entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new 阻止位置(entityliving)), (IEntityLivingData)null);
                    worldIn.spawnEntityInWorld(实体);
                    entityliving.playLivingSound();
                }
            }

            return 实体;
        }
    }

    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
    {
        for (EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.entityEggs.values())
        {
            subItems.add(new ItemStack(itemIn, 1, entitylist$entityegginfo.spawnedID));
        }
    }
}
