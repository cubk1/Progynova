package net.minecraft.client.renderer;

import net.minecraft.client.resources.model.Model图像位置;
import net.minecraft.item.ItemStack;

public interface ItemMeshDefinition
{
    Model图像位置 getModelLocation(ItemStack stack);
}
