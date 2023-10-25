package net.minecraft.world.pathfinder;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.实体;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class SwimNodeProcessor extends NodeProcessor
{
    public void initProcessor(IBlockAccess iblockaccessIn, 实体 实体In)
    {
        super.initProcessor(iblockaccessIn, 实体In);
    }

    public void postProcess()
    {
        super.postProcess();
    }

    public PathPoint getPathPointTo(实体 实体In)
    {
        return this.openPoint(MathHelper.floor_double(实体In.getEntityBoundingBox().minX), MathHelper.floor_double(实体In.getEntityBoundingBox().minY + 0.5D), MathHelper.floor_double(实体In.getEntityBoundingBox().minZ));
    }

    public PathPoint getPathPointToCoords(实体 实体In, double x, double y, double target)
    {
        return this.openPoint(MathHelper.floor_double(x - (double)(实体In.width / 2.0F)), MathHelper.floor_double(y + 0.5D), MathHelper.floor_double(target - (double)(实体In.width / 2.0F)));
    }

    public int findPathOptions(PathPoint[] pathOptions, 实体 实体In, PathPoint currentPoint, PathPoint targetPoint, float maxDistance)
    {
        int i = 0;

        for (EnumFacing enumfacing : EnumFacing.values())
        {
            PathPoint pathpoint = this.getSafePoint(实体In, currentPoint.xCoord + enumfacing.getFrontOffsetX(), currentPoint.yCoord + enumfacing.getFrontOffsetY(), currentPoint.zCoord + enumfacing.getFrontOffsetZ());

            if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint;
            }
        }

        return i;
    }

    private PathPoint getSafePoint(实体 实体In, int x, int y, int z)
    {
        int i = this.func_176186_b(实体In, x, y, z);
        return i == -1 ? this.openPoint(x, y, z) : null;
    }

    private int func_176186_b(实体 实体In, int x, int y, int z)
    {
        阻止位置.Mutable阻止位置 blockpos$mutableblockpos = new 阻止位置.Mutable阻止位置();

        for (int i = x; i < x + this.entitySizeX; ++i)
        {
            for (int j = y; j < y + this.entitySizeY; ++j)
            {
                for (int k = z; k < z + this.entitySizeZ; ++k)
                {
                    Block block = this.blockaccess.getBlockState(blockpos$mutableblockpos.set(i, j, k)).getBlock();

                    if (block.getMaterial() != Material.water)
                    {
                        return 0;
                    }
                }
            }
        }

        return -1;
    }
}
