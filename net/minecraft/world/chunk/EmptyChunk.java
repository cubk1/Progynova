package net.minecraft.world.chunk;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.实体;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.阻止位置;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class EmptyChunk extends Chunk
{
    public EmptyChunk(World worldIn, int x, int z)
    {
        super(worldIn, x, z);
    }

    public boolean isAtLocation(int x, int z)
    {
        return x == this.xPosition && z == this.zPosition;
    }

    public int getHeightValue(int x, int z)
    {
        return 0;
    }

    public void generateHeightMap()
    {
    }

    public void generateSkylightMap()
    {
    }

    public Block getBlock(阻止位置 pos)
    {
        return Blocks.air;
    }

    public int getBlockLightOpacity(阻止位置 pos)
    {
        return 255;
    }

    public int getBlockMetadata(阻止位置 pos)
    {
        return 0;
    }

    public int getLightFor(EnumSkyBlock p_177413_1_, 阻止位置 pos)
    {
        return p_177413_1_.defaultLightValue;
    }

    public void setLightFor(EnumSkyBlock p_177431_1_, 阻止位置 pos, int value)
    {
    }

    public int getLightSubtracted(阻止位置 pos, int amount)
    {
        return 0;
    }

    public void addEntity(实体 实体In)
    {
    }

    public void removeEntity(实体 实体In)
    {
    }

    public void removeEntityAtIndex(实体 实体In, int p_76608_2_)
    {
    }

    public boolean canSeeSky(阻止位置 pos)
    {
        return false;
    }

    public TileEntity getTileEntity(阻止位置 pos, Chunk.EnumCreateEntityType p_177424_2_)
    {
        return null;
    }

    public void addTileEntity(TileEntity tileEntityIn)
    {
    }

    public void addTileEntity(阻止位置 pos, TileEntity tileEntityIn)
    {
    }

    public void removeTileEntity(阻止位置 pos)
    {
    }

    public void onChunkLoad()
    {
    }

    public void onChunkUnload()
    {
    }

    public void setChunkModified()
    {
    }

    public void getEntitiesWithinAABBForEntity(实体 实体In, AxisAlignedBB aabb, List<实体> listToFill, Predicate <? super 实体> p_177414_4_)
    {
    }

    public <T extends 实体> void getEntitiesOfTypeWithinAAAB(Class <? extends T > entityClass, AxisAlignedBB aabb, List<T> listToFill, Predicate <? super T > p_177430_4_)
    {
    }

    public boolean needsSaving(boolean p_76601_1_)
    {
        return false;
    }

    public Random getRandomWithSeed(long seed)
    {
        return new Random(this.getWorld().getSeed() + (long)(this.xPosition * this.xPosition * 4987142) + (long)(this.xPosition * 5947611) + (long)(this.zPosition * this.zPosition) * 4392871L + (long)(this.zPosition * 389711) ^ seed);
    }

    public boolean isEmpty()
    {
        return true;
    }

    public boolean getAreLevelsEmpty(int startY, int endY)
    {
        return true;
    }
}
