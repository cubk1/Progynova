package net.minecraft.client.renderer.entity.layers;

import net.minecraft.block.Block;
import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderWitch;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class LayerHeldItemWitch implements LayerRenderer<EntityWitch>
{
    private final RenderWitch witchRenderer;

    public LayerHeldItemWitch(RenderWitch witchRendererIn)
    {
        this.witchRenderer = witchRendererIn;
    }

    public void doRenderLayer(EntityWitch entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        ItemStack itemstack = entitylivingbaseIn.getHeldItem();

        if (itemstack != null)
        {
            光照状态经理.色彩(1.0F, 1.0F, 1.0F);
            光照状态经理.推黑客帝国();

            if (this.witchRenderer.getMainModel().isChild)
            {
                光照状态经理.理解(0.0F, 0.625F, 0.0F);
                光照状态经理.辐射(-20.0F, -1.0F, 0.0F, 0.0F);
                float f = 0.5F;
                光照状态经理.障眼物(f, f, f);
            }

            ((ModelWitch)this.witchRenderer.getMainModel()).villagerNose.postRender(0.0625F);
            光照状态经理.理解(-0.0625F, 0.53125F, 0.21875F);
            Item item = itemstack.getItem();
            我的手艺 宇轩的世界 = 我的手艺.得到我的手艺();

            if (item instanceof ItemBlock && 宇轩的世界.getBlockRendererDispatcher().isRenderTypeChest(Block.getBlockFromItem(item), itemstack.getMetadata()))
            {
                光照状态经理.理解(0.0F, 0.0625F, -0.25F);
                光照状态经理.辐射(30.0F, 1.0F, 0.0F, 0.0F);
                光照状态经理.辐射(-5.0F, 0.0F, 1.0F, 0.0F);
                float f4 = 0.375F;
                光照状态经理.障眼物(f4, -f4, f4);
            }
            else if (item == Items.bow)
            {
                光照状态经理.理解(0.0F, 0.125F, -0.125F);
                光照状态经理.辐射(-45.0F, 0.0F, 1.0F, 0.0F);
                float f1 = 0.625F;
                光照状态经理.障眼物(f1, -f1, f1);
                光照状态经理.辐射(-100.0F, 1.0F, 0.0F, 0.0F);
                光照状态经理.辐射(-20.0F, 0.0F, 1.0F, 0.0F);
            }
            else if (item.isFull3D())
            {
                if (item.shouldRotateAroundWhenRendering())
                {
                    光照状态经理.辐射(180.0F, 0.0F, 0.0F, 1.0F);
                    光照状态经理.理解(0.0F, -0.0625F, 0.0F);
                }

                this.witchRenderer.transformHeldFull3DItemLayer();
                光照状态经理.理解(0.0625F, -0.125F, 0.0F);
                float f2 = 0.625F;
                光照状态经理.障眼物(f2, -f2, f2);
                光照状态经理.辐射(0.0F, 1.0F, 0.0F, 0.0F);
                光照状态经理.辐射(0.0F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                光照状态经理.理解(0.1875F, 0.1875F, 0.0F);
                float f3 = 0.875F;
                光照状态经理.障眼物(f3, f3, f3);
                光照状态经理.辐射(-20.0F, 0.0F, 0.0F, 1.0F);
                光照状态经理.辐射(-60.0F, 1.0F, 0.0F, 0.0F);
                光照状态经理.辐射(-30.0F, 0.0F, 0.0F, 1.0F);
            }

            光照状态经理.辐射(-15.0F, 1.0F, 0.0F, 0.0F);
            光照状态经理.辐射(40.0F, 0.0F, 0.0F, 1.0F);
            宇轩的世界.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON);
            光照状态经理.流行音乐黑客帝国();
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
