package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.阻止位置;
import net.minecraft.world.World;

public interface IRenderChunkFactory
{
    RenderChunk makeRenderChunk(World worldIn, RenderGlobal globalRenderer, 阻止位置 pos, int index);
}
