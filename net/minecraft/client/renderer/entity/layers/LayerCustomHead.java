package net.minecraft.client.renderer.entity.layers;

import com.mojang.authlib.GameProfile;
import java.util.UUID;

import net.minecraft.client.我的手艺;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.光照状态经理;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.monster.实体Zombie;
import net.minecraft.entity.实体LivingBase;
import net.minecraft.entity.passive.实体Villager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StringUtils;

public class LayerCustomHead implements LayerRenderer<实体LivingBase>
{
    private final ModelRenderer field_177209_a;

    public LayerCustomHead(ModelRenderer p_i46120_1_)
    {
        this.field_177209_a = p_i46120_1_;
    }

    public void doRenderLayer(实体LivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        ItemStack itemstack = entitylivingbaseIn.getCurrentArmor(3);

        if (itemstack != null && itemstack.getItem() != null)
        {
            Item item = itemstack.getItem();
            我的手艺 宇轩的世界 = 我的手艺.得到我的手艺();
            光照状态经理.推黑客帝国();

            if (entitylivingbaseIn.正在下蹲())
            {
                光照状态经理.理解(0.0F, 0.2F, 0.0F);
            }

            boolean flag = entitylivingbaseIn instanceof 实体Villager || entitylivingbaseIn instanceof 实体Zombie && ((实体Zombie)entitylivingbaseIn).isVillager();

            if (!flag && entitylivingbaseIn.isChild())
            {
                float f = 2.0F;
                float f1 = 1.4F;
                光照状态经理.障眼物(f1 / f, f1 / f, f1 / f);
                光照状态经理.理解(0.0F, 16.0F * scale, 0.0F);
            }

            this.field_177209_a.postRender(0.0625F);
            光照状态经理.色彩(1.0F, 1.0F, 1.0F, 1.0F);

            if (item instanceof ItemBlock)
            {
                float f2 = 0.625F;
                光照状态经理.理解(0.0F, -0.25F, 0.0F);
                光照状态经理.辐射(180.0F, 0.0F, 1.0F, 0.0F);
                光照状态经理.障眼物(f2, -f2, -f2);

                if (flag)
                {
                    光照状态经理.理解(0.0F, 0.1875F, 0.0F);
                }

                宇轩的世界.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.HEAD);
            }
            else if (item == Items.skull)
            {
                float f3 = 1.1875F;
                光照状态经理.障眼物(f3, -f3, -f3);

                if (flag)
                {
                    光照状态经理.理解(0.0F, 0.0625F, 0.0F);
                }

                GameProfile gameprofile = null;

                if (itemstack.hasTagCompound())
                {
                    NBTTagCompound nbttagcompound = itemstack.getTagCompound();

                    if (nbttagcompound.hasKey("SkullOwner", 10))
                    {
                        gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
                    }
                    else if (nbttagcompound.hasKey("SkullOwner", 8))
                    {
                        String s = nbttagcompound.getString("SkullOwner");

                        if (!StringUtils.isNullOrEmpty(s))
                        {
                            gameprofile = TileEntitySkull.updateGameprofile(new GameProfile((UUID)null, s));
                            nbttagcompound.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
                        }
                    }
                }

                TileEntitySkullRenderer.instance.renderSkull(-0.5F, 0.0F, -0.5F, EnumFacing.UP, 180.0F, itemstack.getMetadata(), gameprofile, -1);
            }

            光照状态经理.流行音乐黑客帝国();
        }
    }

    public boolean shouldCombineTextures()
    {
        return true;
    }
}
