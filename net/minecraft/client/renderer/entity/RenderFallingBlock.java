package net.minecraft.client.renderer.entity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.实体FallingBlock;
import net.minecraft.util.阻止位置;
import net.minecraft.util.图像位置;
import net.minecraft.world.World;

public class RenderFallingBlock extends Render<实体FallingBlock>
{
    public RenderFallingBlock(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
    }

    public void doRender(实体FallingBlock entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if (entity.getBlock() != null)
        {
            this.bindTexture(TextureMap.locationBlocksTexture);
            IBlockState iblockstate = entity.getBlock();
            Block block = iblockstate.getBlock();
            阻止位置 blockpos = new 阻止位置(entity);
            World world = entity.getWorldObj();

            if (iblockstate != world.getBlockState(blockpos) && block.getRenderType() != -1)
            {
                if (block.getRenderType() == 3)
                {
                    光照状态经理.推黑客帝国();
                    光照状态经理.理解((float)x, (float)y, (float)z);
                    光照状态经理.disableLighting();
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    worldrenderer.begin(7, DefaultVertexFormats.BLOCK);
                    int i = blockpos.getX();
                    int j = blockpos.getY();
                    int k = blockpos.getZ();
                    worldrenderer.setTranslation((double)((float)(-i) - 0.5F), (double)(-j), (double)((float)(-k) - 0.5F));
                    BlockRendererDispatcher blockrendererdispatcher = 我的手艺.得到我的手艺().getBlockRendererDispatcher();
                    IBakedModel ibakedmodel = blockrendererdispatcher.getModelFromBlockState(iblockstate, world, (阻止位置)null);
                    blockrendererdispatcher.getBlockModelRenderer().renderModel(world, ibakedmodel, iblockstate, blockpos, worldrenderer, false);
                    worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
                    tessellator.draw();
                    光照状态经理.enableLighting();
                    光照状态经理.流行音乐黑客帝国();
                    super.doRender(entity, x, y, z, entityYaw, partialTicks);
                }
            }
        }
    }

    protected 图像位置 getEntityTexture(实体FallingBlock entity)
    {
        return TextureMap.locationBlocksTexture;
    }
}
