package net.optifine.gui;

import net.minecraft.client.gui.鬼Chat;
import net.minecraft.client.gui.鬼VideoSettings;
import net.minecraft.src.Config;
import net.optifine.shaders.Shaders;

public class 鬼ChatOF extends 鬼Chat
{
    private static final String CMD_RELOAD_SHADERS = "/reloadShaders";
    private static final String CMD_RELOAD_CHUNKS = "/reloadChunks";

    public 鬼ChatOF(鬼Chat guiChat)
    {
        super(鬼VideoSettings.getGuiChatText(guiChat));
    }

    public void sendChatMessage(String msg)
    {
        if (this.checkCustomCommand(msg))
        {
            this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
        }
        else
        {
            super.sendChatMessage(msg);
        }
    }

    private boolean checkCustomCommand(String msg)
    {
        if (msg == null)
        {
            return false;
        }
        else
        {
            msg = msg.trim();

            if (msg.equals("/reloadShaders"))
            {
                if (Config.isShaders())
                {
                    Shaders.uninit();
                    Shaders.loadShaderPack();
                }

                return true;
            }
            else if (msg.equals("/reloadChunks"))
            {
                this.mc.renderGlobal.loadRenderers();
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
