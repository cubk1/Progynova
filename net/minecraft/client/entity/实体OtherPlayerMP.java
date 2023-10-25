package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.我的手艺;
import net.minecraft.item.ItemStack;
import net.minecraft.util.阻止位置;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class 实体OtherPlayerMP extends AbstractClientPlayer
{
    private boolean isItemInUse;
    private int otherPlayerMPPosRotationIncrements;
    private double otherPlayerMPX;
    private double otherPlayerMPY;
    private double otherPlayerMPZ;
    private double otherPlayerMPYaw;
    private double otherPlayerMPPitch;

    public 实体OtherPlayerMP(World worldIn, GameProfile gameProfileIn)
    {
        super(worldIn, gameProfileIn);
        this.stepHeight = 0.0F;
        this.noClip = true;
        this.renderOffsetY = 0.25F;
        this.renderDistanceWeight = 10.0D;
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return true;
    }

    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
    {
        this.otherPlayerMPX = x;
        this.otherPlayerMPY = y;
        this.otherPlayerMPZ = z;
        this.otherPlayerMPYaw = (double)yaw;
        this.otherPlayerMPPitch = (double)pitch;
        this.otherPlayerMPPosRotationIncrements = posRotationIncrements;
    }

    public void onUpdate()
    {
        this.renderOffsetY = 0.0F;
        super.onUpdate();
        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d0 = this.X坐标 - this.prevPosX;
        double d1 = this.Z坐标 - this.prevPosZ;
        float f = MathHelper.sqrt_double(d0 * d0 + d1 * d1) * 4.0F;

        if (f > 1.0F)
        {
            f = 1.0F;
        }

        this.limbSwingAmount += (f - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;

        if (!this.isItemInUse && this.isEating() && this.inventory.mainInventory[this.inventory.currentItem] != null)
        {
            ItemStack itemstack = this.inventory.mainInventory[this.inventory.currentItem];
            this.setItemInUse(this.inventory.mainInventory[this.inventory.currentItem], itemstack.getItem().getMaxItemUseDuration(itemstack));
            this.isItemInUse = true;
        }
        else if (this.isItemInUse && !this.isEating())
        {
            this.clearItemInUse();
            this.isItemInUse = false;
        }
    }

    public void onLivingUpdate()
    {
        if (this.otherPlayerMPPosRotationIncrements > 0)
        {
            double d0 = this.X坐标 + (this.otherPlayerMPX - this.X坐标) / (double)this.otherPlayerMPPosRotationIncrements;
            double d1 = this.Y坐标 + (this.otherPlayerMPY - this.Y坐标) / (double)this.otherPlayerMPPosRotationIncrements;
            double d2 = this.Z坐标 + (this.otherPlayerMPZ - this.Z坐标) / (double)this.otherPlayerMPPosRotationIncrements;
            double d3;

            for (d3 = this.otherPlayerMPYaw - (double)this.旋转侧滑; d3 < -180.0D; d3 += 360.0D)
            {
                ;
            }

            while (d3 >= 180.0D)
            {
                d3 -= 360.0D;
            }

            this.旋转侧滑 = (float)((double)this.旋转侧滑 + d3 / (double)this.otherPlayerMPPosRotationIncrements);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.otherPlayerMPPitch - (double)this.rotationPitch) / (double)this.otherPlayerMPPosRotationIncrements);
            --this.otherPlayerMPPosRotationIncrements;
            this.setPosition(d0, d1, d2);
            this.setRotation(this.旋转侧滑, this.rotationPitch);
        }

        this.prevCameraYaw = this.cameraYaw;
        this.updateArmSwingProgress();
        float f1 = MathHelper.sqrt_double(this.通便X * this.通便X + this.通便Z * this.通便Z);
        float f = (float)Math.atan(-this.motionY * 0.20000000298023224D) * 15.0F;

        if (f1 > 0.1F)
        {
            f1 = 0.1F;
        }

        if (!this.onGround || this.getHealth() <= 0.0F)
        {
            f1 = 0.0F;
        }

        if (this.onGround || this.getHealth() <= 0.0F)
        {
            f = 0.0F;
        }

        this.cameraYaw += (f1 - this.cameraYaw) * 0.4F;
        this.cameraPitch += (f - this.cameraPitch) * 0.8F;
    }

    public void setCurrentItemOrArmor(int slotIn, ItemStack stack)
    {
        if (slotIn == 0)
        {
            this.inventory.mainInventory[this.inventory.currentItem] = stack;
        }
        else
        {
            this.inventory.armorInventory[slotIn - 1] = stack;
        }
    }

    public void 增添聊天讯息(IChatComponent component)
    {
        我的手艺.得到我的手艺().ingameGUI.getChatGUI().printChatMessage(component);
    }

    public boolean canCommandSenderUseCommand(int permLevel, String commandName)
    {
        return false;
    }

    public 阻止位置 getPosition()
    {
        return new 阻止位置(this.X坐标 + 0.5D, this.Y坐标 + 0.5D, this.Z坐标 + 0.5D);
    }
}
