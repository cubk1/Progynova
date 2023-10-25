package net.optifine.config;

import net.minecraft.util.图像位置;
import net.optifine.util.EntityUtils;

public class EntityClassLocator implements IObjectLocator
{
    public Object getObject(图像位置 loc)
    {
        Class oclass = EntityUtils.getEntityClassByName(loc.getResourcePath());
        return oclass;
    }
}
