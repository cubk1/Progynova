package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;

public class ChestRenderer
{
    public void renderChestBrightness(Block p_178175_1_, float color)
    {
        光照状态经理.色彩(color, color, color, 1.0F);
        光照状态经理.辐射(90.0F, 0.0F, 1.0F, 0.0F);
        TileEntityItemStackRenderer.instance.renderByItem(new ItemStack(p_178175_1_));
    }
}
