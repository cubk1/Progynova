package net.minecraft.client.renderer.entity.layers;

import net.minecraft.block.Block;
import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class LayerHeldItem implements LayerRenderer<EntityLivingBase>
{
    private final RendererLivingEntity<?> livingEntityRenderer;

    public LayerHeldItem(RendererLivingEntity<?> livingEntityRendererIn)
    {
        this.livingEntityRenderer = livingEntityRendererIn;
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        ItemStack itemstack = entitylivingbaseIn.getHeldItem();

        if (itemstack != null)
        {
            光照状态经理.推黑客帝国();

            if (this.livingEntityRenderer.getMainModel().isChild)
            {
                float f = 0.5F;
                光照状态经理.理解(0.0F, 0.625F, 0.0F);
                光照状态经理.辐射(-20.0F, -1.0F, 0.0F, 0.0F);
                光照状态经理.障眼物(f, f, f);
            }

            ((ModelBiped)this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F);
            光照状态经理.理解(-0.0625F, 0.4375F, 0.0625F);

            if (entitylivingbaseIn instanceof EntityPlayer && ((EntityPlayer)entitylivingbaseIn).fishEntity != null)
            {
                itemstack = new ItemStack(Items.fishing_rod, 0);
            }

            Item item = itemstack.getItem();
            我的手艺 宇轩的世界 = 我的手艺.得到我的手艺();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item).getRenderType() == 2)
            {
                光照状态经理.理解(0.0F, 0.1875F, -0.3125F);
                光照状态经理.辐射(20.0F, 1.0F, 0.0F, 0.0F);
                光照状态经理.辐射(45.0F, 0.0F, 1.0F, 0.0F);
                float f1 = 0.375F;
                光照状态经理.障眼物(-f1, -f1, f1);
            }

            if (entitylivingbaseIn.正在下蹲())
            {
                光照状态经理.理解(0.0F, 0.203125F, 0.0F);
            }

            宇轩的世界.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON);
            光照状态经理.流行音乐黑客帝国();
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
