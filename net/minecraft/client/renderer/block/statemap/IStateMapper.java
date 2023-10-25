package net.minecraft.client.renderer.block.statemap;

import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.Model图像位置;

public interface IStateMapper
{
    Map<IBlockState, Model图像位置> putStateModelLocations(Block blockIn);
}
