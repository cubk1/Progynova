package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.阻止位置;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class ListedRenderChunk extends RenderChunk
{
    private final int baseDisplayList = GLAllocation.generateDisplayLists(EnumWorldBlockLayer.values().length);

    public ListedRenderChunk(World worldIn, RenderGlobal renderGlobalIn, 阻止位置 pos, int indexIn)
    {
        super(worldIn, renderGlobalIn, pos, indexIn);
    }

    public int getDisplayList(EnumWorldBlockLayer layer, CompiledChunk p_178600_2_)
    {
        return !p_178600_2_.isLayerEmpty(layer) ? this.baseDisplayList + layer.ordinal() : -1;
    }

    public void deleteGlResources()
    {
        super.deleteGlResources();
        GLAllocation.deleteDisplayLists(this.baseDisplayList, EnumWorldBlockLayer.values().length);
    }
}
