package net.minecraft.realms;

import java.lang.reflect.Constructor;

import net.minecraft.client.gui.鬼Screen;
import net.minecraft.client.gui.鬼ScreenRealmsProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsBridge extends RealmsScreen
{
    private static final Logger LOGGER = LogManager.getLogger();
    private 鬼Screen previousScreen;

    public void switchToRealms(鬼Screen p_switchToRealms_1_)
    {
        this.previousScreen = p_switchToRealms_1_;

        try
        {
            Class<?> oclass = Class.forName("com.mojang.realmsclient.RealmsMainScreen");
            Constructor<?> constructor = oclass.getDeclaredConstructor(new Class[] {RealmsScreen.class});
            constructor.setAccessible(true);
            Object object = constructor.newInstance(new Object[] {this});
            我的手艺.得到我的手艺().displayGuiScreen(((RealmsScreen)object).getProxy());
        }
        catch (Exception exception)
        {
            LOGGER.error((String)"Realms module missing", (Throwable)exception);
        }
    }

    public 鬼ScreenRealmsProxy getNotificationScreen(鬼Screen p_getNotificationScreen_1_)
    {
        try
        {
            this.previousScreen = p_getNotificationScreen_1_;
            Class<?> oclass = Class.forName("com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen");
            Constructor<?> constructor = oclass.getDeclaredConstructor(new Class[] {RealmsScreen.class});
            constructor.setAccessible(true);
            Object object = constructor.newInstance(new Object[] {this});
            return ((RealmsScreen)object).getProxy();
        }
        catch (Exception exception)
        {
            LOGGER.error((String)"Realms module missing", (Throwable)exception);
            return null;
        }
    }

    public void init()
    {
        我的手艺.得到我的手艺().displayGuiScreen(this.previousScreen);
    }
}
