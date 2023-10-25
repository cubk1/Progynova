package net.minecraft.client.renderer.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.阻止位置;
import net.minecraft.world.World;

public class TileEntityPistonRenderer extends TileEntitySpecialRenderer<TileEntityPiston>
{
    private final BlockRendererDispatcher blockRenderer = 我的手艺.得到我的手艺().getBlockRendererDispatcher();

    public void renderTileEntityAt(TileEntityPiston te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        阻止位置 blockpos = te.getPos();
        IBlockState iblockstate = te.getPistonState();
        Block block = iblockstate.getBlock();

        if (block.getMaterial() != Material.air && te.getProgress(partialTicks) < 1.0F)
        {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            this.bindTexture(TextureMap.locationBlocksTexture);
            RenderHelper.disableStandardItemLighting();
            光照状态经理.正常工作(770, 771);
            光照状态经理.启用混合品();
            光照状态经理.disableCull();

            if (我的手艺.isAmbientOcclusionEnabled())
            {
                光照状态经理.shadeModel(7425);
            }
            else
            {
                光照状态经理.shadeModel(7424);
            }

            worldrenderer.begin(7, DefaultVertexFormats.BLOCK);
            worldrenderer.setTranslation((double)((float)x - (float)blockpos.getX() + te.getOffsetX(partialTicks)), (double)((float)y - (float)blockpos.getY() + te.getOffsetY(partialTicks)), (double)((float)z - (float)blockpos.getZ() + te.getOffsetZ(partialTicks)));
            World world = this.getWorld();

            if (block == Blocks.piston_head && te.getProgress(partialTicks) < 0.5F)
            {
                iblockstate = iblockstate.withProperty(BlockPistonExtension.SHORT, Boolean.valueOf(true));
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate, world, blockpos), iblockstate, blockpos, worldrenderer, true);
            }
            else if (te.shouldPistonHeadBeRendered() && !te.isExtending())
            {
                BlockPistonExtension.EnumPistonType blockpistonextension$enumpistontype = block == Blocks.sticky_piston ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
                IBlockState iblockstate1 = Blocks.piston_head.getDefaultState().withProperty(BlockPistonExtension.TYPE, blockpistonextension$enumpistontype).withProperty(BlockPistonExtension.FACING, iblockstate.getValue(BlockPistonBase.FACING));
                iblockstate1 = iblockstate1.withProperty(BlockPistonExtension.SHORT, Boolean.valueOf(te.getProgress(partialTicks) >= 0.5F));
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate1, world, blockpos), iblockstate1, blockpos, worldrenderer, true);
                worldrenderer.setTranslation((double)((float)x - (float)blockpos.getX()), (double)((float)y - (float)blockpos.getY()), (double)((float)z - (float)blockpos.getZ()));
                iblockstate.withProperty(BlockPistonBase.EXTENDED, Boolean.valueOf(true));
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate, world, blockpos), iblockstate, blockpos, worldrenderer, true);
            }
            else
            {
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iblockstate, world, blockpos), iblockstate, blockpos, worldrenderer, false);
            }

            worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
            tessellator.draw();
            RenderHelper.enableStandardItemLighting();
        }
    }
}
