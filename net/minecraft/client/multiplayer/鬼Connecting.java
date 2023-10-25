package net.minecraft.client.multiplayer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.client.gui.鬼Button;
import net.minecraft.client.gui.鬼Disconnected;
import net.minecraft.client.我的手艺;
import net.minecraft.client.gui.鬼Screen;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.util.交流组分文本;
import net.minecraft.util.ChatComponentTranslation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class 鬼Connecting extends 鬼Screen
{
    private static final AtomicInteger CONNECTION_ID = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();
    private NetworkManager networkManager;
    private boolean cancel;
    private final 鬼Screen previousGuiScreen;

    public 鬼Connecting(鬼Screen p_i1181_1_, 我的手艺 mcIn, ServerData p_i1181_3_)
    {
        this.mc = mcIn;
        this.previousGuiScreen = p_i1181_1_;
        ServerAddress serveraddress = ServerAddress.fromString(p_i1181_3_.serverIP);
        mcIn.loadWorld((WorldClient)null);
        mcIn.setServerData(p_i1181_3_);
        this.connect(serveraddress.getIP(), serveraddress.getPort());
    }

    public 鬼Connecting(鬼Screen p_i1182_1_, 我的手艺 mcIn, String hostName, int port)
    {
        this.mc = mcIn;
        this.previousGuiScreen = p_i1182_1_;
        mcIn.loadWorld((WorldClient)null);
        this.connect(hostName, port);
    }

    private void connect(final String ip, final int port)
    {
        logger.info("Connecting to " + ip + ", " + port);
        (new Thread("Server Connector #" + CONNECTION_ID.incrementAndGet())
        {
            public void run()
            {
                InetAddress inetaddress = null;

                try
                {
                    if (鬼Connecting.this.cancel)
                    {
                        return;
                    }

                    inetaddress = InetAddress.getByName(ip);
                    鬼Connecting.this.networkManager = NetworkManager.createNetworkManagerAndConnect(inetaddress, port, 鬼Connecting.this.mc.游戏一窝.isUsingNativeTransport());
                    鬼Connecting.this.networkManager.setNetHandler(new NetHandlerLoginClient(鬼Connecting.this.networkManager, 鬼Connecting.this.mc, 鬼Connecting.this.previousGuiScreen));
                    鬼Connecting.this.networkManager.sendPacket(new C00Handshake(47, ip, port, EnumConnectionState.LOGIN));
                    鬼Connecting.this.networkManager.sendPacket(new C00PacketLoginStart(鬼Connecting.this.mc.getSession().getProfile()));
                }
                catch (UnknownHostException unknownhostexception)
                {
                    if (鬼Connecting.this.cancel)
                    {
                        return;
                    }

                    鬼Connecting.logger.error((String)"Couldn\'t connect to server", (Throwable)unknownhostexception);
                    鬼Connecting.this.mc.displayGuiScreen(new 鬼Disconnected(鬼Connecting.this.previousGuiScreen, "connect.failed", new ChatComponentTranslation("disconnect.genericReason", new Object[] {"Unknown host"})));
                }
                catch (Exception exception)
                {
                    if (鬼Connecting.this.cancel)
                    {
                        return;
                    }

                    鬼Connecting.logger.error((String)"Couldn\'t connect to server", (Throwable)exception);
                    String s = exception.toString();

                    if (inetaddress != null)
                    {
                        String s1 = inetaddress.toString() + ":" + port;
                        s = s.replaceAll(s1, "");
                    }

                    鬼Connecting.this.mc.displayGuiScreen(new 鬼Disconnected(鬼Connecting.this.previousGuiScreen, "connect.failed", new ChatComponentTranslation("disconnect.genericReason", new Object[] {s})));
                }
            }
        }).start();
    }

    public void updateScreen()
    {
        if (this.networkManager != null)
        {
            if (this.networkManager.isChannelOpen())
            {
                this.networkManager.processReceivedPackets();
            }
            else
            {
                this.networkManager.checkDisconnected();
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    }

    public void initGui()
    {
        this.buttonList.clear();
        this.buttonList.add(new 鬼Button(0, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
    }

    protected void actionPerformed(鬼Button button) throws IOException
    {
        if (button.id == 0)
        {
            this.cancel = true;

            if (this.networkManager != null)
            {
                this.networkManager.closeChannel(new 交流组分文本("Aborted"));
            }

            this.mc.displayGuiScreen(this.previousGuiScreen);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        if (this.networkManager == null)
        {
            this.drawCenteredString(this.fontRendererObj, I18n.format("connect.connecting", new Object[0]), this.width / 2, this.height / 2 - 50, 16777215);
        }
        else
        {
            this.drawCenteredString(this.fontRendererObj, I18n.format("connect.authorizing", new Object[0]), this.width / 2, this.height / 2 - 50, 16777215);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
