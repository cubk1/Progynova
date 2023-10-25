package net.optifine.override;

import net.minecraft.client.entity.实体PlayerSP;
import net.minecraft.client.我的手艺;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.实体Player;
import net.minecraft.entity.实体;
import net.minecraft.item.ItemStack;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class PlayerControllerOF extends PlayerControllerMP
{
    private boolean acting = false;
    private 阻止位置 lastClick阻止位置 = null;
    private 实体 lastClick实体 = null;

    public PlayerControllerOF(我的手艺 mcIn, NetHandlerPlayClient netHandler)
    {
        super(mcIn, netHandler);
    }

    public boolean clickBlock(阻止位置 loc, EnumFacing face)
    {
        this.acting = true;
        this.lastClick阻止位置 = loc;
        boolean flag = super.clickBlock(loc, face);
        this.acting = false;
        return flag;
    }

    public boolean onPlayerDamageBlock(阻止位置 posBlock, EnumFacing directionFacing)
    {
        this.acting = true;
        this.lastClick阻止位置 = posBlock;
        boolean flag = super.onPlayerDamageBlock(posBlock, directionFacing);
        this.acting = false;
        return flag;
    }

    public boolean sendUseItem(实体Player player, World worldIn, ItemStack stack)
    {
        this.acting = true;
        boolean flag = super.sendUseItem(player, worldIn, stack);
        this.acting = false;
        return flag;
    }

    public boolean onPlayerRightClick(实体PlayerSP p_178890_1, WorldClient p_178890_2, ItemStack p_178890_3, 阻止位置 p_178890_4, EnumFacing p_178890_5, Vec3 p_178890_6)
    {
        this.acting = true;
        this.lastClick阻止位置 = p_178890_4;
        boolean flag = super.onPlayerRightClick(p_178890_1, p_178890_2, p_178890_3, p_178890_4, p_178890_5, p_178890_6);
        this.acting = false;
        return flag;
    }

    public boolean interactWithEntitySendPacket(实体Player player, 实体 target)
    {
        this.lastClick实体 = target;
        return super.interactWithEntitySendPacket(player, target);
    }

    public boolean isPlayerRightClickingOnEntity(实体Player player, 实体 target, MovingObjectPosition ray)
    {
        this.lastClick实体 = target;
        return super.isPlayerRightClickingOnEntity(player, target, ray);
    }

    public boolean isActing()
    {
        return this.acting;
    }

    public 阻止位置 getLastClickBlockPos()
    {
        return this.lastClick阻止位置;
    }

    public 实体 getLastClickEntity()
    {
        return this.lastClick实体;
    }
}
