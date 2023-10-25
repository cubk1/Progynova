package net.minecraft.client.renderer.block.statemap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.Model图像位置;
import net.minecraft.util.图像位置;

public class DefaultStateMapper extends StateMapperBase
{
    protected Model图像位置 getModelResourceLocation(IBlockState state)
    {
        return new Model图像位置((图像位置)Block.blockRegistry.getNameForObject(state.getBlock()), this.getPropertyString(state.getProperties()));
    }
}
