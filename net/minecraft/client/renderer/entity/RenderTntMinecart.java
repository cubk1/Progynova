package net.minecraft.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.我的手艺;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;

public class RenderTntMinecart extends RenderMinecart<EntityMinecartTNT>
{
    public RenderTntMinecart(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    protected void func_180560_a(EntityMinecartTNT minecart, float partialTicks, IBlockState state)
    {
        int i = minecart.getFuseTicks();

        if (i > -1 && (float)i - partialTicks + 1.0F < 10.0F)
        {
            float f = 1.0F - ((float)i - partialTicks + 1.0F) / 10.0F;
            f = MathHelper.clamp_float(f, 0.0F, 1.0F);
            f = f * f;
            f = f * f;
            float f1 = 1.0F + f * 0.3F;
            光照状态经理.障眼物(f1, f1, f1);
        }

        super.func_180560_a(minecart, partialTicks, state);

        if (i > -1 && i / 5 % 2 == 0)
        {
            BlockRendererDispatcher blockrendererdispatcher = 我的手艺.得到我的手艺().getBlockRendererDispatcher();
            光照状态经理.禁用手感();
            光照状态经理.disableLighting();
            光照状态经理.启用混合品();
            光照状态经理.正常工作(770, 772);
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, (1.0F - ((float)i - partialTicks + 1.0F) / 100.0F) * 0.8F);
            光照状态经理.推黑客帝国();
            blockrendererdispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), 1.0F);
            光照状态经理.流行音乐黑客帝国();
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);
            光照状态经理.禁用混合品();
            光照状态经理.enableLighting();
            光照状态经理.启用手感();
        }
    }
}
