package net.minecraft.world.pathfinder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.entity.实体;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.阻止位置;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class WalkNodeProcessor extends NodeProcessor
{
    private boolean canEnterDoors;
    private boolean canBreakDoors;
    private boolean avoidsWater;
    private boolean canSwim;
    private boolean shouldAvoidWater;

    public void initProcessor(IBlockAccess iblockaccessIn, 实体 实体In)
    {
        super.initProcessor(iblockaccessIn, 实体In);
        this.shouldAvoidWater = this.avoidsWater;
    }

    public void postProcess()
    {
        super.postProcess();
        this.avoidsWater = this.shouldAvoidWater;
    }

    public PathPoint getPathPointTo(实体 实体In)
    {
        int i;

        if (this.canSwim && 实体In.isInWater())
        {
            i = (int) 实体In.getEntityBoundingBox().minY;
            阻止位置.Mutable阻止位置 blockpos$mutableblockpos = new 阻止位置.Mutable阻止位置(MathHelper.floor_double(实体In.X坐标), i, MathHelper.floor_double(实体In.Z坐标));

            for (Block block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock(); block == Blocks.flowing_water || block == Blocks.water; block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock())
            {
                ++i;
                blockpos$mutableblockpos.set(MathHelper.floor_double(实体In.X坐标), i, MathHelper.floor_double(实体In.Z坐标));
            }

            this.avoidsWater = false;
        }
        else
        {
            i = MathHelper.floor_double(实体In.getEntityBoundingBox().minY + 0.5D);
        }

        return this.openPoint(MathHelper.floor_double(实体In.getEntityBoundingBox().minX), i, MathHelper.floor_double(实体In.getEntityBoundingBox().minZ));
    }

    public PathPoint getPathPointToCoords(实体 实体In, double x, double y, double target)
    {
        return this.openPoint(MathHelper.floor_double(x - (double)(实体In.width / 2.0F)), MathHelper.floor_double(y), MathHelper.floor_double(target - (double)(实体In.width / 2.0F)));
    }

    public int findPathOptions(PathPoint[] pathOptions, 实体 实体In, PathPoint currentPoint, PathPoint targetPoint, float maxDistance)
    {
        int i = 0;
        int j = 0;

        if (this.getVerticalOffset(实体In, currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord) == 1)
        {
            j = 1;
        }

        PathPoint pathpoint = this.getSafePoint(实体In, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord + 1, j);
        PathPoint pathpoint1 = this.getSafePoint(实体In, currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord, j);
        PathPoint pathpoint2 = this.getSafePoint(实体In, currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord, j);
        PathPoint pathpoint3 = this.getSafePoint(实体In, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord - 1, j);

        if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint;
        }

        if (pathpoint1 != null && !pathpoint1.visited && pathpoint1.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint1;
        }

        if (pathpoint2 != null && !pathpoint2.visited && pathpoint2.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint2;
        }

        if (pathpoint3 != null && !pathpoint3.visited && pathpoint3.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint3;
        }

        return i;
    }

    private PathPoint getSafePoint(实体 实体In, int x, int y, int z, int p_176171_5_)
    {
        PathPoint pathpoint = null;
        int i = this.getVerticalOffset(实体In, x, y, z);

        if (i == 2)
        {
            return this.openPoint(x, y, z);
        }
        else
        {
            if (i == 1)
            {
                pathpoint = this.openPoint(x, y, z);
            }

            if (pathpoint == null && p_176171_5_ > 0 && i != -3 && i != -4 && this.getVerticalOffset(实体In, x, y + p_176171_5_, z) == 1)
            {
                pathpoint = this.openPoint(x, y + p_176171_5_, z);
                y += p_176171_5_;
            }

            if (pathpoint != null)
            {
                int j = 0;
                int k;

                for (k = 0; y > 0; pathpoint = this.openPoint(x, y, z))
                {
                    k = this.getVerticalOffset(实体In, x, y - 1, z);

                    if (this.avoidsWater && k == -1)
                    {
                        return null;
                    }

                    if (k != 1)
                    {
                        break;
                    }

                    if (j++ >= 实体In.getMaxFallHeight())
                    {
                        return null;
                    }

                    --y;

                    if (y <= 0)
                    {
                        return null;
                    }
                }

                if (k == -2)
                {
                    return null;
                }
            }

            return pathpoint;
        }
    }

    private int getVerticalOffset(实体 实体In, int x, int y, int z)
    {
        return func_176170_a(this.blockaccess, 实体In, x, y, z, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.avoidsWater, this.canBreakDoors, this.canEnterDoors);
    }

    public static int func_176170_a(IBlockAccess blockaccessIn, 实体 实体In, int x, int y, int z, int sizeX, int sizeY, int sizeZ, boolean avoidWater, boolean breakDoors, boolean enterDoors)
    {
        boolean flag = false;
        阻止位置 blockpos = new 阻止位置(实体In);
        阻止位置.Mutable阻止位置 blockpos$mutableblockpos = new 阻止位置.Mutable阻止位置();

        for (int i = x; i < x + sizeX; ++i)
        {
            for (int j = y; j < y + sizeY; ++j)
            {
                for (int k = z; k < z + sizeZ; ++k)
                {
                    blockpos$mutableblockpos.set(i, j, k);
                    Block block = blockaccessIn.getBlockState(blockpos$mutableblockpos).getBlock();

                    if (block.getMaterial() != Material.air)
                    {
                        if (block != Blocks.trapdoor && block != Blocks.iron_trapdoor)
                        {
                            if (block != Blocks.flowing_water && block != Blocks.water)
                            {
                                if (!enterDoors && block instanceof BlockDoor && block.getMaterial() == Material.wood)
                                {
                                    return 0;
                                }
                            }
                            else
                            {
                                if (avoidWater)
                                {
                                    return -1;
                                }

                                flag = true;
                            }
                        }
                        else
                        {
                            flag = true;
                        }

                        if (实体In.worldObj.getBlockState(blockpos$mutableblockpos).getBlock() instanceof BlockRailBase)
                        {
                            if (!(实体In.worldObj.getBlockState(blockpos).getBlock() instanceof BlockRailBase) && !(实体In.worldObj.getBlockState(blockpos.down()).getBlock() instanceof BlockRailBase))
                            {
                                return -3;
                            }
                        }
                        else if (!block.isPassable(blockaccessIn, blockpos$mutableblockpos) && (!breakDoors || !(block instanceof BlockDoor) || block.getMaterial() != Material.wood))
                        {
                            if (block instanceof BlockFence || block instanceof BlockFenceGate || block instanceof BlockWall)
                            {
                                return -3;
                            }

                            if (block == Blocks.trapdoor || block == Blocks.iron_trapdoor)
                            {
                                return -4;
                            }

                            Material material = block.getMaterial();

                            if (material != Material.lava)
                            {
                                return 0;
                            }

                            if (!实体In.isInLava())
                            {
                                return -2;
                            }
                        }
                    }
                }
            }
        }

        return flag ? 2 : 1;
    }

    public void setEnterDoors(boolean canEnterDoorsIn)
    {
        this.canEnterDoors = canEnterDoorsIn;
    }

    public void setBreakDoors(boolean canBreakDoorsIn)
    {
        this.canBreakDoors = canBreakDoorsIn;
    }

    public void setAvoidsWater(boolean avoidsWaterIn)
    {
        this.avoidsWater = avoidsWaterIn;
    }

    public void setCanSwim(boolean canSwimIn)
    {
        this.canSwim = canSwimIn;
    }

    public boolean getEnterDoors()
    {
        return this.canEnterDoors;
    }

    public boolean getCanSwim()
    {
        return this.canSwim;
    }

    public boolean getAvoidsWater()
    {
        return this.avoidsWater;
    }
}
