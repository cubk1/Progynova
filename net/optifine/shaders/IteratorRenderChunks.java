package net.optifine.shaders;

import java.util.Iterator;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.阻止位置;
import net.optifine.阻止位置M;

public class IteratorRenderChunks implements Iterator<RenderChunk>
{
    private ViewFrustum viewFrustum;
    private Iterator3d Iterator3d;
    private 阻止位置M posBlock = new 阻止位置M(0, 0, 0);

    public IteratorRenderChunks(ViewFrustum viewFrustum, 阻止位置 posStart, 阻止位置 posEnd, int width, int height)
    {
        this.viewFrustum = viewFrustum;
        this.Iterator3d = new Iterator3d(posStart, posEnd, width, height);
    }

    public boolean hasNext()
    {
        return this.Iterator3d.hasNext();
    }

    public RenderChunk next()
    {
        阻止位置 blockpos = this.Iterator3d.next();
        this.posBlock.setXyz(blockpos.getX() << 4, blockpos.getY() << 4, blockpos.getZ() << 4);
        RenderChunk renderchunk = this.viewFrustum.getRenderChunk(this.posBlock);
        return renderchunk;
    }

    public void remove()
    {
        throw new RuntimeException("Not implemented");
    }
}
