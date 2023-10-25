package net.minecraft.client.resources;

import java.util.UUID;
import net.minecraft.util.图像位置;

public class DefaultPlayerSkin
{
    private static final 图像位置 TEXTURE_STEVE = new 图像位置("textures/entity/steve.png");
    private static final 图像位置 TEXTURE_ALEX = new 图像位置("textures/entity/alex.png");

    public static 图像位置 getDefaultSkinLegacy()
    {
        return TEXTURE_STEVE;
    }

    public static 图像位置 getDefaultSkin(UUID playerUUID)
    {
        return isSlimSkin(playerUUID) ? TEXTURE_ALEX : TEXTURE_STEVE;
    }

    public static String getSkinType(UUID playerUUID)
    {
        return isSlimSkin(playerUUID) ? "slim" : "default";
    }

    private static boolean isSlimSkin(UUID playerUUID)
    {
        return (playerUUID.hashCode() & 1) == 1;
    }
}
