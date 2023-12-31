package net.minecraft.entity.ai;

import net.minecraft.entity.实体Living;
import net.minecraft.entity.实体;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.util.MathHelper;

public class EntityLookHelper
{
    private 实体Living entity;
    private float deltaLookYaw;
    private float deltaLookPitch;
    private boolean isLooking;
    private double posX;
    private double posY;
    private double posZ;

    public EntityLookHelper(实体Living entitylivingIn)
    {
        this.entity = entitylivingIn;
    }

    public void setLookPositionWithEntity(实体 实体In, float deltaYaw, float deltaPitch)
    {
        this.posX = 实体In.X坐标;

        if (实体In instanceof 实体LivingBase)
        {
            this.posY = 实体In.Y坐标 + (double) 实体In.getEyeHeight();
        }
        else
        {
            this.posY = (实体In.getEntityBoundingBox().minY + 实体In.getEntityBoundingBox().maxY) / 2.0D;
        }

        this.posZ = 实体In.Z坐标;
        this.deltaLookYaw = deltaYaw;
        this.deltaLookPitch = deltaPitch;
        this.isLooking = true;
    }

    public void setLookPosition(double x, double y, double z, float deltaYaw, float deltaPitch)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.deltaLookYaw = deltaYaw;
        this.deltaLookPitch = deltaPitch;
        this.isLooking = true;
    }

    public void onUpdateLook()
    {
        this.entity.rotationPitch = 0.0F;

        if (this.isLooking)
        {
            this.isLooking = false;
            double d0 = this.posX - this.entity.X坐标;
            double d1 = this.posY - (this.entity.Y坐标 + (double)this.entity.getEyeHeight());
            double d2 = this.posZ - this.entity.Z坐标;
            double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);
            float f = (float)(MathHelper.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
            float f1 = (float)(-(MathHelper.atan2(d1, d3) * 180.0D / Math.PI));
            this.entity.rotationPitch = this.updateRotation(this.entity.rotationPitch, f1, this.deltaLookPitch);
            this.entity.rotationYawHead = this.updateRotation(this.entity.rotationYawHead, f, this.deltaLookYaw);
        }
        else
        {
            this.entity.rotationYawHead = this.updateRotation(this.entity.rotationYawHead, this.entity.renderYawOffset, 10.0F);
        }

        float f2 = MathHelper.wrapAngleTo180_float(this.entity.rotationYawHead - this.entity.renderYawOffset);

        if (!this.entity.getNavigator().noPath())
        {
            if (f2 < -75.0F)
            {
                this.entity.rotationYawHead = this.entity.renderYawOffset - 75.0F;
            }

            if (f2 > 75.0F)
            {
                this.entity.rotationYawHead = this.entity.renderYawOffset + 75.0F;
            }
        }
    }

    private float updateRotation(float p_75652_1_, float p_75652_2_, float p_75652_3_)
    {
        float f = MathHelper.wrapAngleTo180_float(p_75652_2_ - p_75652_1_);

        if (f > p_75652_3_)
        {
            f = p_75652_3_;
        }

        if (f < -p_75652_3_)
        {
            f = -p_75652_3_;
        }

        return p_75652_1_ + f;
    }

    public boolean getIsLooking()
    {
        return this.isLooking;
    }

    public double getLookPosX()
    {
        return this.posX;
    }

    public double getLookPosY()
    {
        return this.posY;
    }

    public double getLookPosZ()
    {
        return this.posZ;
    }
}
