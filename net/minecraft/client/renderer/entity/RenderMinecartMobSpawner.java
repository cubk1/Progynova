package net.minecraft.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntityMobSpawnerRenderer;
import net.minecraft.entity.ai.实体MinecartMobSpawner;
import net.minecraft.init.Blocks;

public class RenderMinecartMobSpawner extends RenderMinecart<实体MinecartMobSpawner>
{
    public RenderMinecartMobSpawner(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    protected void func_180560_a(实体MinecartMobSpawner minecart, float partialTicks, IBlockState state)
    {
        super.func_180560_a(minecart, partialTicks, state);

        if (state.getBlock() == Blocks.mob_spawner)
        {
            TileEntityMobSpawnerRenderer.renderMob(minecart.func_98039_d(), minecart.X坐标, minecart.Y坐标, minecart.Z坐标, partialTicks);
        }
    }
}
