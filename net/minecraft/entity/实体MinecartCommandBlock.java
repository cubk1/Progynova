package net.minecraft.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.item.实体Minecart;
import net.minecraft.entity.player.实体Player;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.阻止位置;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class 实体MinecartCommandBlock extends 实体Minecart
{
    private final CommandBlockLogic commandBlockLogic = new CommandBlockLogic()
    {
        public void updateCommand()
        {
            实体MinecartCommandBlock.this.getDataWatcher().updateObject(23, this.getCommand());
            实体MinecartCommandBlock.this.getDataWatcher().updateObject(24, IChatComponent.Serializer.componentToJson(this.getLastOutput()));
        }
        public int func_145751_f()
        {
            return 1;
        }
        public void func_145757_a(ByteBuf p_145757_1_)
        {
            p_145757_1_.writeInt(实体MinecartCommandBlock.this.getEntityId());
        }
        public 阻止位置 getPosition()
        {
            return new 阻止位置(实体MinecartCommandBlock.this.X坐标, 实体MinecartCommandBlock.this.Y坐标 + 0.5D, 实体MinecartCommandBlock.this.Z坐标);
        }
        public Vec3 getPositionVector()
        {
            return new Vec3(实体MinecartCommandBlock.this.X坐标, 实体MinecartCommandBlock.this.Y坐标, 实体MinecartCommandBlock.this.Z坐标);
        }
        public World getEntityWorld()
        {
            return 实体MinecartCommandBlock.this.worldObj;
        }
        public 实体 getCommandSenderEntity()
        {
            return 实体MinecartCommandBlock.this;
        }
    };
    private int activatorRailCooldown = 0;

    public 实体MinecartCommandBlock(World worldIn)
    {
        super(worldIn);
    }

    public 实体MinecartCommandBlock(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.getDataWatcher().addObject(23, "");
        this.getDataWatcher().addObject(24, "");
    }

    protected void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.commandBlockLogic.readDataFromNBT(tagCompund);
        this.getDataWatcher().updateObject(23, this.getCommandBlockLogic().getCommand());
        this.getDataWatcher().updateObject(24, IChatComponent.Serializer.componentToJson(this.getCommandBlockLogic().getLastOutput()));
    }

    protected void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        this.commandBlockLogic.writeDataToNBT(tagCompound);
    }

    public 实体Minecart.EnumMinecartType getMinecartType()
    {
        return 实体Minecart.EnumMinecartType.COMMAND_BLOCK;
    }

    public IBlockState getDefaultDisplayTile()
    {
        return Blocks.command_block.getDefaultState();
    }

    public CommandBlockLogic getCommandBlockLogic()
    {
        return this.commandBlockLogic;
    }

    public void onActivatorRailPass(int x, int y, int z, boolean receivingPower)
    {
        if (receivingPower && this.已存在的刻度 - this.activatorRailCooldown >= 4)
        {
            this.getCommandBlockLogic().trigger(this.worldObj);
            this.activatorRailCooldown = this.已存在的刻度;
        }
    }

    public boolean interactFirst(实体Player playerIn)
    {
        this.commandBlockLogic.tryOpenEditCommandBlock(playerIn);
        return false;
    }

    public void onDataWatcherUpdate(int dataID)
    {
        super.onDataWatcherUpdate(dataID);

        if (dataID == 24)
        {
            try
            {
                this.commandBlockLogic.setLastOutput(IChatComponent.Serializer.jsonToComponent(this.getDataWatcher().getWatchableObjectString(24)));
            }
            catch (Throwable var3)
            {
                ;
            }
        }
        else if (dataID == 23)
        {
            this.commandBlockLogic.setCommand(this.getDataWatcher().getWatchableObjectString(23));
        }
    }
}
