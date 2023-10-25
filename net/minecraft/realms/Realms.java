package net.minecraft.realms;

import com.google.common.util.concurrent.ListenableFuture;
import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;
import java.net.Proxy;

import net.minecraft.client.gui.鬼MainMenu;
import net.minecraft.client.我的手艺;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.Session;
import net.minecraft.world.WorldSettings;

public class Realms
{
    public static boolean isTouchScreen()
    {
        return 我的手艺.得到我的手艺().游戏一窝.touchscreen;
    }

    public static Proxy getProxy()
    {
        return 我的手艺.得到我的手艺().getProxy();
    }

    public static String sessionId()
    {
        Session session = 我的手艺.得到我的手艺().getSession();
        return session == null ? null : session.getSessionID();
    }

    public static String userName()
    {
        Session session = 我的手艺.得到我的手艺().getSession();
        return session == null ? null : session.getUsername();
    }

    public static long currentTimeMillis()
    {
        return 我的手艺.getSystemTime();
    }

    public static String getSessionId()
    {
        return 我的手艺.得到我的手艺().getSession().getSessionID();
    }

    public static String getUUID()
    {
        return 我的手艺.得到我的手艺().getSession().getPlayerID();
    }

    public static String getName()
    {
        return 我的手艺.得到我的手艺().getSession().getUsername();
    }

    public static String uuidToName(String p_uuidToName_0_)
    {
        return 我的手艺.得到我的手艺().getSessionService().fillProfileProperties(new GameProfile(UUIDTypeAdapter.fromString(p_uuidToName_0_), (String)null), false).getName();
    }

    public static void setScreen(RealmsScreen p_setScreen_0_)
    {
        我的手艺.得到我的手艺().displayGuiScreen(p_setScreen_0_.getProxy());
    }

    public static String getGameDirectoryPath()
    {
        return 我的手艺.得到我的手艺().mcDataDir.getAbsolutePath();
    }

    public static int survivalId()
    {
        return WorldSettings.GameType.SURVIVAL.getID();
    }

    public static int creativeId()
    {
        return WorldSettings.GameType.CREATIVE.getID();
    }

    public static int adventureId()
    {
        return WorldSettings.GameType.ADVENTURE.getID();
    }

    public static int spectatorId()
    {
        return WorldSettings.GameType.SPECTATOR.getID();
    }

    public static void setConnectedToRealms(boolean p_setConnectedToRealms_0_)
    {
        我的手艺.得到我的手艺().setConnectedToRealms(p_setConnectedToRealms_0_);
    }

    public static ListenableFuture<Object> downloadResourcePack(String p_downloadResourcePack_0_, String p_downloadResourcePack_1_)
    {
        ListenableFuture<Object> listenablefuture = 我的手艺.得到我的手艺().getResourcePackRepository().downloadResourcePack(p_downloadResourcePack_0_, p_downloadResourcePack_1_);
        return listenablefuture;
    }

    public static void clearResourcePack()
    {
        我的手艺.得到我的手艺().getResourcePackRepository().clearResourcePack();
    }

    public static boolean getRealmsNotificationsEnabled()
    {
        return 我的手艺.得到我的手艺().游戏一窝.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS);
    }

    public static boolean inTitleScreen()
    {
        return 我的手艺.得到我的手艺().currentScreen != null && 我的手艺.得到我的手艺().currentScreen instanceof 鬼MainMenu;
    }
}
