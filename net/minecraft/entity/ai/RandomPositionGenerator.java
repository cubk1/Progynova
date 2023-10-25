package net.minecraft.entity.ai;

import java.util.Random;
import net.minecraft.entity.实体Creature;
import net.minecraft.util.阻止位置;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RandomPositionGenerator
{
    private static Vec3 staticVector = new Vec3(0.0D, 0.0D, 0.0D);

    public static Vec3 findRandomTarget(实体Creature entitycreatureIn, int xz, int y)
    {
        return findRandomTargetBlock(entitycreatureIn, xz, y, (Vec3)null);
    }

    public static Vec3 findRandomTargetBlockTowards(实体Creature entitycreatureIn, int xz, int y, Vec3 targetVec3)
    {
        staticVector = targetVec3.subtract(entitycreatureIn.X坐标, entitycreatureIn.Y坐标, entitycreatureIn.Z坐标);
        return findRandomTargetBlock(entitycreatureIn, xz, y, staticVector);
    }

    public static Vec3 findRandomTargetBlockAwayFrom(实体Creature entitycreatureIn, int xz, int y, Vec3 targetVec3)
    {
        staticVector = (new Vec3(entitycreatureIn.X坐标, entitycreatureIn.Y坐标, entitycreatureIn.Z坐标)).subtract(targetVec3);
        return findRandomTargetBlock(entitycreatureIn, xz, y, staticVector);
    }

    private static Vec3 findRandomTargetBlock(实体Creature entitycreatureIn, int xz, int y, Vec3 targetVec3)
    {
        Random random = entitycreatureIn.getRNG();
        boolean flag = false;
        int i = 0;
        int j = 0;
        int k = 0;
        float f = -99999.0F;
        boolean flag1;

        if (entitycreatureIn.hasHome())
        {
            double d0 = entitycreatureIn.getHomePosition().distanceSq((double)MathHelper.floor_double(entitycreatureIn.X坐标), (double)MathHelper.floor_double(entitycreatureIn.Y坐标), (double)MathHelper.floor_double(entitycreatureIn.Z坐标)) + 4.0D;
            double d1 = (double)(entitycreatureIn.getMaximumHomeDistance() + (float)xz);
            flag1 = d0 < d1 * d1;
        }
        else
        {
            flag1 = false;
        }

        for (int j1 = 0; j1 < 10; ++j1)
        {
            int l = random.nextInt(2 * xz + 1) - xz;
            int k1 = random.nextInt(2 * y + 1) - y;
            int i1 = random.nextInt(2 * xz + 1) - xz;

            if (targetVec3 == null || (double)l * targetVec3.xCoord + (double)i1 * targetVec3.zCoord >= 0.0D)
            {
                if (entitycreatureIn.hasHome() && xz > 1)
                {
                    阻止位置 blockpos = entitycreatureIn.getHomePosition();

                    if (entitycreatureIn.X坐标 > (double)blockpos.getX())
                    {
                        l -= random.nextInt(xz / 2);
                    }
                    else
                    {
                        l += random.nextInt(xz / 2);
                    }

                    if (entitycreatureIn.Z坐标 > (double)blockpos.getZ())
                    {
                        i1 -= random.nextInt(xz / 2);
                    }
                    else
                    {
                        i1 += random.nextInt(xz / 2);
                    }
                }

                l = l + MathHelper.floor_double(entitycreatureIn.X坐标);
                k1 = k1 + MathHelper.floor_double(entitycreatureIn.Y坐标);
                i1 = i1 + MathHelper.floor_double(entitycreatureIn.Z坐标);
                阻止位置 blockpos1 = new 阻止位置(l, k1, i1);

                if (!flag1 || entitycreatureIn.isWithinHomeDistanceFromPosition(blockpos1))
                {
                    float f1 = entitycreatureIn.getBlockPathWeight(blockpos1);

                    if (f1 > f)
                    {
                        f = f1;
                        i = l;
                        j = k1;
                        k = i1;
                        flag = true;
                    }
                }
            }
        }

        if (flag)
        {
            return new Vec3((double)i, (double)j, (double)k);
        }
        else
        {
            return null;
        }
    }
}
