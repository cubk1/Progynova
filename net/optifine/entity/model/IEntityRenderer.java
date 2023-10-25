package net.optifine.entity.model;

import net.minecraft.util.图像位置;

public interface IEntityRenderer
{
    Class getEntityClass();

    void setEntityClass(Class var1);

    图像位置 getLocationTextureCustom();

    void setLocationTextureCustom(图像位置 var1);
}
