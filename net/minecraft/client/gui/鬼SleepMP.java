package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class 鬼SleepMP extends 鬼Chat
{
    public void initGui()
    {
        super.initGui();
        this.buttonList.add(new 鬼Button(1, this.width / 2 - 100, this.height - 40, I18n.format("multiplayer.stopSleeping", new Object[0])));
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1)
        {
            this.wakeFromSleep();
        }
        else if (keyCode != 28 && keyCode != 156)
        {
            super.keyTyped(typedChar, keyCode);
        }
        else
        {
            String s = this.inputField.getText().trim();

            if (!s.isEmpty())
            {
                this.mc.宇轩游玩者.sendChatMessage(s);
            }

            this.inputField.setText("");
            this.mc.ingameGUI.getChatGUI().resetScroll();
        }
    }

    protected void actionPerformed(鬼Button button) throws IOException
    {
        if (button.id == 1)
        {
            this.wakeFromSleep();
        }
        else
        {
            super.actionPerformed(button);
        }
    }

    private void wakeFromSleep()
    {
        NetHandlerPlayClient nethandlerplayclient = this.mc.宇轩游玩者.sendQueue;
        nethandlerplayclient.addToSendQueue(new C0BPacketEntityAction(this.mc.宇轩游玩者, C0BPacketEntityAction.Action.STOP_SLEEPING));
    }
}
