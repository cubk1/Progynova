package net.optifine.shaders;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.entity.实体;
import net.minecraft.util.阻止位置;
import net.minecraft.util.MathHelper;

public class ShadowUtils
{
    public static Iterator<RenderChunk> makeShadowChunkIterator(WorldClient world, double partialTicks, 实体 view实体, int renderDistanceChunks, ViewFrustum viewFrustum)
    {
        float f = Shaders.getShadowRenderDistance();

        if (f > 0.0F && f < (float)((renderDistanceChunks - 1) * 16))
        {
            int i = MathHelper.ceiling_float_int(f / 16.0F) + 1;
            float f6 = world.getCelestialAngleRadians((float)partialTicks);
            float f1 = Shaders.sunPathRotation * MathHelper.deg2Rad;
            float f2 = f6 > MathHelper.PId2 && f6 < 3.0F * MathHelper.PId2 ? f6 + MathHelper.PI : f6;
            float f3 = -MathHelper.sin(f2);
            float f4 = MathHelper.cos(f2) * MathHelper.cos(f1);
            float f5 = -MathHelper.cos(f2) * MathHelper.sin(f1);
            阻止位置 blockpos = new 阻止位置(MathHelper.floor_double(view实体.X坐标) >> 4, MathHelper.floor_double(view实体.Y坐标) >> 4, MathHelper.floor_double(view实体.Z坐标) >> 4);
            阻止位置 blockpos1 = blockpos.add((double)(-f3 * (float)i), (double)(-f4 * (float)i), (double)(-f5 * (float)i));
            阻止位置 blockpos2 = blockpos.add((double)(f3 * (float)renderDistanceChunks), (double)(f4 * (float)renderDistanceChunks), (double)(f5 * (float)renderDistanceChunks));
            IteratorRenderChunks iteratorrenderchunks = new IteratorRenderChunks(viewFrustum, blockpos1, blockpos2, i, i);
            return iteratorrenderchunks;
        }
        else
        {
            List<RenderChunk> list = Arrays.<RenderChunk>asList(viewFrustum.renderChunks);
            Iterator<RenderChunk> iterator = list.iterator();
            return iterator;
        }
    }
}
