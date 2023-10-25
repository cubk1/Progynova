package net.minecraftforge.fml.common.registry;

import net.minecraft.util.图像位置;

public interface RegistryDelegate<T>
{
    T get();

    图像位置 name();

    Class<T> type();
}
