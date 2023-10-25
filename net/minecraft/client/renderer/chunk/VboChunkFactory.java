package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.阻止位置;
import net.minecraft.world.World;

public class VboChunkFactory implements IRenderChunkFactory
{
    public RenderChunk makeRenderChunk(World worldIn, RenderGlobal globalRenderer, 阻止位置 pos, int index)
    {
        return new RenderChunk(worldIn, globalRenderer, pos, index);
    }
}
