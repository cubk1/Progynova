package net.minecraft.util;

import net.minecraft.entity.实体;

public class MovingObjectPosition
{
    private 阻止位置 阻止位置;
    public MovingObjectPosition.MovingObjectType typeOfHit;
    public EnumFacing sideHit;
    public Vec3 hitVec;
    public 实体 实体Hit;

    public MovingObjectPosition(Vec3 hitVecIn, EnumFacing facing, 阻止位置 阻止位置In)
    {
        this(MovingObjectPosition.MovingObjectType.BLOCK, hitVecIn, facing, 阻止位置In);
    }

    public MovingObjectPosition(Vec3 p_i45552_1_, EnumFacing facing)
    {
        this(MovingObjectPosition.MovingObjectType.BLOCK, p_i45552_1_, facing, 阻止位置.ORIGIN);
    }

    public MovingObjectPosition(实体 实体In)
    {
        this(实体In, new Vec3(实体In.X坐标, 实体In.Y坐标, 实体In.Z坐标));
    }

    public MovingObjectPosition(MovingObjectPosition.MovingObjectType typeOfHitIn, Vec3 hitVecIn, EnumFacing sideHitIn, 阻止位置 阻止位置In)
    {
        this.typeOfHit = typeOfHitIn;
        this.阻止位置 = 阻止位置In;
        this.sideHit = sideHitIn;
        this.hitVec = new Vec3(hitVecIn.xCoord, hitVecIn.yCoord, hitVecIn.zCoord);
    }

    public MovingObjectPosition(实体 实体HitIn, Vec3 hitVecIn)
    {
        this.typeOfHit = MovingObjectPosition.MovingObjectType.ENTITY;
        this.实体Hit = 实体HitIn;
        this.hitVec = hitVecIn;
    }

    public 阻止位置 getBlockPos()
    {
        return this.阻止位置;
    }

    public String toString()
    {
        return "HitResult{type=" + this.typeOfHit + ", blockpos=" + this.阻止位置 + ", f=" + this.sideHit + ", pos=" + this.hitVec + ", entity=" + this.实体Hit + '}';
    }

    public static enum MovingObjectType
    {
        MISS,
        BLOCK,
        ENTITY;
    }
}
