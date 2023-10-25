package net.optifine.config;

import net.minecraft.item.Item;
import net.minecraft.util.图像位置;

public class ItemLocator implements IObjectLocator
{
    public Object getObject(图像位置 loc)
    {
        Item item = Item.getByNameOrId(loc.toString());
        return item;
    }
}
